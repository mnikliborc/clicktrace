package com.niklim.clicktrace.service.export.jira;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Optional;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.service.SessionCompressor;

/**
 * Loads session files. Additionally adds one line to
 * {@link FileManager#SESSION_PROPS_FILENAME} file with initial image width
 * (TODO improve design).
 */
public class SessionFileLoader implements SessionCompressor.FileLoader {
	private Integer imageWidth;

	public SessionFileLoader(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	@Override
	public Optional<InputStream> load(String filename, String filepath) throws IOException {
		if (filename.endsWith("~") || filename.equals(".DS_Store")) {
			return Optional.<InputStream> absent();
		}
		InputStream in = new FileInputStream(filepath);
		if (StringUtils.equals(filename, FileManager.SESSION_PROPS_FILENAME)) {
			return Optional.of(addInitImageWidthProperty(in, imageWidth));
		} else {
			return Optional.of(in);
		}
	}

	private InputStream addInitImageWidthProperty(InputStream in, Integer initImageWidth) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sessionPropsContent = new StringBuilder();

		readAllButInitImageWidthProp(reader, sessionPropsContent);
		sessionPropsContent.append("session.initImageWidth=" + String.valueOf(initImageWidth));

		reader.close();
		in.close();

		return new ByteArrayInputStream(sessionPropsContent.toString().getBytes());
	}

	private void readAllButInitImageWidthProp(BufferedReader reader, StringBuilder sessionPropsContent)
			throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("session.initImageWidth")) {
				sessionPropsContent.append(line);
				sessionPropsContent.append('\n');
			}
		}
	}
}