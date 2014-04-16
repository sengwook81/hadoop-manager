package org.zero.hadoop.manager.service.app;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;
import org.zero.hadoop.manager.util.ListWrap;

@Controller
public class ServerApplicationCTR extends AppCTR<ServerApplicationVo>{

	@Override
	@RequestMapping("/application/server/list")
	public Model getApplications(ServerApplicationVo param, Model model) {
		return super.getApplications(param, model);
	}

	@Override
	@RequestMapping("/application/server/add")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model addApplications(@RequestBody ListWrap<ServerApplicationVo> param, Model model) {
		return super.addApplications(param, model);
	}

	@Override
	@RequestMapping("/application/server/modify")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model updateApplications(@RequestBody ListWrap<ServerApplicationVo> param, Model model) {
		return super.updateApplications(param, model);
	}

}
