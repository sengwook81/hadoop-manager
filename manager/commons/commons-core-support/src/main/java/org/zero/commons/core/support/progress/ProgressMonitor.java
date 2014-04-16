package org.zero.commons.core.support.progress;

public interface ProgressMonitor {

	public void begin() ;
	
	public void progress(int nPercent);
	
	public void finish();
}

