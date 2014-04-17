package org.zero.commons.core.support.network.ssh;

import org.zero.commons.core.support.progress.ProgressMonitor;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * Ftp 전송 진행률 모니터 Wrapper Class
 * @author SengWook Jung
 *
 */
public class SFtpProgressMonitorWrap implements SftpProgressMonitor {

	private long maxSize;
	private long counter = 0;
	private ProgressMonitor monitor = new ProgressMonitor() {
		@Override
		public void progress(int nPercent) {
		}
		@Override
		public void finish() {
		}
		@Override
		public void begin() {
		}
	};

	public SFtpProgressMonitorWrap(long maxSize,ProgressMonitor monitor) {
		this.maxSize = maxSize;
		if(monitor != null) {
			this.monitor = monitor;
		}
	}
	
	@Override
	public void init(int op, String src, String dest, long max) {
		monitor.begin();
	}

	@Override
	public boolean count(long count) {
		//System.out.println(count);
		counter += count;
		int val = (int) (((float)counter / (float)maxSize)*100);
		monitor.progress(val);
		return true;
	}

	@Override
	public void end() {
		monitor.finish();
	}
}