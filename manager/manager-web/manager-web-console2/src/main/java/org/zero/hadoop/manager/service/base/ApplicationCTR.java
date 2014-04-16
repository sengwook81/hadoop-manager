package org.zero.hadoop.manager.service.base;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zero.hadoop.manager.service.base.dao.ApplicationConfigDao;
import org.zero.hadoop.manager.service.base.dao.ApplicationDao;
import org.zero.hadoop.manager.service.base.vo.ApplicationConfigVo;
import org.zero.hadoop.manager.service.base.vo.ApplicationSVo;
import org.zero.hadoop.manager.service.base.vo.ApplicationVo;
import org.zero.hadoop.manager.service.common.SeqManager;
import org.zero.hadoop.manager.util.ListWrap;

@Controller
public class ApplicationCTR {

	@Autowired
	SeqManager seqManager;
	public static final String APP_SEQ_NAME = "AA";
	public static final String APPCFG_SEQ_NAME = "AC";
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationCTR.class);
	@Autowired
	ApplicationDao applicationDao;

	@Autowired
	ApplicationConfigDao aopplicationConfigDao;
	
	@RequestMapping("/application/list")
	public Model getApplications(ApplicationSVo param, Model model) {
		logger.debug("TX BEGIN getApplications With {} ", param);
		List<ApplicationSVo> Applications = applicationDao.getApplicationConfigList(param);

		model.addAttribute("_rslt", Applications);
		logger.debug("TX FINISH getApplications With {} ", Applications);
		return model;
	}
	
	@RequestMapping("/application/config/list")
	public Model getApplications(ApplicationConfigVo param, Model model) {
		logger.debug("TX BEGIN getApplications With {} ", param);
		List<ApplicationConfigVo> Applications = aopplicationConfigDao.getApplicationConfig(param);

		model.addAttribute("_rslt", Applications);
		logger.debug("TX FINISH getApplications With {} ", model);
		return model;
	}

	@RequestMapping("/application/add")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model addApplications(@RequestBody ListWrap<ApplicationSVo> param, Model model) {
		logger.debug("TX BEGIN addApplications With {} ", param);
		for (ApplicationSVo item : param.getList()) {
			if (StringUtils.isEmpty(item.getApp_Id())) {
				String seq = seqManager.getSeq(APP_SEQ_NAME);
				item.setApp_Id(seq);
				applicationDao.insApplication(item);
				updateApplicationConfigChild(item);
			}
		}
		logger.debug("TX FINISH addApplications With {} ", model);
		return model;
	}

	@RequestMapping("/application/modify")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model modifyApplications(@RequestBody ListWrap<ApplicationSVo> param, Model model) {
		logger.debug("TX BEGIN modifyApplications With {} ", param);
		for (ApplicationSVo item : param.getList()) {
			if(item.getDel_Yn()) {
				deleteApplicationConfigChild(item);
				applicationDao.delApplication(item);
			}
			else {
				applicationDao.uptApplication(item);
				updateApplicationConfigChild(item);
			}
		}
		logger.debug("TX FINISH modifyApplications With {} ", model);
		return model;
	}
	
	@Autowired
	ApplicationConfigDao applicationConfigDao;
	
	private void deleteApplicationConfigChild(ApplicationSVo parent) {
		if(parent.getConfigs() != null ) {
			for (ApplicationConfigVo item : parent.getConfigs()) {
				applicationConfigDao.delApplicationConfig(item);
			}
		}
	}
	private void updateApplicationConfigChild(ApplicationSVo parent) {
		if(parent.getConfigs() != null ) {
			for (ApplicationConfigVo item : parent.getConfigs()) {
				
				if(item.getDel_Yn()) {
					applicationConfigDao.delApplicationConfig(item);
				}
				else if (StringUtils.isEmpty(item.getConfig_Id())) {
					String seq = seqManager.getSeq(APPCFG_SEQ_NAME);
					item.setApp_Id(parent.getApp_Id());
					item.setConfig_Id(seq);
					applicationConfigDao.insApplicationConfig(item);
				}
				else
				{
					applicationConfigDao.uptApplicationConfig(item);
				}
			}
		}
	}

	@RequestMapping("/application/delete")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model deleteApplications(@RequestBody ListWrap<ApplicationSVo> param, Model model) {
		logger.debug("TX BEGIN deleteApplications With {} ", param);
		for (ApplicationVo item : param.getList()) {
			applicationDao.delApplication(item);
		}
		logger.debug("TX FINISH deleteApplications With {} ", model);
		return model;
	}

}
