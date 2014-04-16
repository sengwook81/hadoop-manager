package org.zero.commons.core.support.thread;

public abstract class ThreadRunner<T> implements Runnable{

	
	@Override
	public void run() {
		
	}
	
	public abstract T customRun();
}
