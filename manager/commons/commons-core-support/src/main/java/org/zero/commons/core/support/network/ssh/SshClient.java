package org.zero.commons.core.support.network.ssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zero.commons.core.support.progress.ProgressMonitor;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshClient {

	protected Logger logger = LoggerFactory.getLogger(SshClient.class);

	public static final String SHELL_MODE = "shell";
	public static final String EXEC_MODE = "exec";
	public static final String SFTP_MODE = "sftp";

	private Pattern alphaNumeric = Pattern.compile("([^a-zA-z0-9])");

	private static final String TERMINATOR = "--ZERO-LINE--";

	private Channel channel;
	private Session session;

	private SFtpClient fileClient;

	private PipedInputStream fromServer;
	private PipedOutputStream toServer;
	private String previousMode;

	private String lastCommand;

	private Session lastSession = null;

	private String host;

	private String user;

	private String pwd;

	private int port = 22;

	private int timeout = 3000;

	private String home;

	public SshClient(String host, String user, String pwd) throws Exception {
		this(host, user, pwd, 22, 3000);
	}

	public SshClient(String host, String user, String pwd, int port) throws Exception {
		this(host, user, pwd, port, 3000);
	}

	public SshClient(String host, String user, String pwd, int port, int timeout) throws Exception {
		this.host = host;
		this.user = user;
		this.pwd = pwd;
		this.port = port;
		this.timeout = timeout;
		initSession();
	}

	protected void initSession() throws Exception {

		if (host == null || user == null) {
			throw new RuntimeException("Host or UserName is Empty");
		}
		JSch jsch = new JSch();

		session = jsch.getSession(user, host, port);
		// 3. 패스워드를 설정한다.
		session.setPassword(pwd);

		java.util.Properties config = new java.util.Properties();
		// 4-1. 호스트 정보를 검사하지 않는다.
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		logger.debug("Ssh Try To Connect {}", host);
		// SingleSession 일경우.
		session.setServerAliveInterval(30);
		session.setServerAliveCountMax(Integer.MAX_VALUE);
		// 5. 접속한다.
		session.connect(timeout);
		lastSession = session;
		logger.debug("Ssh Connect Success {}", host);
		sendShell("echo ''");
		home = sendShell("pwd");
		fileClient = new SFtpClient(session);
	}

	public void reconnect() throws Exception {
		disconnect();
		initSession();
	}

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
			logger.debug("Send Command [{}]",command);
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
			while (totalWait < timeout) {
				if (reader.ready()) {
					break;
				}
				logger.trace("Wait response {} --> {} " , totalWait , timeout);
				Thread.sleep(timeout / 10);
				totalWait += timeout / 10;
			}

			int linePos = 0;
			if (reader.ready()) {
				logger.trace("Response Ready");
				for (int i = 0; true; i++) {
					try {
						line = reader.readLine();
					} catch (IOException e) {
						logger.warn("Communication seems to be closed...");
						break;
					}
					
					if (line.indexOf(TERMINATOR) != -1 && (++count > 1)){
						break;
					}
					else if(line.indexOf(TERMINATOR) != -1)  {
						
					}
					else {
						System.out.println((linePos++) + "   " + line);
						if(builder.length() > 0) {
							builder.append("\n").append(line);
						}
						else {
							builder.append(line);	
						}
						
					}
				}
				result = builder.toString();
				//int beginIndex = result.indexOf(TERMINATOR + "\"") + ((TERMINATOR + "\"\n").length());
				//result = result.substring(beginIndex);// replaceAll(escape(TERMINATOR), "").trim();
				//System.out.println(beginIndex);
				logger.trace("Command : " + lastCommand + " -> Result : " + result);
				return result;
			} else {
				throw new Exception("Server did not answer in the time (" + timeout + "ms) to command (" + lastCommand + ")");
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
				ChannelShell channelShell = (ChannelShell) channel;
				channelShell.setPty(true);
				channelShell.setPtySize(1024, 128,1024, 1024);
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

	public void upload(String path, String f, ProgressMonitor monitor) throws JSchException, IOException {
		fileClient.upload(path, f, monitor);
	}

	public void upload(String path, File f, ProgressMonitor monitor) throws JSchException, IOException {
		fileClient.upload(path, f, monitor);
	}

	public String getHome() {
		return home;
	}
}
