package com.niklim.clicktrace.service.export.html;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Files;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.service.ScreenShotUtils;
import com.niklim.clicktrace.service.exception.HtmlExportException;

@Singleton
public class HtmlExportService {

	private static final String HTML_EXPORT_FOLDER = "html-export/";

	@Inject
	public HtmlExportRenderer renderer;

	public void export(Session session, String outputDirPath) throws HtmlExportException, IOException {
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

		copyFiles(session, outputDirPath);
		String html = renderer.renderHtml(session, HTML_EXPORT_FOLDER);
		saveHtml(session, outputDirPath, html);
	}

	private void saveHtml(Session session, String outputDirPath, String html) throws IOException {
		try {
			String indexPath = outputDirPath + session.getName() + File.separator + "clicktrace.html";
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

	private void copyStaticResources(Session session, String outputDirPath) throws IOException {
		copy("resources/clicktrace.css", outputDirPath + session.getName());
		copy("resources/clicktrace.js", outputDirPath + session.getName());
		copy("resources/footer.png", outputDirPath + session.getName());
	}

	private void createDirectories(Session session, String outputDirPath) throws IOException {
		Files.createDirectory(outputDirPath + session.getName());
		Files.createDirectory(outputDirPath + session.getName() + File.separator + "resources");
		Files.createDirectory(outputDirPath + session.getName() + File.separator + "shots");
	}

	private void copy(String source, String targetBasePath) throws IOException {
		InputStream resource = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(HTML_EXPORT_FOLDER + source);
		Files.copy(resource, targetBasePath + File.separator + source);
	}

}
