package org.zero.hadoop.manager.service.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zero.hadoop.manager.service.app.cmp.AppInstallStatus;
import org.zero.hadoop.manager.service.app.cmp.InstallerPool;
import org.zero.hadoop.manager.service.app.dao.InstallerDao;
import org.zero.hadoop.manager.service.app.dao.ServerApplicationDao;
import org.zero.hadoop.manager.service.app.processor.AppInstallProcessor;
import org.zero.hadoop.manager.service.app.processor.ProcessMonitor;
import org.zero.hadoop.manager.service.app.vo.InstallerVo;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;
import org.zero.hadoop.manager.service.base.dao.ApplicationDao;
import org.zero.hadoop.manager.service.base.vo.ApplicationVo;
import org.zero.hadoop.manager.util.ListWrap;

@Controller
public class InstallerCTR implements ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(InstallerCTR.class);

	@Autowired
	ProcessMonitor monitor;

	@Autowired
	InstallerPool installerPool;

	@Autowired
	ApplicationDao applicationDao;

	@Autowired
	InstallerDao installerDao;

	@Autowired
	ServerApplicationDao serverApplicationDao;
	private ApplicationContext applicationContext;

	@RequestMapping("/application/installer/applist")
	public Model getApplications(ApplicationVo param, Model model) {
		logger.debug("TX BEGIN get Installer Apps With {} ", param);
		List<ApplicationVo> Applications = applicationDao.getApplicationList(param);

		model.addAttribute("_rslt", Applications);
		logger.debug("TX FINISH get Installer Apps With {} ", model);
		return model;
	}

	@RequestMapping("/application/installer/list")
	public Model getApplicationServers(InstallerVo param, Model model) {
		logger.debug("TX BEGIN get Installer Apps With {} ", param);
		List<InstallerVo> appServers = installerDao.getAppServers(param);

		model.addAttribute("_rslt", appServers);
		logger.debug("TX FINISH get Installer Apps With {} ", model);
		return model;
	}

	@RequestMapping("/application/installer/monitor")
	public Model monitorProgress(Model model) {
		model.addAttribute("_rslt", monitor.getProcessData());
		return model;
	}

	@RequestMapping("/application/installer/install")
	public Model installApplication(@RequestBody ListWrap<InstallerVo> params, Model model) {
		logger.debug("TX BEGIN get Installer Apps With {} ", params);

		String failItems = "";
		for (InstallerVo param : params.getList()) {
			if (param.isInstall_Chk()) {
				ServerApplicationVo appServer = serverApplicationDao.getServerApplication(param);
				logger.debug("Install Logic Check {} ", appServer);
				if (appServer != null && !appServer.getInstall_Flag().equals("F")) {

					if (appServer.getInstall_Flag().equals("P")) {
						failItems += param.getServer_Name();
						continue;
						// throw new RuntimeException(param.getServer_Name() +
						// " 설치 진행중");
					}

					// SetProcess Status
					appServer.setInstall_Flag(AppInstallStatus.NOW_PROCESSING);
					appServer.setInstall_Status("START");
					serverApplicationDao.uptServerApplication(appServer);

					// Application Installer Bean Info Select
					ApplicationVo application = applicationDao.getApplication(new ApplicationVo(appServer.getApp_Id()));

					// getApplicationInstallProcessor Bean
					AppInstallProcessor processorBean = applicationContext.getBean(application.getApp_Processor_Name(), AppInstallProcessor.class);
					processorBean.setInstallFlag(true);
					processorBean.setParam(appServer);
					// Do Install Start
					installerPool.excuteJob(processorBean);
					// getInstall Processor Bean
				}
			}
		}

		if (!failItems.isEmpty()) {
			failItems += " 설치 진행중";
			model.addAttribute("_rslt", failItems);
		} else {
			model.addAttribute("_rslt", "SUCCESS");
		}
		logger.debug("TX FINISH get Installer Apps With {} ", model);
		return model;
	}

	@RequestMapping("/application/installer/uninstall")
	public Model unInstallApplication(@RequestBody InstallerVo param, Model model) {
		logger.debug("TX BEGIN get UnInstaller Apps With {} ", param);

		String failItems = "";
		ServerApplicationVo appServer = serverApplicationDao.getServerApplication(param);
		logger.debug("UnInstall Logic Check {} ", appServer);
		if (appServer != null && appServer.getInstall_Flag().equals("F")) {
			// SetProcess Status
			serverApplicationDao.uptServerApplication(appServer);

			// Application Installer Bean Info Select
			ApplicationVo application = applicationDao.getApplication(new ApplicationVo(appServer.getApp_Id()));

			// getApplicationInstallProcessor Bean
			AppInstallProcessor processorBean = applicationContext.getBean(application.getApp_Processor_Name(), AppInstallProcessor.class);

			processorBean.setInstallFlag(false);
			processorBean.setParam(appServer);
			// Do UnInstall Start
			installerPool.excuteJob(processorBean);
			// getInstall Processor Bean
		}

			model.addAttribute("_rslt", "SUCCESS");
		logger.debug("TX FINISH get UnInstaller Apps With {} ", model);
		return model;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
