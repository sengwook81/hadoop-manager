package org.zero.commons.core.support.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.zero.commons.core.support.DoWith;

/**
 * Jar 파일 압축 관련 처리 Class
 * @author SengWook Jung
 *
 */
public class ZJarCompressor extends ZBaseCompressor {
	protected static final Logger log = Logger.getLogger(ZTarCompressor.class
			.getName());

	public void compress(String resultName, String file) {
		compress(resultName, new File(file));
	}

	public void compress(String resultName, File file) {
		ArrayList<File> arrayList = new ArrayList<File>();
		arrayList.add(file);
		compress(resultName, arrayList, file.getParent());
	}

	public void compress(String resultName, List<File> files,
			final String baseDir) {

		FileOutputStream stream = null;
		JarOutputStream out = null;
		try {
			stream = new FileOutputStream(resultName);
			out = new JarOutputStream(stream, new Manifest());

			for (File itemFile : files) {
				if (itemFile == null)
					continue;

				exploreFiles(itemFile, out, new DoWith<CompressData>() {
					public CompressData doWith(CompressData out) {
						String entryName = out.getFile().getPath().substring(
								baseDir.length());
						byte buffer[] = new byte[BUFFER_SIZE];
						JarOutputStream jout = (JarOutputStream) out.getOutputStream();
						log.info("Adding " + entryName);
						// TODO Auto-generated method stub
						JarEntry jarAdd = new JarEntry(entryName);
						jarAdd.setTime(out.getFile().lastModified());
						try {
							jout.putNextEntry(jarAdd);
							FileInputStream in = new FileInputStream(out.getFile());
						/*	while (true) {
								int nRead = in.read(buffer, 0, buffer.length);
								if (nRead <= 0)
									break;
								.write(buffer, 0, nRead);
							}*/
							IOUtils.copy(in, out.getOutputStream());
							IOUtils.closeQuietly(in);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						
						return out;
					}
				});
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(stream);
		}
	}

	public List<File> decompress(String fileName, String targetDir) {

		FileOutputStream fos = null;
		InputStream is = null; // get the
		ArrayList<File> retData = new ArrayList<File>();
		try {

			File rootDir = new File(targetDir);
			if (!rootDir.exists()) {
				rootDir.mkdirs();
			}
			JarFile jar = new JarFile(fileName);
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry file = (java.util.jar.JarEntry) entries.nextElement();
				java.io.File f = new java.io.File(targetDir
						+ java.io.File.separator + file.getName());

				if (file.isDirectory()) {

				} else {
					if (!f.getParentFile().exists()) { // if its a directory,
														// create it
						f.getParentFile().mkdirs();
					}
					log.info("Decompress File " + f.getPath());
					is = jar.getInputStream(file);
					fos = new java.io.FileOutputStream(f);
					/*while (is.available() > 0) { // write contents of 'is' to
													// 'fos'
						fos.write(is.read());
					}*/
					IOUtils.copy(is, fos);
					IOUtils.closeQuietly(fos);
					IOUtils.closeQuietly(is);
					retData.add(f);
				}

			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(is);
		}
		return retData;
	}
}
