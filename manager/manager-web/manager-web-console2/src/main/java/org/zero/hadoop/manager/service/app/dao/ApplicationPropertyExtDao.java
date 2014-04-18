package org.zero.hadoop.manager.service.app.dao;

import java.util.List;

import org.zero.hadoop.manager.service.app.vo.ApplicationConfigPropertyExtVo;

public interface ApplicationPropertyExtDao {

	
	List<ApplicationConfigPropertyExtVo> getApplicationPropertySource(ApplicationConfigPropertyExtVo param);
	List<ApplicationConfigPropertyExtVo> getApplicationPropertyExt(ApplicationConfigPropertyExtVo param);
	int insApplicationPropertyExt(ApplicationConfigPropertyExtVo param);
	int uptApplicationPropertyExt(ApplicationConfigPropertyExtVo param);
	int delApplicationPropertyExt(ApplicationConfigPropertyExtVo param);
}
