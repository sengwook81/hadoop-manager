package org.zero.commons.core.support.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FutureThreadPool<T> {

	protected static final Logger log = LoggerFactory.getLogger(FutureThreadPool.class);
	private ExecutorService threadPool;

	List<Future<T>> retFutures = new ArrayList<Future<T>>();
	private int poolSize;

	public FutureThreadPool(int poolSize) {
		this.poolSize = poolSize;
		threadPool = Executors.newFixedThreadPool(poolSize);
	}

	public void addJob(Callable<T> callable) {
		Future<T> submit = threadPool.submit(callable);
		retFutures.add(submit);
	}

	public void excuteJob(Runnable runnable) {
		
		threadPool.execute(runnable);
	}

	public <K extends T> List<K> waitFinish() {
		List<K> ret = new ArrayList();

		for (Future<T> threadResult : retFutures) {
			K v = null;
			try {
				v = (K) threadResult.get();
				log.debug("Thread Output : {} ", v);
				ret.add(v);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				log.debug("Callable Thread Exception Occur : {}", v);
				e.printStackTrace();
			}
		}
		retFutures.clear();
		return ret;
	}

	public void destroyPool() {
		threadPool.shutdownNow();
		while (!threadPool.isTerminated()) {
			try {
				log.trace("Pool Shutdown Wait");
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
		threadPool = null;
		threadPool = Executors.newFixedThreadPool(poolSize);
	}

	public static void main(String[] args) {
		FutureThreadPool<String> futureThreadPool = new FutureThreadPool<String>(10);
		for (int i = 0; i < 100000; i++) {

			for (int k = 0; k < 100; k++) {
				futureThreadPool.excuteJob(new Runnable() {
					@Override
					public void run() {
						int tot = 0;
						for (int n = 0; n < 1000; n++)
							tot += n;
						System.out.println(tot);
					}
				});
			}
			// futureThreadPool.waitFinish();

		}
		futureThreadPool.destroyPool();
		System.out.println(Runtime.getRuntime().freeMemory());
	}
}
