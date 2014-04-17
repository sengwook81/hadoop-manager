package My;

import java.io.IOException;

import org.zero.commons.core.support.network.ssh.SecureShellSession;
import org.zero.commons.core.support.network.ssh.SshClient_Old;

import com.jcraft.jsch.JSchException;

public class Expect4JClient {

	public static void main(String[] args) throws JSchException, IOException {
		SecureShellSession sss = new SecureShellSession(3000);
		sss.setHost("name1");
		sss.setUsername("installer");
		sss.setPassword("hadoop");
		SshClient_Old client = new SshClient_Old(sss);
		
		
		client.execCommand("echo ''");
		
		client.execCommand("cd ~/zhome/conf");
		
		client.execCommand("pwd");
	}
}
