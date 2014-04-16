package org.zero.hadoop.manager.util;

import javax.annotation.PostConstruct;

public class ZeroStarter {
	
	
	
	
	@PostConstruct
	private void init() { 
		System.setProperty("ZERO", "zeroAdmin");
	}
}