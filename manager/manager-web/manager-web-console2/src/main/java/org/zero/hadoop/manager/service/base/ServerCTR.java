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
import org.zero.hadoop.manager.service.base.dao.ServerDao;
import org.zero.hadoop.manager.service.base.vo.ServerVo;
import org.zero.hadoop.manager.service.common.SeqManager;
import org.zero.hadoop.manager.util.ListWrap;

@Controller
public class ServerCTR {

	@Autowired
	SeqManager seqManager;
	public static final String SVRGRP_SEQ_NAME = "SV";
	
	private static Logger logger = LoggerFactory.getLogger(ServerCTR.class);
	@Autowired
	ServerDao ServerDao;

	@RequestMapping("/server/list")
	public Model getServers(ServerVo param, Model model) {
		logger.debug("TX BEGIN getServers With {} ", param);
		List<ServerVo> Servers = ServerDao.getServerList(param);

		model.addAttribute("_rslt", Servers);
		logger.debug("TX FINISH getServers With {} ", model);
		return model;
	}

	@RequestMapping("/server/add")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model addServers(@RequestBody ListWrap<ServerVo> param, Model model) {
		logger.debug("TX BEGIN addServers With {} ", param);
		for (ServerVo item : param.getList()) {
			if (StringUtils.isEmpty(item.getServer_Id())) {
				String seq = seqManager.getSeq(SVRGRP_SEQ_NAME);
				item.setServer_Id(seq);
				ServerDao.insServer(item);
			}
		}

		logger.debug("TX FINISH addServers With {} ", model);
		return model;
	}

	@RequestMapping("/server/modify")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model modifyServers(@RequestBody ListWrap<ServerVo> param, Model model) {
		logger.debug("TX BEGIN modifyServers With {} ", param);
		for (ServerVo item : param.getList()) {
			if(item.getDel_Yn()) {
				ServerDao.delServer(item);
			}
			else {
				ServerDao.uptServer(item);
			}
		}
		logger.debug("TX FINISH modifyServers With {} ", model);
		return model;
	}

	/*
	@RequestMapping("/server/delete")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model deleteServers(@RequestBody ListWrap<ServerVo> param, Model model) {
		logger.debug("TX BEGIN deleteServers With {} ", param);
		for (ServerVo item : param.getList()) {
			ServerDao.delServer(item);
		}
		logger.debug("TX FINISH deleteServers With {} ", model);
		return model;
	}

	@RequestMapping("/server/test")
	@Transactional(propagation = Propagation.REQUIRED)
	public Model test(@RequestBody ListWrap<ServerVo> param, Model model) {
		logger.debug("TX BEGIN modifyServers With {} ", param);
		for (int i = 0; i < 100; i++) {
			String seq = seqManager.getSeq(SVRGRP_SEQ_NAME);
			System.out.println(seq);
		}
		logger.debug("TX FINISH modifyServers With {} ", model);
		return model;
	}
	*/
}
