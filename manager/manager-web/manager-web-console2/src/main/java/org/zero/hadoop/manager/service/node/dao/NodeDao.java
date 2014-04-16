package org.zero.hadoop.manager.service.node.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.MapKey;
import org.zero.hadoop.manager.service.node.vo.NodeVo;

public interface NodeDao {

	@MapKey("node_id")
	public HashMap<Integer,NodeVo> getNodeList();
	public int getLastId();
}
