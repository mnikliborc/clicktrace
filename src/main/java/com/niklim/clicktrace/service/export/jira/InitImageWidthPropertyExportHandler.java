package com.niklim.clicktrace.service.export.jira;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.FileManager;

public class InitImageWidthPropertyExportHandler {
	public static void handle(String sessionsDirPath, Session session, Integer imageWidth)
			throws IOException {
		String sessionPropsFilePath = sessionsDirPath + session.getName() + File.separator
				+ FileManager.SESSION_PROPS_FILENAME;
		File sessionPropsFile = new File(sessionPropsFilePath);
		if (sessionPropsFile.exists()) {
			addInitImageWidthSessionProperty(sessionPropsFile, imageWidth);
		}
	}

	private static void addInitImageWidthSessionProperty(File sessionPropsFile, Integer initImageWidth)
			throws IOException {
		FileInputStream in = new FileInputStream(sessionPropsFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sessionPropsContent = new StringBuilder();

		readAllButInitImageWidthProp(reader, sessionPropsContent);
		sessionPropsContent.append(UserProperties.EXPORT_IMAGE_WIDTH + "=" + initImageWidth);

		reader.close();
		in.close();

		FileOutputStream out = new FileOutputStream(sessionPropsFile, false);
		out.write(sessionPropsContent.toString().getBytes());
		out.close();
	}

	private static void readAllButInitImageWidthProp(BufferedReader reader, StringBuilder sessionPropsContent)
			throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith(UserProperties.EXPORT_IMAGE_WIDTH)) {
				sessionPropsContent.append(line);
				sessionPropsContent.append('\n');
			}
		}
	}
}
