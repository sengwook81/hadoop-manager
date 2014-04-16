package org.zero.commons.core.support.compress;

import java.io.File;
import java.io.OutputStream;

import org.zero.commons.core.support.DoWith;

/**
 * 기본 압축 추상클래스. 
 * @author Administrator
 */
public abstract class ZBaseCompressor implements ZCompressor{

	protected static int BUFFER_SIZE = 10240;
	
	public static interface CompressBehavior {
		public void compress(CompressData compressData);
	}
	
	public static class CompressData {
		private File file;
		private OutputStream outputStream;
		
		public CompressData(File file, OutputStream outputStream) {
			super();
			this.file = file;
			this.outputStream = outputStream;
		}
		/**
		 * @return the file
		 */
		public File getFile() {
			return file;
		}
		/**
		 * @param file the file to set
		 */
		public void setFile(File file) {
			this.file = file;
		}
		/**
		 * @return the outputStream
		 */
		public OutputStream getOutputStream() {
			return outputStream;
		}
		/**
		 * @param outputStream the outputStream to set
		 */
		public void setOutputStream(OutputStream outputStream) {
			this.outputStream = outputStream;
		}
	}
	/**
	 * @param f
	 * @param out
	 * @param compressor
	 */
	protected void exploreFiles(File f,OutputStream out,DoWith<CompressData> compressor) {
		if(!f.exists()) {
			return ;
		}
		
		if(f.isDirectory()) {
			for(File child : f.listFiles()) {
				exploreFiles(child,out,compressor);	
			}
		}
		else {
			compressor.doWith(new CompressData(f,out));
		}
	}
}
