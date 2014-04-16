package org.zero.hadoop.manager.service.app.dao;

import java.util.List;

import org.zero.hadoop.manager.service.app.vo.HadoopVo;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;

public interface HadoopDao {
	List<HadoopVo> getServerApplications(ServerApplicationVo param);
	int insServerApplication(ServerApplicationVo param);
	int uptServerApplication(ServerApplicationVo param);
	int delServerApplication(ServerApplicationVo param);
}
