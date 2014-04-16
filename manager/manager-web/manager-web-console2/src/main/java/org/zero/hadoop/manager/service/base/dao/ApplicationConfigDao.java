package org.zero.hadoop.manager.service.base.dao;

import java.util.List;

import org.zero.hadoop.manager.service.base.vo.ApplicationConfigVo;


public interface ApplicationConfigDao {

	List<ApplicationConfigVo> getApplicationConfig(ApplicationConfigVo param);
	
	int insApplicationConfig(ApplicationConfigVo param);
	
	int uptApplicationConfig(ApplicationConfigVo param);
	
	int delApplicationConfig(ApplicationConfigVo param);
}
