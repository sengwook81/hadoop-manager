package org.zero.hadoop.manager.service.app.dao;

import java.util.List;

import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;

public interface ServerApplicationDao {

	ServerApplicationVo getServerApplication(ServerApplicationVo param);
	List<ServerApplicationVo> getServerApplications(ServerApplicationVo param);
	int insServerApplication(ServerApplicationVo param);
	// 어플리케이션 정보 데이터 갱신.
	int uptServerApplication(ServerApplicationVo param);
	// 상태코드만 갱신.
	int uptServerApplicationStatus(ServerApplicationVo param);
	int delServerApplication(ServerApplicationVo param);
}
