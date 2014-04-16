package org.zero.hadoop.manager.service.app.dao;

import java.util.List;

import org.zero.hadoop.manager.service.app.vo.InstallerVo;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;

public interface InstallerDao {
	List<InstallerVo> getAppServers(InstallerVo param);
}
