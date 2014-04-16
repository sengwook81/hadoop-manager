package org.zero.hadoop.manager.service.base.dao;

import java.util.List;

import org.zero.hadoop.manager.service.base.vo.ServerVo;

public interface ServerDao {

	ServerVo getServer(ServerVo param);

	List<ServerVo> getServerList(ServerVo param);

	int insServer(ServerVo param);

	int uptServer(ServerVo param);

	int delServer(ServerVo param);
}
