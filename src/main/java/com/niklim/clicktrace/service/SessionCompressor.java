package com.niklim.clicktrace.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.xml.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.Session;

/**
 * Compresses Clicktrace session folder using zip.
 */
public class SessionCompressor {
	private static final Logger log = LoggerFactory.getLogger(SessionCompressor.class);
	@Inject
	private FileManager fileManager;

	/**
	 * Compresses given Clicktrace session and encodes it as String.
	 * 
	 * @param session
	 * @return encoded and compressed Clicktrace session
	 * @throws IOException
	 */
	public String compress(Session session) throws IOException {
		String sessionName = session.getName();

		byte[] zipBytes = zip(sessionName);
		log.debug("Zipped size={}", zipBytes.length);

		String content = Base64.encode(zipBytes);
		return content;
	}

	private byte[] zip(String sessionName) throws IOException {
		log.debug("Zip compression started");

		byte[] buffer = new byte[1024];
		String source = FileManager.SESSIONS_DIR + sessionName;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);

		List<String> filenames = fileManager.loadFileNames(source, new FileManager.NoTrashFilter());

		for (String file : filenames) {
			log.debug("File Added : " + file);
			ZipEntry ze = new ZipEntry(sessionName + File.separator + file);
			zos.putNextEntry(ze);

			FileInputStream in = new FileInputStream(source + File.separator + file);

			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			in.close();
		}

		zos.closeEntry();
		zos.close();

		log.debug("Zip compression finished");
		return bos.toByteArray();
	}

}
