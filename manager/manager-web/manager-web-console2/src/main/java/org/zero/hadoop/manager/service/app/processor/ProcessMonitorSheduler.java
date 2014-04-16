package org.zero.hadoop.manager.service.app.processor;

import java.util.Calendar;

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
		for (String key : monitor.getProcessMap().keySet()) {
			if (timeInMillis - monitor.getProcessMap().get(key).getProcess_Time() > 15000) {
				logger.debug("Remove Old Status -> {}", monitor.getProcessMap().get(key));
				monitor.getProcessMap().remove(key);
			}
		}
	}
}
