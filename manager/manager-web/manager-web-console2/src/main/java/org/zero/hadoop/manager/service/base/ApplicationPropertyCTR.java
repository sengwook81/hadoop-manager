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
import org.zero.hadoop.manager.service.base.dao.ApplicationPropertyDao;
import org.zero.hadoop.manager.service.base.vo.ApplicationPropertyVo;
import org.zero.hadoop.manager.service.common.SeqManager;
import org.zero.hadoop.manager.util.ListWrap;

@Controller
public class ApplicationPropertyCTR {

	@Autowired
	SeqManager seqManager;
	
	public static final String SEQ_NAME = "ACP";
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationPropertyCTR.class);
	@Autowired
	ApplicationPropertyDao aopplicationPropertyDao;

	@RequestMapping("/application/config/prop/list")
	public Model getApplications(ApplicationPropertyVo param, Model model) {
		logger.debug("TX BEGIN get ApplicationsProperty With {} ", param);
		List<ApplicationPropertyVo> Applications = aopplicationPropertyDao.getApplicationPropertyList(param);

		model.addAttribute("_rslt", Applications);
		logger.debug("TX FINISH get ApplicationsProperty With {} ", model);
		return model;
	}

	@RequestMapping("/application/config/prop/add")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model addApplications(@RequestBody ListWrap<ApplicationPropertyVo> param, Model model) {
		logger.debug("TX BEGIN add ApplicationsProperty With {} ", param);
		for (ApplicationPropertyVo item : param.getList()) {
			if (!StringUtils.isEmpty(item.getApp_Id())) {
				//String seq = seqManager.getSeq(SEQ_NAME);
				//item.setApp_Id(seq);
				aopplicationPropertyDao.insApplicationProperty(item);
			}
			else {
				throw new RuntimeException("Has No Parent Application Config");
			}
		}
		logger.debug("TX FINISH addA pplicationsProperty With {} ", model);
		return model;
	}

	@RequestMapping("/application/config/prop/modify")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model modifyApplications(@RequestBody ListWrap<ApplicationPropertyVo> param, Model model) {
		logger.debug("TX BEGIN modify ApplicationsProperty With {} ", param);
		for (ApplicationPropertyVo item : param.getList()) {
			aopplicationPropertyDao.uptApplicationProperty(item);
		}
		logger.debug("TX FINISH modify ApplicationsProperty With {} ", model);
		return model;
	}

	@RequestMapping("/application/config/prop/delete")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model deleteApplications(@RequestBody ListWrap<ApplicationPropertyVo> param, Model model) {
		logger.debug("TX BEGIN delete ApplicationsProperty With {} ", param);
		for (ApplicationPropertyVo item : param.getList()) {
			aopplicationPropertyDao.delApplicationProperty(item);
		}
		logger.debug("TX FINISH delete ApplicationsProperty With {} ", model);
		return model;
	}

}
