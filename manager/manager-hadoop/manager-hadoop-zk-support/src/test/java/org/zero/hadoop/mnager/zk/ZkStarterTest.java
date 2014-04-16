package org.zero.hadoop.mnager.zk;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.junit.Test;

public class ZkStarterTest {

	
	int tickTime = 2000;
	int clientPort = 2181;
	int numberOfConnection = Integer.MAX_VALUE;
	
	@Test
	public void startZk()
	{
		String dataDirectory = System.getProperty("java.io.tmpdir");

		File dir = new File(dataDirectory, "zookeeper").getAbsoluteFile();
		try {
			ZooKeeperServer zks = new ZooKeeperServer(dir, dir, tickTime);
			ServerCnxnFactory standaloneServerFactory = NIOServerCnxnFactory.createFactory(new InetSocketAddress(clientPort), numberOfConnection);
			
			standaloneServerFactory.startup(zks);
			
			standaloneServerFactory.join();
			
			standaloneServerFactory.shutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
