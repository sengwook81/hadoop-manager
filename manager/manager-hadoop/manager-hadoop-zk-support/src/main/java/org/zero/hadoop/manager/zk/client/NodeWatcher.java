package org.zero.hadoop.manager.zk.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class NodeWatcher  implements Watcher{

	@Override
	public void process(WatchedEvent event) {
		System.out.println("PATH : " + event.getPath() );
		System.out.println("STATUS : " + event.getState() );
		System.out.println("TYPE : " + event.getType().toString() );
	}

}
