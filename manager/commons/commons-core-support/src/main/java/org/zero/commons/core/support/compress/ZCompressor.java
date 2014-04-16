package org.zero.commons.core.support.compress;

import java.io.File;
import java.util.List;

/**
 * 압축 처리 표준 interface
 * @author SengWook Jung
 */
public interface ZCompressor {
	
	/** 단일파일 압축.
	 * @param resultName 결과 압축파일명
	 * @param file 대상 파일명
	 */
	public void compress(String resultName , String file);
	
	
	/** 단일파일 압축.
	 * @param resultName 결과 압축파일명
	 * @param file 대상 파일
	 */
	public void compress(String resultName , File file);
	
	/**
	 * @param resultName 결과 압축파일명
	 * @param files 대상 파일 목록.
	 * @param rootDir 최상위 경로
	 */
	public void compress(String resultName, List<File> files, String rootDir);
	
	/**
	 * @param fileName 압축해제 대상 압축파일 명
	 * @param targetDir 압축 해제 경로
	 * @return
	 */
	public List<File> decompress(String fileName,String targetDir );
}
