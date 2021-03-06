package org.zero.hadoop.manager.service.app.processor;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.zero.commons.core.support.progress.ProgressMonitor;
import org.zero.hadoop.manager.service.app.cmp.AppInstallStatus;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;
import org.zero.hadoop.manager.service.base.vo.ServerVo;

@Component("installer.java")
@Scope("prototype")
public class JavaInstallProcessor extends AppInstallProcessor {

	private static final String JAVA_HOME = APP_HOME + "/jdk";
	private static final String CONFIG_FILE = ".JAVA_HOME.conf";
	
	private static Logger logger = LoggerFactory.getLogger(JavaInstallProcessor.class);

	@Override
	public AppInstallStatus doInstall(ServerVo server, final ServerApplicationVo item) throws Exception {

		String commandResult = "";
		// When Connect Check Success
		// CHECK HOME
		getMonitor().addProcessData(item, "HOME CHECK", "DIRECTORY CHECK");
		commandResult = getSshClient().sendShell("[ ! -d "  + APP_CONFIG_HOME+" ] && echo 'YES'");
		if (commandResult.equals("YES")) {
			commandResult = getSshClient().sendShell("mkdir -p " + APP_CONFIG_HOME);
			if (!commandResult.isEmpty()) {
				getMonitor().addProcessData(item, "HOME CHECK", "CREATE DIR FAIL");
				throw new RuntimeException("Create Dir Exception[" + commandResult + "]");
			}
		}

		// CHECK JAVA_HOME
		getMonitor().addProcessData(item, "JAVA CHECK", "DIRECTORY CHECK");
		commandResult = getSshClient().sendShell("[ -d "+ JAVA_HOME +" ] && echo 'YES'");
		if (!commandResult.isEmpty()) {
			getMonitor().addProcessData(item, "JAVA CHECK", "JAVA ALREADY INSTALLED");
			throw new RuntimeException("JAVA ALREADY INSTALLED [" + commandResult + "]");
		}

		// Upload Jdk tar.gz
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:" + "**/jdk*.tar.gz");
		String tpPath = UUID.randomUUID().toString();
		if (resources.length > 0) {
			Resource resource = resources[0];
			getSshClient().upload(APP_HOME +"/binary", resource.getFile(), new ProgressMonitor() {

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
			getSshClient().sendShell("mkdir -p "+APP_HOME+"/temp/" + tpPath);
			commandResult = getSshClient().sendShell("tar -xf " + APP_HOME +"/binary/" + resource.getFile().getName() + " -C "+APP_HOME+"/temp/" + tpPath);
			logger.debug("JDK DECOMPRESS RESULT [{}]", commandResult);
			getMonitor().addProcessData(item, "JAVA DECOMPRESS", "SUCCESS");

			// JDK 경로 획득.
			commandResult = getSshClient().sendShell("ls --color=never -b "+APP_HOME+"/temp/" + tpPath);
			if (commandResult.isEmpty()) {
				getMonitor().addProcessData(item, "Java install Error", "JDK Decompress Path Disappear");
				getSshClient().sendShell("rm -f -r "+APP_HOME+"/temp/" + tpPath);
				throw new RuntimeException("Java install Error  [Path Disappear]");
			}

			// JDK 경로 획득.
			commandResult = getSshClient().sendShell("mv "+APP_HOME+"/temp/" + tpPath + "/" + commandResult + " "  + JAVA_HOME);
			if (!commandResult.isEmpty()) {
				getMonitor().addProcessData(item, "Java install Error", "JDK Path Move Fail");
				getSshClient().sendShell("rm -f -r "+APP_HOME+"/temp/" + tpPath);
				throw new RuntimeException("Java install Error  [JDK Path Move Fail]");
			}
			// 임시 디렉토리 삭제.
			commandResult = getSshClient().sendShell("rm -f -r "+APP_HOME+"/temp/" + tpPath);
			getMonitor().addProcessData(item, "JDK PATH SETTING", JAVA_HOME);

			// Home Directory 설정.
			commandResult = getSshClient().sendShell("echo 'export JAVA_HOME=" + getSshClient().getHome() + "/zhome/jdk' > " +APP_CONFIG_HOME+ "/" + CONFIG_FILE);
			commandResult = getSshClient().sendShell("echo 'export PATH=$PATH:$JAVA_HOME/bin' >> " +APP_CONFIG_HOME+ "/" + CONFIG_FILE);
			getMonitor().addProcessData(item, "JDK CONFIG SETTING", APP_CONFIG_HOME+ "/" + CONFIG_FILE);

			commandResult = getSshClient().sendShell("source ~/.bashrc");
			commandResult = getSshClient().sendShell("echo $JAVA_HOME");
			getMonitor().addProcessData(item, "JAVA_HOME CONFIRM", commandResult);

		} else {
			throw new RuntimeException("Java Binary Not Found");
		}

		return new AppInstallStatus(AppInstallStatus.FINISH_INSTALL, "SUCCESS");
	}

	@Override
	public AppInstallStatus doUnInstall(ServerVo server, ServerApplicationVo item) throws Exception {
		String commandResult = "";
		commandResult = getSshClient().sendShell("echo $JAVA_HOME");
		if(!commandResult.isEmpty()) {
			getSshClient().sendShell("rm -f " + APP_CONFIG_HOME + "/" + CONFIG_FILE);
			getSshClient().sendShell("rm -f -r " + JAVA_HOME);
		}
		return new AppInstallStatus(AppInstallStatus.FINISH_UNINSTALL, "SUCCESS");
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
			e.printStackTrace();
		}
	}

}