package org.zero.commons.core.support.compress;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.zero.commons.core.support.file.ZFileUtils;



public class ZCompressUtil {

	public static enum COMPRESSOR_TYPE {
		TAR_GZ,
		TAR,
		ZIP;
	}
	
	public static void decompress(String file, String targetPath ,COMPRESSOR_TYPE type ) {
		if(type == COMPRESSOR_TYPE.TAR_GZ) {
			
			try {
				String createTempDirectory = ZFileUtils.createTempDirectory();
				System.out.println("CREATE TEMP PATH : " +createTempDirectory);
				ZCompressor zzip = new ZZipCompressor();
				List<File> decompress = zzip.decompress(file, createTempDirectory);
				ZTarCompressor ztar = new ZTarCompressor();
				for(File tpFile : decompress) {
					System.out.println(tpFile.getAbsolutePath());
					System.out.println(tpFile.getName());
					ztar.decompress(tpFile.getPath(), targetPath);
				}
				File tempDir = new File(createTempDirectory);
				FileUtils.deleteDirectory(tempDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(type == COMPRESSOR_TYPE.TAR) {
			try {
				ZTarCompressor ztar = new ZTarCompressor();
				ztar.decompress(file, targetPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(type == COMPRESSOR_TYPE.ZIP) {
			try {
				ZCompressor zzip = new ZZipCompressor();
				List<File> decompress = zzip.decompress(file, targetPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
