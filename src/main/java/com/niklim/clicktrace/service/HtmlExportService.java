package com.niklim.clicktrace.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Files;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.service.exception.HtmlExportException;

@Singleton
public class HtmlExportService {

	public void export(Session session, String outputDirPath, boolean simpleHtml) throws HtmlExportException,
			IOException {
		if (!outputDirPath.endsWith(File.separator)) {
			outputDirPath += File.separator;
		}

		if (!Files.exists(outputDirPath)) {
			throw new HtmlExportException(InfoMsgs.HTML_EXPORT_FOLDER_NOT_EXISTS);
		}
		if (Files.exists(outputDirPath + session.getName())) {
			throw new HtmlExportException("Unable to create '" + session.getName()
					+ "' folder in given directory. Already exists.");
		}

		copyFiles(session, outputDirPath, simpleHtml);
		String html = createHtml(session, simpleHtml);
		saveHtml(session, outputDirPath, html);
	}

	private void saveHtml(Session session, String outputDirPath, String html) throws IOException {
		try {
			String indexPath = outputDirPath + session.getName() + File.separator + "index.html";
			File index = new File(indexPath);
			index.createNewFile();
			FileOutputStream output = new FileOutputStream(index);
			output.write(html.getBytes());
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void copyFiles(Session session, String outputDirPath, boolean simpleHtml) throws IOException {
		createDirectories(session, outputDirPath, simpleHtml);
		copyStaticResources(session, outputDirPath, simpleHtml);
		copyShots(session, outputDirPath);
	}

	private void copyShots(Session session, String outputDirPath) throws IOException {
		for (ScreenShot shot : session.getShots()) {
			String fromPath = FileManager.SESSIONS_DIR + session.getName() + File.separator + shot.getFilename();
			String toPath = outputDirPath + session.getName() + File.separator + "shots" + File.separator
					+ shot.getFilename();
			Files.copy(fromPath, toPath);

			drawClicks(shot, toPath);
		}
	}

	private void drawClicks(ScreenShot shot, String toPath) throws IOException {
		BufferedImage withClicks = ScreenShotUtils.markClicks(ImageIO.read(new File(toPath)), shot.getClicks());
		File outputfile = new File(toPath);
		ImageIO.write(withClicks, shot.getFilename().substring(shot.getFilename().lastIndexOf(".") + 1), outputfile);
	}

	private String createHtml(Session session, boolean simpleHtml) {
		String mainTemplate = loadTemplate(simpleHtml ? "main-simple.tmpl" : "main-lightbox.tmpl");
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

	private void copyStaticResources(Session session, String outputDirPath, boolean simpleHtml) throws IOException {
		copy("css/clicktrace.css", outputDirPath + session.getName());

		if (!simpleHtml) {
			copy("css/lightbox.css", outputDirPath + session.getName());
			copy("img/close.png", outputDirPath + session.getName());
			copy("img/next.png", outputDirPath + session.getName());
			copy("img/prev.png", outputDirPath + session.getName());
			copy("img/loading.gif", outputDirPath + session.getName());

			copy("js/clicktrace.js", outputDirPath + session.getName());
			copy("js/jquery-1.10.2.min.js", outputDirPath + session.getName());
			copy("js/lightbox-2.6.js", outputDirPath + session.getName());
		}
	}

	private void createDirectories(Session session, String outputDirPath, boolean simpleHtml) throws IOException {
		Files.createDirectory(outputDirPath + session.getName());
		Files.createDirectory(outputDirPath + session.getName() + File.separator + "css");
		Files.createDirectory(outputDirPath + session.getName() + File.separator + "shots");
		if (!simpleHtml) {
			Files.createDirectory(outputDirPath + session.getName() + File.separator + "img");
			Files.createDirectory(outputDirPath + session.getName() + File.separator + "js");
		}
	}

	private void copy(String source, String targetBasePath) throws IOException {
		InputStream resource = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("html-export/" + source);
		Files.copy(resource, targetBasePath + File.separator + source);
	}

	private String loadTemplate(String templateName) {
		Scanner scanner = new Scanner(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("html-export" + File.separator + templateName));
		String template = scanner.useDelimiter("\\Z").next();

		scanner.close();
		return template;
	}

}
