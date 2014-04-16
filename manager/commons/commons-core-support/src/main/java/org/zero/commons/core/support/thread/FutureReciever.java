package org.zero.commons.core.support.thread;

import java.util.concurrent.Future;

public interface FutureReciever<T> {

	public void setFuture(Future<T> future);
}
