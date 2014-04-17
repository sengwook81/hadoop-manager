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

@Component("installer.hadoop121")
@Scope("prototype")
public class HadoopInstallProcessor extends AppInstallProcessor {

	private static Logger logger = LoggerFactory.getLogger(HadoopInstallProcessor.class);

	
	private static final String HADOOP_HOME = APP_HOME + "/hadoop";
	private static final String CONFIG_FILE = ".HADOOP.conf";
	private static final String HADOOP_STORAGE = APP_HOME + "/storage";
	
	@Override
	public AppInstallStatus doInstall(ServerVo server, final ServerApplicationVo item) throws Exception {
		logger.debug("DO Hadoop Install Start");
		String commandResult = "";

		commandResult = getSshClient().sendShell("echo $HADOOP_HOME");
		if (!commandResult.isEmpty()) {
			throw new RuntimeException("Hadoop Home is Already Defined");
		}

		// Upload Hadoop
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

		Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:" + "**/hadoop*.tar.gz");
		String tpPath = UUID.randomUUID().toString();
		if (resources.length > 0) {
			Resource resource = resources[0];
			getSshClient().upload(APP_HOME + "/binary", resource.getFile(), new ProgressMonitor() {
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
			// Tar Decompress
			getSshClient().sendShell("mkdir -p " + APP_HOME + "/temp/" + tpPath);
			commandResult = getSshClient().sendShell(
					"tar -xf " + APP_HOME + "/binary/" + resource.getFile().getName() + " -C " + APP_HOME + "/temp/" + tpPath);
			logger.debug("JDK DECOMPRESS RESULT [{}]", commandResult);
			getMonitor().addProcessData(item, "HADOOP DECOMPRESS", "SUCCESS");
		}
		

		// JDK 경로 획득.
		commandResult = getSshClient().sendShell("ls --color=never -b "+APP_HOME+"/temp/" + tpPath);
		if (commandResult.isEmpty()) {
			getMonitor().addProcessData(item, "Java install Error", "JDK Decompress Path Disappear");
			getSshClient().sendShell("rm -f -r "+APP_HOME+"/temp/" + tpPath);
			throw new RuntimeException("Java install Error  [Path Disappear]");
		}

		// JDK 경로 획득.
		commandResult = getSshClient().sendShell("mv "+APP_HOME+"/temp/" + tpPath + "/" + commandResult + " "  + HADOOP_HOME);
		if (!commandResult.isEmpty()) {
			getMonitor().addProcessData(item, "HADOOP install Error", "HADOOP Path Move Fail");
			getSshClient().sendShell("rm -f -r "+APP_HOME+"/temp/" + tpPath);
			throw new RuntimeException("HADOOP install Error  [HADOOP Path Move Fail]");
		}
		// 임시 디렉토리 삭제.
		commandResult = getSshClient().sendShell("rm -f -r "+APP_HOME+"/temp/" + tpPath);
		getMonitor().addProcessData(item, "HADOOP PATH SETTING", HADOOP_HOME);

		// Home Directory 설정.
		commandResult = getSshClient().sendShell("echo 'export HADOOP_HOME=" + getSshClient().getHome() + "/zhome/hadoop' > " +APP_CONFIG_HOME+ "/" + CONFIG_FILE);
		commandResult = getSshClient().sendShell("echo 'export PATH=$PATH:$HADOOP_HOME/bin' >> " +APP_CONFIG_HOME+ "/" + CONFIG_FILE);
		getMonitor().addProcessData(item, "HADOOP CONFIG SETTING", APP_CONFIG_HOME+ "/" + CONFIG_FILE);

		commandResult = getSshClient().sendShell("source ~/.bashrc");
		commandResult = getSshClient().sendShell("echo $HADOOP_HOME");
		getMonitor().addProcessData(item, "HADOOP_HOME CONFIRM", commandResult);

		// 압축해제 종료.
		
		return new AppInstallStatus(AppInstallStatus.FINISH_INSTALL, "SUCCESS");
	}

	@Override
	public AppInstallStatus doUnInstall(ServerVo server, ServerApplicationVo item) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
