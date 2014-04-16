package org.zero.hadoop.manager.service.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zero.hadoop.manager.service.base.dao.ApplicationConfigDao;
import org.zero.hadoop.manager.service.base.vo.ApplicationConfigVo;

public class ApplicationConfigManagerCTR {
public static final String APPCFG_SEQ_NAME = "AC";
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationConfigManagerCTR.class);
	@Autowired
	ApplicationConfigDao applicationConfigDao;

	@RequestMapping("/application/config/list")
	public Model getApplications(ApplicationConfigVo param, Model model) {
		logger.debug("TX BEGIN getApplications With {} ", param);
		List<ApplicationConfigVo> Applications = applicationConfigDao.getApplicationConfig(param);

		model.addAttribute("_rslt", Applications);
		logger.debug("TX FINISH getApplications With {} ", Applications);
		return model;
	}
}
