package org.zero.hadoop.manager.service.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zero.hadoop.manager.service.app.dao.ApplicationPropertyExtDao;
import org.zero.hadoop.manager.service.app.vo.ApplicationConfigPropertyExtVo;
import org.zero.hadoop.manager.util.ListWrap;

@Controller
public class AppConfigCTR {

	private static Logger logger = LoggerFactory.getLogger(AppConfigCTR.class);

	@Autowired
	ApplicationPropertyExtDao applicationPropertyExtDao;

	@RequestMapping("/application/config/ext/list")
	public Model getApplicationsConfigExt(ApplicationConfigPropertyExtVo param, Model model) {
		logger.debug("TX BEGIN get ApplicationsConfigExt With {} ", param);
		List<ApplicationConfigPropertyExtVo> applicationPropertyExt = applicationPropertyExtDao.getApplicationPropertyExt(param);

		model.addAttribute("_rslt", applicationPropertyExt);

		logger.debug("TX BEGIN get ApplicationsConfigExt With {} ", param);

		return model;
	}

	@RequestMapping("/application/config/ext/add")
	public Model addApplicationsConfigExt(@RequestBody ListWrap<ApplicationConfigPropertyExtVo> params, Model model) {
		logger.debug("TX BEGIN get ApplicationsConfigExt With {} ", params);

		for (ApplicationConfigPropertyExtVo param : params.getList()) {
			if (param.getApp_Id() != null && !param.getApp_Id().isEmpty() && param.getConfig_Id() != null && !param.getConfig_Id().isEmpty()
					&& param.getProp_Key() != null && !param.getProp_Key().isEmpty()) {
				applicationPropertyExtDao.insApplicationPropertyExt(param);
			}
		}
		model.addAttribute("_rslt", "SUCCESS");

		logger.debug("TX BEGIN get ApplicationsConfigExt With {} ", params);

		return model;
	}

	@RequestMapping("/application/config/ext/modify")
	public Model uptApplicationsConfigExt(@RequestBody ListWrap<ApplicationConfigPropertyExtVo> params, Model model) {
		logger.debug("TX BEGIN get ApplicationsConfigExt With {} ", params);

		for (ApplicationConfigPropertyExtVo param : params.getList()) {
			if (param.getApp_Id() != null && !param.getApp_Id().isEmpty() && param.getConfig_Id() != null && !param.getConfig_Id().isEmpty()
					&& param.getProp_Key() != null && !param.getProp_Key().isEmpty()) {
				applicationPropertyExtDao.uptApplicationPropertyExt(param);

			}
		}
		model.addAttribute("_rslt", "SUCCESS");

		logger.debug("TX BEGIN get ApplicationsConfigExt With {} ", params);

		return model;
	}

	@RequestMapping("/application/config/ext/delete")
	public Model delApplicationsConfigExt(@RequestBody ListWrap<ApplicationConfigPropertyExtVo> params, Model model) {
		logger.debug("TX BEGIN get ApplicationsConfigExt With {} ", params);

		for (ApplicationConfigPropertyExtVo param : params.getList()) {
			if (param.getApp_Id() != null && !param.getApp_Id().isEmpty() && param.getConfig_Id() != null && !param.getConfig_Id().isEmpty()
					&& param.getProp_Key() != null && !param.getProp_Key().isEmpty()) {
				applicationPropertyExtDao.delApplicationPropertyExt(param);
			}
		}
		model.addAttribute("_rslt", "SUCCESS");

		logger.debug("TX BEGIN get ApplicationsConfigExt With {} ", params);

		return model;
	}

	@RequestMapping("/application/config/ext/pop/list")
	public Model getApplicationsConfigSource(ApplicationConfigPropertyExtVo param, Model model) {
		logger.debug("TX BEGIN get ApplicationsConfigSource With {} ", param);
		List<ApplicationConfigPropertyExtVo> applicationPropertySource = applicationPropertyExtDao.getApplicationPropertySource(param);

		model.addAttribute("_rslt", applicationPropertySource);

		logger.debug("TX BEGIN get ApplicationsConfigSource With {} ", param);

		return model;
	}

}
