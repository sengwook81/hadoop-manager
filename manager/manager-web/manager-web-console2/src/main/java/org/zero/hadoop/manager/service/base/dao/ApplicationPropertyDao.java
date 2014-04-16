package org.zero.hadoop.manager.service.base.dao;

import java.util.List;

import org.zero.hadoop.manager.service.base.vo.ApplicationPropertyVo;


public interface ApplicationPropertyDao {

	List<ApplicationPropertyVo> getApplicationPropertyList(ApplicationPropertyVo param);
	
	int insApplicationProperty(ApplicationPropertyVo param);
	
	int uptApplicationProperty(ApplicationPropertyVo param);
	
	int delApplicationProperty(ApplicationPropertyVo param);
}
