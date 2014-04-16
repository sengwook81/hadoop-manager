package org.zero.hadoop.manager.service.app.cmp;

import org.springframework.stereotype.Component;
import org.zero.commons.core.support.thread.FutureThreadPool;
import org.zero.hadoop.manager.service.app.processor.AppInstallProcessor;

@Component
public class InstallerPool extends FutureThreadPool<AppInstallProcessor>{

	public InstallerPool() {
		super(10);
	}
	
	public InstallerPool(int poolSize) {
		super(poolSize);
	}
}
