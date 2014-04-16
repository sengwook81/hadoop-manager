package org.zero.commons.spring.support.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressUtil {	

	protected static final Logger log = LoggerFactory.getLogger(CompressUtil.class);
	
	private static List<File> deCompress(File inputFile, final File outputDir) throws FileNotFoundException, IOException, ArchiveException {
		if (!(inputFile.getName().toLowerCase().endsWith("tar.gz") || inputFile.getName().toLowerCase().endsWith("tar"))) {
			log.trace("UnSupport File Format : {}", inputFile.getName());
			return Collections.EMPTY_LIST;
		}

		if (inputFile.getName().toLowerCase().endsWith(".gz")) {
			inputFile = unGzip(inputFile, outputDir);
		}

		return unTar(inputFile, outputDir);
	}

	private static List<File> unTar(final File inputFile, final File outputDir) throws FileNotFoundException, IOException, ArchiveException {

		log.info(String.format("Untaring %s to dir %s.", inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

		final List<File> untaredFiles = new LinkedList<File>();
		final InputStream is = new FileInputStream(inputFile);
		final TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
		TarArchiveEntry entry = null;
		while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
			final File outputFile = new File(outputDir, entry.getName());
			if (entry.isDirectory()) {
				log.info(String.format("Attempting to write output directory %s.", outputFile.getAbsolutePath()));
				if (!outputFile.exists()) {
					log.info(String.format("Attempting to create output directory %s.", outputFile.getAbsolutePath()));
					if (!outputFile.mkdirs()) {
						throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
					}
				}
			} else {
				log.info(String.format("Creating output file %s.", outputFile.getAbsolutePath()));
				final OutputStream outputFileStream = new FileOutputStream(outputFile);
				IOUtils.copy(debInputStream, outputFileStream);
				outputFileStream.close();
			}
			untaredFiles.add(outputFile);
		}
		debInputStream.close();

		return untaredFiles;
	}

	private static File unGzip(final File inputFile, final File outputDir) throws FileNotFoundException, IOException {

		log.info(String.format("Ungzipping %s to dir %s.", inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

		final File outputFile = new File(outputDir, inputFile.getName().substring(0, inputFile.getName().length() - 3));

		final GZIPInputStream in = new GZIPInputStream(new FileInputStream(inputFile));
		final FileOutputStream out = new FileOutputStream(outputFile);

		IOUtils.copy(in, out);

		in.close();
		out.close();

		return outputFile;
	}
}
