package org.zero.commons.core.support.network.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshClient {

	protected static final Logger log = LoggerFactory.getLogger(SshClient.class);
	
	SecureShellSession sshSession = null;

	public SshClient(SecureShellSession session) {
		this.sshSession = session;
	}

	public String execCommand(String command) throws JSchException, IOException {
		return execCommand(new String[] { command }).get(0);
	}

	public List<String> execCommand(String[] commands) throws JSchException, IOException {
		List<String> retData = new ArrayList<String>();
		Session session = sshSession.getSession();
		for (String command : commands) {
			if (!session.isConnected()) {
				throw new JSchException("Session Is Not Connected");
			}
			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
			channelExec.setPty(true);

			if (log.isDebugEnabled()) {
				log.debug("{} Excute Command : {} ", sshSession.getHost(), command);
			}

			channelExec.setCommand(command);

			InputStream err = channelExec.getErrStream();
			channelExec.connect(sshSession.getTimeout());

			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
			String line = null;
			int idx = 0;
			while ((line = br.readLine()) != null) {
				if (idx > 0) {
					sb.append("\n");
				}
				sb.append(line);
				idx++;
			}
			retData.add(sb.toString());

			if (log.isDebugEnabled()) {
				log.debug("{} \nExcute Command : {}\n result : {}\n err :{}", new Object[] { sshSession.getHost(), command, sb.toString(), err });
			}

		}
		return retData;
	}

	public static void main(String[] args) {
		
		SecureShellSession ssh = new SecureShellSession(3000);
		ssh.setHost("10.10.75.131");
		ssh.setUsername("dooby");
		ssh.setPassword("hadoop");
		SshClient sc = new SshClient(ssh);
		
		try {
			String execCommand = sc.execCommand("whoami");
			System.out.println("[" + execCommand + "]");
			execCommand = sc.execCommand("cat /etc/hosts");
			System.out.println("[" + execCommand + "]");
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuffer sb = new StringBuffer();
		System.out.println("[" + sb.toString() + "]");
		
		ssh.distory();
	}

}
