package org.zero.commons.core.support.thread;

/**
 * Thread 동기화 Class
 * @author SengWook
 *
 */
public class Done {

	// 활성 화 쓰레드 개수.
	private int _activeThreads = 0;

	private boolean _started = false;


	/**
	 * 종료대기
	 */
	synchronized public void waitDone() {
		try {
			while (_activeThreads > 0) {
				wait();
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * 시작 대기
	 */
	synchronized public void waitBegin() {
		try {
			while (!_started) {
				wait();
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * 작업 시작.
	 */
	synchronized public void workerBegin() {
		_activeThreads++;
		_started = true;
		notify();
	}

	/**
	 * 작업 종료.
	 */
	synchronized public void workerEnd() {
		_activeThreads--;
		notify();
	}

	/**
	 * 작업 초기화
	 */
	synchronized public void reset() {
		_activeThreads = 0;
	}

}
