package org.zero.hadoop.manager.service.base.dao;

import java.util.List;

import org.zero.hadoop.manager.service.base.vo.ServerGroupVo;


public interface ServerGroupDao {

	List<ServerGroupVo> getServerGroupList(ServerGroupVo param);
	
	int insServerGroup(ServerGroupVo param);
	
	int uptServerGroup(ServerGroupVo param);
	
	int delServerGroup(ServerGroupVo param);
}
