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
import org.zero.hadoop.manager.service.base.dao.ServerGroupDao;
import org.zero.hadoop.manager.service.base.vo.ServerGroupVo;
import org.zero.hadoop.manager.service.common.SeqManager;
import org.zero.hadoop.manager.util.ListWrap;

@Controller
public class ServerGroupCTR {
	
	private static Logger logger = LoggerFactory.getLogger(ServerGroupCTR.class);
	@Autowired
	ServerGroupDao serverGroupDao;
	
	@RequestMapping("/servergroup/list")
	public Model getServerGroups(ServerGroupVo param, Model model){
		logger.debug("TX BEGIN getServerGroups With {} ",param);
		List<ServerGroupVo> serverGroups = serverGroupDao.getServerGroupList(param);
		
		model.addAttribute("_rslt",serverGroups);
		logger.debug("TX FINISH getServerGroups With {} ",model);
		return model;
	}
	
	@Autowired
	SeqManager seqManager;
	
	public static final String SVRGRP_SEQ_NAME = "SG"; 
	
	@RequestMapping("/servergroup/add")
	@Transactional(propagation= Propagation.REQUIRED)
	public Model addServerGroups(@RequestBody ListWrap<ServerGroupVo> param, Model model){
		logger.debug("TX BEGIN addServerGroups With {} ",param);
		for(ServerGroupVo item : param.getList()) {
			if(StringUtils.isEmpty(item.getGroup_Id()))
			{
				String seq = seqManager.getSeq(SVRGRP_SEQ_NAME);
				item.setGroup_Id(seq);
				serverGroupDao.insServerGroup(item);	
			}
		}
		
		logger.debug("TX FINISH addServerGroups With {} ",model);
		return model;
	}
	
	
	@RequestMapping("/servergroup/modify")
	@Transactional(propagation= Propagation.REQUIRED)
	public Model modifyServerGroups(@RequestBody ListWrap<ServerGroupVo> param, Model model){
		logger.debug("TX BEGIN modifyServerGroups With {} ",param);
		for(ServerGroupVo item : param.getList()) {
			if(item.getDel_Yn()) {
				serverGroupDao.uptServerGroup(item);
			}
			else {
				serverGroupDao.delServerGroup(item);
			}
		}
		logger.debug("TX FINISH modifyServerGroups With {} ",model);
		return model;
	}
	
	/*
	@RequestMapping("/servergroup/delete")
	@Transactional(propagation= Propagation.REQUIRED)
	public Model deleteServerGroups(@RequestBody ListWrap<ServerGroupVo> param, Model model){
		logger.debug("TX BEGIN deleteServerGroups With {} ",param);
		for(ServerGroupVo item : param.getList()) {
			serverGroupDao.delServerGroup(item);	
		}
		logger.debug("TX FINISH deleteServerGroups With {} ",model);
		return model;
	}
	
	
	@RequestMapping("/servergroup/test")
	@Transactional(propagation= Propagation.REQUIRED)
	public Model test(@RequestBody ListWrap<ServerGroupVo> param, Model model){
		logger.debug("TX BEGIN modifyServerGroups With {} ",param);
		for(int i = 0;i<100;i++)
		{
			String seq = seqManager.getSeq(SVRGRP_SEQ_NAME);
			System.out.println(seq);
		}
		logger.debug("TX FINISH modifyServerGroups With {} ",model);
		return model;
	}
	
	*/
}
