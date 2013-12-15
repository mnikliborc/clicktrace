package com.niklim.clicktrace.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.service.exception.HtmlExportAlreadyExistsException;

@Singleton
public class HtmlExportService {

	public void export(Session session, String outputDirPath) throws HtmlExportAlreadyExistsException, IOException {
		if (!outputDirPath.endsWith(File.separator)) {
			outputDirPath += File.separator;
		}

		if (Files.exists(Paths.get(outputDirPath + session.getName()))) {
			throw new HtmlExportAlreadyExistsException("Unable to create '" + session.getName()
					+ "' folder in given directory. Already exists.");
		}

		copyFiles(session, outputDirPath);
		String html = createHtml(session);
		saveHtml(session, outputDirPath, html);
	}

	private void saveHtml(Session session, String outputDirPath, String html) throws IOException {
		try {
			String indexPath = outputDirPath + session.getName() + File.separator
					+ "index.html";
			File index = new File(indexPath);
			index.createNewFile();
			FileOutputStream output = new FileOutputStream(index);
			output.write(html.getBytes());
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void copyFiles(Session session, String outputDirPath) throws IOException {
			createDirectories(session, outputDirPath);
			copyStaticResources(session, outputDirPath);
			copyShots(session, outputDirPath);
	}

	private void copyShots(Session session, String outputDirPath) throws IOException {
		for (ScreenShot shot : session.getShots()) {
			Path source = Paths.get(FileManager.SESSIONS_DIR + session.getName() + File.separator + shot.getFilename());
			Path target = Paths.get(outputDirPath + session.getName() + File.separator + "shots" + File.separator
					+ shot.getFilename());
			Files.copy(source, target);
		}
	}

	private String createHtml(Session session) {
		String mainTemplate = loadTemplate("main.tmpl");
		String shotTemplate = loadTemplate("shot.tmpl");

		StringBuffer shotsHtmlBuffer = new StringBuffer();
		for (ScreenShot shot : session.getShots()) {
			String shotString = shotTemplate.replace("${shot-label}", Strings.nullToEmpty(shot.getLabel()))
					.replace("${shot-description}", Strings.nullToEmpty(shot.getDescription()))
					.replace("${shot-filename}", shot.getFilename());

			shotsHtmlBuffer.append(shotString);
			shotsHtmlBuffer.append("\n\n");
		}
		String html = mainTemplate.replace("${session-name}", session.getName())
				.replace("${session-description}", Strings.nullToEmpty(session.getDescription()))
				.replace("${shots}", shotsHtmlBuffer.toString());
		return html;
	}

	private void copyStaticResources(Session session, String outputDirPath) throws IOException {
		copy("css/clicktrace.css", outputDirPath + session.getName());
		copy("css/lightbox.css", outputDirPath + session.getName());

		copy("img/close.png", outputDirPath + session.getName());
		copy("img/next.png", outputDirPath + session.getName());
		copy("img/prev.png", outputDirPath + session.getName());
		copy("img/loading.gif", outputDirPath + session.getName());

		copy("js/clicktrace.js", outputDirPath + session.getName());
		copy("js/jquery-1.10.2.min.js", outputDirPath + session.getName());
		copy("js/lightbox-2.6.js", outputDirPath + session.getName());
	}

	private void createDirectories(Session session, String outputDirPath) throws IOException {
		Files.createDirectory(Paths.get(outputDirPath + session.getName()));
		Files.createDirectory(Paths.get(outputDirPath + session.getName() + File.separator + "css"));
		Files.createDirectory(Paths.get(outputDirPath + session.getName() + File.separator + "img"));
		Files.createDirectory(Paths.get(outputDirPath + session.getName() + File.separator + "js"));
		Files.createDirectory(Paths.get(outputDirPath + session.getName() + File.separator + "shots"));
	}

	private void copy(String source, String targetBasePath) throws IOException {
		InputStream resource = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("html-export/" + source);
		Files.copy(resource, Paths.get(targetBasePath + File.separator + source));
	}

	private String loadTemplate(String templateName) {
		Scanner scanner = new Scanner(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("html-export" + File.separator + templateName));
		String template = scanner.useDelimiter("\\Z").next();

		scanner.close();
		return template;
	}

}
