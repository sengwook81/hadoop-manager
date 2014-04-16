package org.zero.hadoop.mnager.zk;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.netflix.curator.utils.ZKPaths;


public class ZookeeperClientTest  {

	public static void main(String[] args) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("name1:2181", retryPolicy);
		
		client.start();
		
		try
		{			
			String forPath = client.create().forPath("/zero/mynode1", null);
			System.out.println("FOR PATH : " +forPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
