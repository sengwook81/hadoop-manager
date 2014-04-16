package org.zero.hadoop.manager.service.app.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.zero.commons.core.support.network.ssh.SecureShellSession;
import org.zero.commons.core.support.network.ssh.SftpClient;
import org.zero.commons.core.support.network.ssh.SshClient;
import org.zero.commons.core.support.progress.ProgressMonitor;
import org.zero.hadoop.manager.service.app.cmp.AppInstallStatus;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;
import org.zero.hadoop.manager.service.base.vo.ServerVo;

@Component("installer.hadoop121")
@Scope("prototype")
public class HadoopInstallProcessor extends AppInstallProcessor {

	private static Logger logger = LoggerFactory.getLogger(HadoopInstallProcessor.class);

	@Override
	public AppInstallStatus doInstall(ServerVo server, final ServerApplicationVo item) throws Exception {
		logger.debug("DO Hadoop Install Start");
		SshClient ssh = new SshClient(getShellSession());

		String commandResult = "";
		String home = ssh.execCommand("pwd");
		commandResult = ssh.execCommand("whoami");

		// When Connect Check Success
		if(commandResult != null && commandResult.equals(server.getUser_Id())) {
			// Check JavaHome
			commandResult = ssh.execCommand("echo $JAVA_HOME");
			if(commandResult.isEmpty()) {
				throw new RuntimeException("JAVA_HOME IS NOT SET");
			}
			// Create Hadoop Directory
		}
		// Upload Hadoop
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		
		Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:" + "**/hadoop*.tar.gz");
		
		if(resources.length > 0) {
			Resource resource = resources[0];
			SftpClient sftpClient = new SftpClient(getShellSession());
			sftpClient.upload("installer_test", resource.getFile(), new ProgressMonitor() {
				int nPrevPercent = 0;
				@Override
				public void progress(int nPercent) {
					if (nPrevPercent != nPercent) {
						System.out.println("Progress : " + nPercent);
						getMonitor().addProcessData(item, "HADOOP SEND", nPercent + "%");
						nPrevPercent = nPercent;
					}
				}

				@Override
				public void finish() {
					getMonitor().addProcessData(item, "HADOOP SEND FINISH", "100%");
				}

				@Override
				public void begin() {
					getMonitor().addProcessData(item, "HADOOP SEND START", "0%");
				}
			});
		}
		logger.debug("Hadoop Installer Whami Command Result[{}]", commandResult);
		
		return new AppInstallStatus(AppInstallStatus.FINISH_INSTALL, "SUCCESS");
	}

}
