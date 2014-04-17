package org.zero.hadoop.manager.service.app.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zero.commons.core.support.network.ssh.SecureShellSession;
import org.zero.commons.core.support.network.ssh.SshClient;
import org.zero.hadoop.manager.service.app.cmp.AppInstallStatus;
import org.zero.hadoop.manager.service.app.dao.ServerApplicationDao;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;
import org.zero.hadoop.manager.service.base.dao.ServerDao;
import org.zero.hadoop.manager.service.base.vo.ServerVo;

public abstract class AppInstallProcessor implements Runnable {

	protected static final String APP_HOME = "~/zhome";
	protected static final String APP_CONFIG_HOME = "~/zhome/conf";
	public static String MARKER = "#HINSTALLER_MARKER";

	private static Logger logger = LoggerFactory.getLogger(AppInstallProcessor.class);

	private boolean installFlag = true; // TRUE INSTALL , FALSE UNINSTALL

	AppInstallStatus retStatus = null;

	private ServerApplicationVo param;

	private String homePath;

	@Autowired
	private ProcessMonitor monitor;

	@Autowired
	private ServerDao serverDao;

	@Autowired
	private ServerApplicationDao serverApplicationDao;

	private SshClient sshClient = null;

	public abstract AppInstallStatus doInstall(ServerVo server, ServerApplicationVo item) throws Exception;

	public abstract AppInstallStatus doUnInstall(ServerVo server, ServerApplicationVo item) throws Exception;

	public ServerApplicationVo getParam() {
		return param;
	}

	public void setParam(ServerApplicationVo param) {
		this.param = param;
	}

	public ProcessMonitor getMonitor() {
		return monitor;
	}

	@Override
	public void run() {
		ServerVo server = serverDao.getServer(new ServerVo(param.getServer_Id(), param.getGroup_Id()));

		if (server == null) {
			retStatus = new AppInstallStatus(AppInstallStatus.ERROR_OCCUR, "Unknown Server " + param.toString());
		} else {
			try {

				sshClient = new SshClient(server.getHost_Info(), server.getUser_Id(), server.getUser_Pwd());
				logger.debug("Check Default Bash Marker");
				String marker = sshClient.sendShell("cat ~/.bashrc | grep '" + MARKER + "'");
				if (marker.indexOf(MARKER) < 0) {
					// Register Marker
					logger.debug("REGISTER MARKER LOGIC TO BASHRC");
					sshClient.sendShell("echo '" + MARKER + "\n" + "\n" + "if [ ! -d ~/zhome/conf ]; then\n" + "    mkdir -p ~/zhome/conf\n" + "fi\n"
							+ "\n" + "if [ \"$(ls -A ~/zhome/conf)\" ]; then\n" + "  # User specific aliases and functions\n"
							+ "  FILES=~/zhome/conf/.*.conf\n" + "  for f in $FILES\n" + "  do\n" + "    source $f\n" + "  done\n" + "fi \n"
							+ "' >> ~/.bashrc");
				}

				String commandResult = "";
				commandResult = sshClient.sendShell("whoami");
				logger.debug("Hadoop Installer Whami Command Result[{}]", commandResult);
				// When Connect Check Success
				if (!(commandResult != null && commandResult.equals(server.getUser_Id()))) {
					throw new RuntimeException("Server Connect Check Account Fail[" + commandResult + "]");
				}
				getMonitor().addProcessData(param, "SERVER CONNECT", "SUCCESS");

				if (installFlag) {
					retStatus = doInstall(server, param);
				} else {
					retStatus = doUnInstall(server, param);
				}

			} catch (Exception e) {
				retStatus = new AppInstallStatus(AppInstallStatus.ERROR_OCCUR, e.getMessage());
			}
		}

		param.setInstall_Flag(retStatus.getFlag());
		param.setInstall_Status(retStatus.getStatus());

		serverApplicationDao.uptServerApplication(param);
		if (sshClient != null) {
			sshClient.disconnect();
		}
	}

	public boolean isInstallFlag() {
		return installFlag;
	}

	public void setInstallFlag(boolean installFlag) {
		this.installFlag = installFlag;
	}

	SshClient getSshClient() {
		return sshClient;
	}
}
