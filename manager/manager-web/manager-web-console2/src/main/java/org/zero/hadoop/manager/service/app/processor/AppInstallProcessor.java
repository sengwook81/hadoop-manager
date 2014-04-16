package org.zero.hadoop.manager.service.app.processor;

import java.util.concurrent.Callable;

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

public abstract class AppInstallProcessor implements Callable<AppInstallProcessor> {

	public static String MARKER = "#HINSTALLER_MARKER";
	
	private static Logger logger = LoggerFactory.getLogger(AppInstallProcessor.class);

	AppInstallStatus retStatus = null;

	private SecureShellSession shellSession = new SecureShellSession(3000);

	@Autowired
	private ProcessMonitor monitor;

	@Autowired
	ServerDao serverDao;

	@Autowired
	ServerApplicationDao serverApplicationDao;

	private ServerApplicationVo param;

	@Override
	public AppInstallProcessor call() throws Exception {
		ServerVo server = serverDao.getServer(new ServerVo(param.getServer_Id(), param.getGroup_Id()));
		
		
		if (server == null) {
			retStatus = new AppInstallStatus(AppInstallStatus.ERROR_OCCUR, "Unknown Server " + param.toString());
		} else {
			try { 
				
				shellSession.setHost(server.getHost_Info());
				shellSession.setPort(22);
				shellSession.setUsername(server.getUser_Id());
				shellSession.setPassword(server.getUser_Pwd());
				
				SshClient client = new SshClient(getShellSession());
				logger.debug("Check Default Bash Marker");
				String marker = client.execCommand("cat ~/.bashrc | grep '" + MARKER +"'");
				if(marker.indexOf(MARKER) < 0) {
					// Register Marker
					logger.debug("REGISTER MARKER LOGIC TO BASHRC");
					client.execCommand("echo 'FILES=~/zhome/conf/.*.conf\n"+ 
										"for f in $FILES\n" +
										"do\n" +
										"  source $f\n" +
										"  echo take action on each file. $f store current file name\n" +
										"done' >> ~/.bashrc");
				}
				
				retStatus = doInstall(server,param);
			}catch(Exception e) {
				retStatus = new AppInstallStatus(AppInstallStatus.ERROR_OCCUR, e.getMessage());
			}
		}

		param.setInstall_Flag(retStatus.getFlag());
		param.setInstall_Status(retStatus.getStatus());
		
		serverApplicationDao.uptServerApplication(param);
		shellSession.distory();
		return this;
	}

	public abstract AppInstallStatus doInstall(ServerVo server, ServerApplicationVo item) throws Exception;

	public ServerApplicationVo getParam() {
		return param;
	}

	public void setParam(ServerApplicationVo param) {
		this.param = param;
	}

	public ProcessMonitor getMonitor() {
		return monitor;
	}

	SecureShellSession getShellSession() {
		return shellSession;
	}
}
