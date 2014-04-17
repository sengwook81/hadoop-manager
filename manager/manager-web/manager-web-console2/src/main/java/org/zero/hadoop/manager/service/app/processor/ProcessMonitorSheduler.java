package org.zero.hadoop.manager.service.app.processor;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProcessMonitorSheduler {
	private static Logger logger = LoggerFactory.getLogger(ProcessMonitorSheduler.class);

	@Autowired
	ProcessMonitor monitor;

	@Scheduled(fixedDelay = 15000)
	public void removeOldStatus() {
		long timeInMillis = Calendar.getInstance().getTimeInMillis();
		
		Iterator<Entry<String, ProcessMonitorVo>> iterator = monitor.getProcessMap().entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, ProcessMonitorVo> item = iterator.next();
			if (timeInMillis - item.getValue().getProcess_Time() > 15000) {
				logger.debug("##### Remove Old Status -> {} , {}",item.getKey(),  item.getValue());
				//monitor.getProcessMap().remove(item.getKey());
				iterator.remove();
			}
		}
	}
}
