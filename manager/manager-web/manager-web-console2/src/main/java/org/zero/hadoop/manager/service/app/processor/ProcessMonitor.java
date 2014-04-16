package org.zero.hadoop.manager.service.app.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;

@Component
public class ProcessMonitor {

	Map<String, ProcessMonitorVo> processMap = Collections.synchronizedMap(new LinkedHashMap<String, ProcessMonitorVo>());

	Map<String, ProcessMonitorVo> getProcessMap() {
		return processMap;
	}

	public void addProcessData(ServerApplicationVo item, String processStatus, String processData) {
		processMap.put(item.getServer_Id() + item.getApp_Id(), new ProcessMonitorVo(item, processStatus, processData));
	}

	public List<ProcessMonitorVo> getProcessData() {
		ArrayList<ProcessMonitorVo> retData = new ArrayList<ProcessMonitorVo>(processMap.values());
		return retData;
	}

}
