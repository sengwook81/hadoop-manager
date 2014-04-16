package org.zero.commons.core.support.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.zero.commons.core.support.compress.ZTarCompressor;

/**
 * Thread Pool
 * 
 * @author SengWook
 * 
 */
public class ThreadPool {

	protected static final Logger log = Logger.getLogger(ZTarCompressor.class.getName());
	
	protected Thread threads[] = null;

	Collection assignments = new ArrayList(5);

	/**
	 * 작업 동기화 객체.
	 */
	protected Done done = new Done();

	public ThreadPool(int size) {
		threads = new WorkerThread[size];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new WorkerThread(this);
			threads[i].start();
		}
	}

	/**
	 * 작업 등록
	 * 
	 * @param r Runnable 작업 객체
	 */
	public synchronized void assign(Runnable r) {
		// 잡업 시작.
		done.workerBegin();
		assignments.add(r);
		// 잡업 추가 통지.
		log.info("Thread Pool Add Job [" + r.hashCode() + "]");
		notify();
	}

	public synchronized Runnable getAssignment() {
		try {
			while (!assignments.iterator().hasNext())
				log.info("Thread Pool Wait Job ");
				wait();
				Runnable r = (Runnable) assignments.iterator().next();
			// 작업 큐에서 해당 작업 제거.
			assignments.remove(r);
			return r;
		} catch (InterruptedException e) {
			done.workerEnd();
			return null;
		}
	}

	public void complete() {
		done.waitBegin();
		done.waitDone();
	}

	/**
	 * Thread 종료 .
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() {
		done.reset();
		for (int i = 0; i < threads.length; i++) {
			// 쓰레드 중단.
			threads[i].interrupt();
			// 수행중
			done.workerBegin();
			// threads[i].destroy();
			threads[i] = null;
		}
		// 작업 종료 대기.
		done.waitDone();
	}

	public void closeThread() {
		finalize();
	}
}

/**
 * 작업 수행 Thread
 * 
 * @author Administrator
 * 
 */
class WorkerThread extends Thread {
	protected static final Logger log = Logger.getLogger(WorkerThread.class.getName());
	public boolean busy;
	public ThreadPool owner;

	WorkerThread(ThreadPool o) {
		owner = o;
	}

	public void run() {
		Runnable target = null;
		do {
			// 처리대상 작업 획득.
			target = owner.getAssignment();
			
			if (target != null) {
				log.info("Worker Thread Start Job [" + target.hashCode() + "]");
				
				target.run();
				// 작업 종료 통지.
				owner.done.workerEnd();
			}
		} while (target != null);
	}
}