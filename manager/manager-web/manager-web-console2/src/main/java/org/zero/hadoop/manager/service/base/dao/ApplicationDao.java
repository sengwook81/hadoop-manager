package org.zero.hadoop.manager.service.base.dao;

import java.util.List;

import org.zero.hadoop.manager.service.base.vo.ApplicationSVo;
import org.zero.hadoop.manager.service.base.vo.ApplicationVo;


public interface ApplicationDao {

	ApplicationVo getApplication(ApplicationVo param);
	
	List<ApplicationVo> getApplicationList(ApplicationVo param);
	
	List<ApplicationSVo> getApplicationConfigList(ApplicationVo param);
	
	int insApplication(ApplicationVo param);
	
	int uptApplication(ApplicationVo param);
	
	int delApplication(ApplicationVo param);
}
