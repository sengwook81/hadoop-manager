package My;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zero.commons.core.support.network.ssh.SecureShellSession;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

public class NewSshClient {
	private Pattern alphaNumeric = Pattern.compile("([^a-zA-z0-9])");
	Logger logger = LoggerFactory.getLogger(NewSshClient.class);

	private static final String SHELL_MODE = "shell";
	private static final String EXEC_MODE = "exec";
	private static final String SFTP_MODE = "sftp";

	private static final String TERMINATOR = "--ZERO-LINE--";
	
	private Channel channel;
	private Session session;

	public NewSshClient(Session session) {
		this.session = session;
	}

	public static void main(String[] args) throws Exception {
		SecureShellSession sss = new SecureShellSession(3000);
		sss.setHost("name1");
		sss.setUsername("installer");
		sss.setPassword("hadoop");

		Session session = sss.getSession();

		session.openChannel("shell");

		NewSshClient nclient = new NewSshClient(session);

		nclient.sendShell("pwd");

		nclient.sendShell("cd ~/zhome");

		nclient.sendShell("pwd");

		nclient.disconnect();

	}

	private PipedInputStream fromServer;
	private PipedOutputStream toServer;
	private String previousMode;

	private String lastCommand;

	public boolean isConnected() {
		return (channel != null && channel.isConnected());
	}

	public void disconnect() {
		if (isConnected()) {
			channel.disconnect();
			session.disconnect();
		}
	}

	public String sendShell(String command) throws Exception {
		connect(SHELL_MODE);
		lastCommand = command;
		command += "; echo \"" + TERMINATOR + "\" \n";
		BufferedReader reader = null;
		try {
			toServer.write(command.getBytes());
			// To ensure server answer reception
			// and manage a communication time out.
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("{}", e);
			throw new Exception(e.getLocalizedMessage());
		}
		return getServerResponse();
	}

	private String escape(String subjectString) {
		return alphaNumeric.matcher(subjectString).replaceAll("\\\\$1");
	}

	private String getServerResponse() throws Exception {
		StringBuffer builder = new StringBuffer();
		String result = null;
		try {
			int count = 0;
			String line = "";

			BufferedReader reader = new BufferedReader(new InputStreamReader(fromServer));
			int totalWait = 0;
			while (totalWait < 3000) {
				if (reader.ready()) {
					break;
				}
				Thread.sleep(500);
				totalWait += 500;
			}

			if (reader.ready()) {
				for (int i = 0; true; i++) {
					try {
						line = reader.readLine();
					} catch (IOException e) {
						logger.warn("Communication seems to be closed...");
						break;
					}
					builder.append(line).append("\n");
					if (line.indexOf(TERMINATOR) != -1 && (++count > 1)) {
						break;
					}
				}
				result = builder.toString();
				int beginIndex = result.indexOf(TERMINATOR + "\"") + ((TERMINATOR + "\"").length());
				result = result.replaceAll(escape(TERMINATOR), "").trim();
				logger.debug("Command : " + lastCommand + " -> Result : " + result);
				return result;
			} else {
				throw new Exception("Server did not answer in the time (" + 3000 + "ms) to command (" + lastCommand + ")");
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("{}", e);
			throw new Exception(e.getLocalizedMessage());
		}
	}

	private void connect(String mode) throws Exception {
		if (!isConnected()) {
			try {
				channel = session.openChannel(mode);
				previousMode = mode;
				// If Shell mode, customize I/O streams to get response
				if (mode.equals(SHELL_MODE)) {
					initShellMode();
					channel.connect();
					sendShell("echo ''");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("{}", e);
				throw new Exception(e.getLocalizedMessage());
			}
		} else {
			// To avoid 10 files upload limitation in session
			// if (nbChannel == 9) {
			// disconnect();
			// nbChannel = 0;
			// connect(mode);
			// }
			// Change channel
			if (!previousMode.equals(mode)) {
				previousMode = mode;
				channel.disconnect();
				connect(mode);
			}
			// nbChannel++;
		}
	}

	private void initShellMode() throws IOException {
		PipedOutputStream po = new PipedOutputStream();
		fromServer = new PipedInputStream(po);
		channel.setOutputStream(po);
		toServer = new PipedOutputStream();
		PipedInputStream pi = new PipedInputStream((PipedOutputStream) toServer);
		channel.setInputStream(pi);
	}
}
