package org.zero.hadoop.manager.service.app.processor;

import java.util.UUID;

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

@Component("installer.java")
@Scope("prototype")
public class JavaInstallProcessor extends AppInstallProcessor {

	private static Logger logger = LoggerFactory.getLogger(JavaInstallProcessor.class);

	@Override
	public AppInstallStatus doInstall(ServerVo server, final ServerApplicationVo item) throws Exception {
		SshClient ssh = new SshClient(getShellSession());

		String commandResult = "";
		String home = ssh.execCommand("pwd");
		commandResult = ssh.execCommand("whoami");
		logger.debug("Hadoop Installer Whami Command Result[{}]", commandResult);
		getMonitor().addProcessData(item, "SERVER CONNECT", "SUCCESS");
		// When Connect Check Success
		if (commandResult != null && commandResult.equals(server.getUser_Id())) {
			// CHECK HOME
			getMonitor().addProcessData(item, "HOME CHECK", "DIRECTORY CHECK");
			commandResult = ssh.execCommand("[ ! -d ~/zhome/conf ] && echo 'YES'");
			if (commandResult.equals("YES")) {
				commandResult = ssh.execCommand("mkdir -p ~/zhome/conf");
				if (!commandResult.isEmpty()) {
					getMonitor().addProcessData(item, "HOME CHECK", "CREATE DIR FAIL");
					throw new RuntimeException("Create Dir Exception[" + commandResult + "]");
				}
			}

			// CHECK JAVA_HOME
			getMonitor().addProcessData(item, "JAVA CHECK", "DIRECTORY CHECK");
			commandResult = ssh.execCommand("[ -d ~/zhome/jdk ] && echo 'YES'");
			if (!commandResult.isEmpty()) {
				getMonitor().addProcessData(item, "JAVA CHECK", "JAVA ALREADY INSTALLED");
				throw new RuntimeException("JAVA ALREADY INSTALLED [" + commandResult + "]");
			}
		}

		// Upload Jdk tar.gz
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:" + "**/jdk*.tar.gz");
		String tpPath = UUID.randomUUID().toString();
		if (resources.length > 0) {
			Resource resource = resources[0];
			SftpClient sftpClient = new SftpClient(getShellSession());
			sftpClient.upload("~/zhome", resource.getFile(), new ProgressMonitor() {

				int nPrevPercent = 0;

				@Override
				public void progress(int nPercent) {
					if (nPrevPercent != nPercent) {
						System.out.println("Progress : " + nPercent);
						getMonitor().addProcessData(item, "JDK SEND", nPercent + "%");
						nPrevPercent = nPercent;
					}
				}

				@Override
				public void finish() {
					getMonitor().addProcessData(item, "JDK SEND FINISH", "100%");
				}

				@Override
				public void begin() {
					getMonitor().addProcessData(item, "JDK SEND START", "0%");
				}
			});
			// Tar Decompress
			ssh.execCommand("mkdir -p ~/zhome/temp/" + tpPath);
			commandResult = ssh.execCommand("tar -xf ~/zhome/" + resource.getFile().getName() + " -C ~/zhome/temp/" + tpPath);
			logger.debug("JDK DECOMPRESS RESULT [{}]", commandResult);
			getMonitor().addProcessData(item, "JAVA DECOMPRESS", "SUCCESS");

			// JDK 경로 획득.
			commandResult = ssh.execCommand("ls -b ~/zhome/temp/" + tpPath);
			if (commandResult.isEmpty()) {
				getMonitor().addProcessData(item, "Java install Error", "JDK Decompress Path Disappear");
				ssh.execCommand("rm -f -r ~/zhome/temp/" + tpPath);
				throw new RuntimeException("Java install Error  [Path Disappear]");
			}

			// JDK 경로 획득.
			commandResult = ssh.execCommand("mv ~/zhome/temp/" + tpPath + "/" + commandResult + " ~/zhome/jdk");
			if (!commandResult.isEmpty()) {
				getMonitor().addProcessData(item, "Java install Error", "JDK Path Move Fail");
				ssh.execCommand("rm -f -r ~/zhome/temp/" + tpPath);
				throw new RuntimeException("Java install Error  [JDK Path Move Fail]");
			}
			// 임시 디렉토리 삭제.
			commandResult = ssh.execCommand("rm -f -r ~/zhome/temp/" + tpPath);
			getMonitor().addProcessData(item, "JDK PATH SETTING", "~/zhome/jdk");

			// Home Directory 설정.
			commandResult = ssh.execCommand("echo 'export JAVA_HOME=" + home + "/zhome/jdk' > ~/zhome/conf/.JAVA_HOME.conf");
			commandResult = ssh.execCommand("echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/zhome/conf/.JAVA_HOME.conf");
			getMonitor().addProcessData(item, "JDK CONFIG SETTING", "~/zhome/conf/.JAVA_HOME.conf");
		} else {
			throw new RuntimeException("Java Binary Not Found");
		}

		return new AppInstallStatus(AppInstallStatus.FINISH_INSTALL, "SUCCESS");
	}

	public static void main(String[] args) {
		ServerVo serverVo = new ServerVo("SV00000017", "SV00000017");
		serverVo.setHost_Info("name1");
		serverVo.setUser_Id("installer");
		serverVo.setUser_Pwd("hadoop");

		ServerApplicationVo serverApplicationVo = new ServerApplicationVo();
		serverApplicationVo.setApp_Id("AA00000017");
		serverApplicationVo.setServer_Id(serverVo.getServer_Id());
		serverApplicationVo.setGroup_Id(serverVo.getGroup_Id());
		serverApplicationVo.setApp_Ext_Opts("install");

		JavaInstallProcessor processor = new JavaInstallProcessor();
		processor.setParam(serverApplicationVo);
		try {
			processor.doInstall(serverVo, serverApplicationVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}