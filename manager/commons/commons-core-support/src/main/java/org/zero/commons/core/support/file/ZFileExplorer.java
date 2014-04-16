package org.zero.commons.core.support.file;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zero.commons.core.support.DoWith;

/**
 * File 디렉토리 순환 처리 Claass 
 * @author Administrator
 *
 */
public class ZFileExplorer {

	protected static final Logger log = LoggerFactory
			.getLogger(ZFileExplorer.class);
	
	public static final DoWith<File> NOTHING_PROCESSOR = new DoWith<File>() {
		public File doWith(File data) {
			return data;
		}
	};
	
	/**
	 * 파일 순환.
	 * @param node 탐색 대상 File Node
	 * @param processor 파일 처리기.
	 */
	public static void ExploreFile(File node, DoWith<File> processor ) {
		
		if(!node.exists()) { 
			return ;
		}
		
		if(node.isDirectory()) {
			for(File child : node.listFiles()) { 
				ExploreFile(child,processor);
			}
		}
		else {
			processor.doWith(node);
		}
	}
}
