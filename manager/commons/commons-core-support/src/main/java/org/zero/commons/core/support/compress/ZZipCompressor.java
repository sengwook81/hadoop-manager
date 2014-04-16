package org.zero.commons.core.support.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;


/**
 * Tar 파일 압축 관련 처리 Class
 * @author SengWook Jung
 *
 */
public class ZZipCompressor extends ZBaseCompressor implements ZCompressor {

	protected static Logger log = Logger.getLogger(ZZipCompressor.class
			.getName());

	public void compress(String resultName, String file) {
		compress(resultName,new File(file));
	}

	public void compress(String resultName, File file) {
		
		if(file.exists()) {
			throw new RuntimeException(file.getName() + " Not Found");
		}
		
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		GZIPOutputStream gzipOS = null;
		
		File f = new File(resultName);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		
		
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(new File(resultName));
			gzipOS = new GZIPOutputStream(fos);
/*			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) != -1) {
				gzipOS.write(buffer, 0, len);
			}*/
			IOUtils.copy(fis, gzipOS);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			
			IOUtils.closeQuietly(gzipOS);
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(fis);
		}
	}

	public void compress(String resultName, List<File> files , String baseDir)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Gzip Support Single");
	}

	public List<File> decompress(String fileName, String targetDir ) {
		ArrayList<File> arrayList = new ArrayList<File>();


		log.info("Check Target Dir : " + targetDir);
		File f = new File(targetDir);
		if (!f.exists()) {
			f.mkdirs();
			System.out.println("Make Parent Dir : " + f.getPath());
		}
		
		
		File inputFile = new File(fileName);
		File outputDir = new File(targetDir);
		log.info(String.format("Ungzipping %s to dir %s.",
				inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

		final File outputFile = new File(outputDir, inputFile.getName()
				.substring(0, inputFile.getName().length() - 3));

		GZIPInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new GZIPInputStream(new FileInputStream(inputFile));
			out = new FileOutputStream(outputFile);
			IOUtils.copy(in, out);
			arrayList.add(outputFile);
			return arrayList;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

}
