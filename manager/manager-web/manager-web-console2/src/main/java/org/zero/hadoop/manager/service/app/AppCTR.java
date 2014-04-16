package org.zero.hadoop.manager.service.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.zero.hadoop.manager.service.app.dao.ServerApplicationDao;
import org.zero.hadoop.manager.service.app.vo.HadoopVo;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;
import org.zero.hadoop.manager.util.ListWrap;

public abstract class AppCTR<T extends ServerApplicationVo> {

	private static Logger logger = LoggerFactory.getLogger(AppCTR.class);
	@Autowired
	protected ServerApplicationDao serverApplicationDao;
	
	public Model getApplications(T param, Model model) {
		logger.debug("TX BEGIN get ServerApplication With {} ", param);
		List<ServerApplicationVo> Applications = serverApplicationDao.getServerApplications(param);

		model.addAttribute("_rslt", Applications);
		logger.debug("TX FINISH get ServerApplication With {} ", model);
		return model;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Model addApplications(@RequestBody ListWrap<T> param, Model model) {
		logger.debug("TX BEGIN add ServerApplication With {} ", param);
		for (T item : param.getList()) {
			if (!StringUtils.isEmpty(item.getServer_Id()) ||
				!StringUtils.isEmpty(item.getGroup_Id()) ||
				!StringUtils.isEmpty(item.getApp_Id())) {
				serverApplicationDao.insServerApplication(item);
			}
			else {
				throw new RuntimeException("Has No Parent Application Config");
			}
		}
		logger.debug("TX FINISH add ServerApplication With {} ", model);
		return model;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Model updateApplications(@RequestBody ListWrap<T> param, Model model) {
		logger.debug("TX BEGIN add ServerApplication With {} ", param);
		for (T item : param.getList()) {
			if (!StringUtils.isEmpty(item.getServer_Id()) ||
				!StringUtils.isEmpty(item.getGroup_Id()) ||
				!StringUtils.isEmpty(item.getApp_Id())) {
				
				if(item.isRow_Flag() == false) {
					if(item.getApp_Ext_Opts() == null || item.getApp_Ext_Opts().isEmpty()) {
					}
					else {
						serverApplicationDao.insServerApplication(item);
					}
				}
				else {
					if(item.getApp_Ext_Opts() == null || item.getApp_Ext_Opts().isEmpty()) {
						serverApplicationDao.delServerApplication(item);
					}
					else {
						serverApplicationDao.uptServerApplication(item);
					}
				}
			}
			else {
				throw new RuntimeException("Has No Parent Application Config");
			}
		}
		logger.debug("TX FINISH add ServerApplication With {} ", model);
		return model;
	}
}
