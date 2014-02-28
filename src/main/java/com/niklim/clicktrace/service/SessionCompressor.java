package com.niklim.clicktrace.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.xml.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.props.UserProperties;

/**
 * Compresses Clicktrace session folder using zip.
 */
public class SessionCompressor {
	private static final Logger log = LoggerFactory.getLogger(SessionCompressor.class);
	@Inject
	private FileManager fileManager;

	@Inject
	private UserProperties props;

	public static interface FileLoader {
		Optional<InputStream> load(String filename, String filepath) throws IOException;
	}

	/**
	 * Compresses given Clicktrace session and encodes it as String. Uses
	 * {@link FileLoader} to load session files and perform arbitrary
	 * modifications.
	 * 
	 * @param session
	 * @param fileLoader
	 * @return encoded and compressed Clicktrace session
	 * @throws IOException
	 */
	public String compressWithImageWidth(Session session, FileLoader fileLoader) throws IOException {
		String sessionName = session.getName();

		byte[] zipBytes = zip(sessionName, fileLoader);
		log.debug("Zipped size={}", zipBytes.length);

		String content = Base64.encode(zipBytes);
		return content;
	}

	private byte[] zip(String sessionName, FileLoader fileLoader) throws IOException {
		log.debug("Zip compression started");

		byte[] buffer = new byte[1024];
		String source = props.getSessionsDirPath() + sessionName;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);

		List<String> filenames = fileManager.loadFileNames(source, new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return true;
			}
		});

		for (String filename : filenames) {

			String filepath = source + File.separator + filename;
			Optional<InputStream> inOpt = fileLoader.load(filename, filepath);

			if (!inOpt.isPresent()) {
				continue;
			}

			ZipEntry ze = new ZipEntry(sessionName + File.separator + filename);
			zos.putNextEntry(ze);

			int len;
			while ((len = inOpt.get().read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			inOpt.get().close();
			log.debug("File Added : " + filename);
		}

		zos.closeEntry();
		zos.close();

		log.debug("Zip compression finished");
		byte[] byteArray = bos.toByteArray();
		bos.close();
		return byteArray;
	}

}
