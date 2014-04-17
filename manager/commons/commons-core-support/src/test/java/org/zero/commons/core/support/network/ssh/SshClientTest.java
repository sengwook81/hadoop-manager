package org.zero.commons.core.support.network.ssh;

import java.io.IOException;

import org.junit.Test;
import org.zero.commons.core.support.progress.ProgressMonitor;

import com.jcraft.jsch.JSchException;

public class SshClientTest {

	@Test
	public void sshCommandTest() throws Exception {
		SshClient sc = new SshClient("data3", "installer", "hadoop");
		String result = sc.sendShell("ls --color=never -b ~/zhome/temp/bc3885cb-9176-437c-af77-e132b2e1587a");
		System.out.println("[" + result + "]");
	}

	// @Test
	public void tarDeCompressTest() throws Exception {
		SshClient sc = new SshClient("name1", "installer", "hadoop");
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
		// SshClient sshClient = new SshClient();
	}

}
