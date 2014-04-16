package org.zero.commons.core.support.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.zero.commons.core.support.DoWith;

/**
 * Tar 파일 압축 관련 처리 Class
 * @author SengWook Jung
 *
 */
public class ZTarCompressor extends ZBaseCompressor {

	protected static final Logger log = Logger.getLogger(ZTarCompressor.class
			.getName());

	public void compress(String resultName, String file) {
		compress(resultName, new File(file));
	}

	public void compress(String resultName, File file) {
		ArrayList<File> arrayList = new ArrayList<File>();
		arrayList.add(file);
		String baseDir = file.getParent();
		if(file.isDirectory()){
			
		}
		compress(resultName, arrayList, baseDir);
	}

	public void compress(String resultName, List<File> files , final String baseDir) {

		TarArchiveOutputStream tOut = null;
		File f = new File(resultName);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		try {
			tOut = new TarArchiveOutputStream(new FileOutputStream(f));
			for (File item : files) {
				exploreFiles(item, tOut, new DoWith<CompressData>() {
					public CompressData doWith(CompressData cd) {
						File f = cd.getFile();
						OutputStream out = cd.getOutputStream();
						log.info(baseDir.length() + " , " + f.getName());
						String entryName = f.getPath().substring(baseDir.length());
						TarArchiveOutputStream tarOut = (TarArchiveOutputStream) out;
						TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
						log.info(entryName);
						try {
							tarOut.putArchiveEntry(tarEntry);
							IOUtils.copy(new FileInputStream(f), tarOut);
							tarOut.closeArchiveEntry();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						finally {
							IOUtils.closeQuietly(tarOut);						
						}
						return cd;
					}
				});
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(tOut);
		}
	}

	public List<File> decompress(String fileName, String targetDir) {

		final List<File> untaredFiles = new LinkedList<File>();
		File f = new File(targetDir);
		if (!f.exists()) {
			f.mkdirs();
		}

		File inputFile = new File(fileName);
		File outputDir = new File(targetDir);

		log.info(String.format("Untaring %s to dir %s.",
				inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));
		InputStream is = null;
		TarArchiveInputStream debInputStream = null;
		try {
			is = new FileInputStream(inputFile);
			debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory()
					.createArchiveInputStream("tar", is);

			TarArchiveEntry entry = null;
			while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
				final File outputFile = new File(outputDir, entry.getName());
				if (entry.isDirectory()) {
					log.info(String.format(
							"Attempting to write output directory %s.",
							outputFile.getAbsolutePath()));
					if (!outputFile.exists()) {
						log.info(String.format(
								"Attempting to create output directory %s.",
								outputFile.getAbsolutePath()));
						if (!outputFile.mkdirs()) {
							throw new RuntimeException(String.format(
									"Couldn't create directory %s.",
									outputFile.getAbsolutePath()));
						}
					}
				} else {
					log.info(String.format("Creating output file %s.",
							outputFile.getAbsolutePath()));
					
					if(!outputFile.getParentFile().exists()){ 
						outputFile.getParentFile().mkdirs();
					}
					OutputStream outputFileStream = new FileOutputStream(
							outputFile);
					IOUtils.copy(debInputStream, outputFileStream);
					IOUtils.closeQuietly(outputFileStream);
				}
				untaredFiles.add(outputFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(debInputStream);
		}

		return untaredFiles;
	}
}
