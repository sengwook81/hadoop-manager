package org.zero.hadoop.manager.service.base.cmp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zero.hadoop.manager.service.app.dao.ApplicationPropertyExtDao;
import org.zero.hadoop.manager.service.app.vo.ApplicationConfigPropertyExtVo;

public abstract class ConfigBuilder {

	@Autowired
	ApplicationPropertyExtDao applicationPropertyExtDao;
	
	public String buildConfig(String app_Id , String config_Id) {
		
		ApplicationConfigPropertyExtVo param = new ApplicationConfigPropertyExtVo();
		param.setApp_Id(app_Id);
		param.setConfig_Id(config_Id);
		
		return buildData(applicationPropertyExtDao.getApplicationPropertyExt(param));
	}
	protected abstract String buildData(List<ApplicationConfigPropertyExtVo> data);
}
