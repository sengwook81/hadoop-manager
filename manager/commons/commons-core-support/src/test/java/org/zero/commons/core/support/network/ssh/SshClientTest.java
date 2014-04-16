package org.zero.commons.core.support.network.ssh;

import java.io.IOException;

import org.junit.Test;
import org.zero.commons.core.support.progress.ProgressMonitor;

import com.jcraft.jsch.JSchException;

public class SshClientTest {

	@Test
	public void tarDeCompressTest() {
		SecureShellSession ssh = new SecureShellSession(3000);
		ssh.setHost("10.10.75.131");
		ssh.setUsername("dooby");
		ssh.setPassword("hadoop");
		SftpClient sc = new SftpClient(ssh);
		try {

			sc.upload("installer_test", "/Users/appszero/Downloads/hadoop-1.2.1 (1).tar.gz", new ProgressMonitor() {

				@Override
				public void progress(int nPercent) {
					System.out.println("Progress : " + nPercent);
				}

				@Override
				public void finish() {
					System.out.println("Progress : " + 100);

				}

				@Override
				public void begin() {
					// TODO Auto-generated method stub

				}
			});
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//SshClient sshClient = new SshClient();
	}

}
