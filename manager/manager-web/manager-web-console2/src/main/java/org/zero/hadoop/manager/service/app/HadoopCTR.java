package org.zero.hadoop.manager.service.app;

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
import org.zero.hadoop.manager.service.app.dao.HadoopDao;
import org.zero.hadoop.manager.service.app.vo.HadoopVo;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;
import org.zero.hadoop.manager.util.ListWrap;

@Controller
public class HadoopCTR extends AppCTR<HadoopVo>{

	private static Logger logger = LoggerFactory.getLogger(HadoopCTR.class);
	@Autowired
	HadoopDao hadoopDao; 
	
	@RequestMapping("/application/hadoop/list")
	public Model getApplications(HadoopVo param, Model model) {
		logger.debug("TX BEGIN get HadoopApplication With {} ", param);
		List<HadoopVo> Applications = hadoopDao.getServerApplications(param);

		model.addAttribute("_rslt", Applications);
		logger.debug("TX FINISH get HadoopApplication With {} ", model);
		return model;
	}

	@RequestMapping("/application/hadoop/add")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model addApplications(@RequestBody ListWrap<HadoopVo> param, Model model) {
		logger.debug("TX BEGIN add HadoopApplication With {} ", param);
		for (ServerApplicationVo item : param.getList()) {
			if (!StringUtils.isEmpty(item.getServer_Id()) ||
				!StringUtils.isEmpty(item.getGroup_Id()) ||
				!StringUtils.isEmpty(item.getApp_Id())) {
				serverApplicationDao.insServerApplication(item);
			}
			else {
				throw new RuntimeException("Has No Parent Application Config");
			}
		}
		logger.debug("TX FINISH add HadoopApplication With {} ", model);
		return model;
	}
	
	@RequestMapping("/application/hadoop/modify")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model updateApplications(@RequestBody ListWrap<HadoopVo> param, Model model) {
		logger.debug("TX BEGIN add HadoopApplication With {} ", param);
		for (HadoopVo item : param.getList()) {
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
		logger.debug("TX FINISH add HadoopApplication With {} ", model);
		return model;
	}
}
