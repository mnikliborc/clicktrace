package com.niklim.clicktrace.service.export.html;

import java.util.Scanner;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.MarkupParser;

public class HtmlExportRenderer {

	@Inject
	private UserProperties props;

	public String renderHtml(Session session, String htmlExportFolder, int initImageWidth) {
		String mainTemplate = loadTemplate("main.tmpl", htmlExportFolder);
		String shotTemplate = loadTemplate("shot.tmpl", htmlExportFolder);
		String linksTemplate = loadTemplate("links.tmpl", htmlExportFolder);

		MarkupParser markupParser = new MarkupParser(props.getMarkupSyntax());
		String shotsHtml = renderShots(session, shotTemplate, markupParser);
		String linksHtml = renderLinks(session, linksTemplate);
		String html = renderMain(session, mainTemplate, shotsHtml, linksHtml, initImageWidth, markupParser);
		return html;
	}

	private String renderMain(Session session, String mainTemplate, String shotsHtml, String linksHtml,
			int initImageWidth, MarkupParser markupParser) {
		String html = mainTemplate.replace("${encoding}", System.getProperty("file.encoding", "UTF-8"))
				.replace("${init-image-width}", String.valueOf(initImageWidth))
				.replace("${init-image-width}", String.valueOf(initImageWidth))
				.replace("${session-name}", session.getName())
				.replace("${session-description}", markupParser.toHtml(Strings.nullToEmpty(session.getDescription())))
				.replace("${shots}", shotsHtml).replace("${links}", linksHtml);
		return html;
	}

	private String renderShots(Session session, String shotTemplate, MarkupParser markupParser) {
		StringBuffer buffer = new StringBuffer();
		int index = 1;
		for (ScreenShot shot : session.getShots()) {
			String label = String.valueOf(index) + ". " + Strings.nullToEmpty(shot.getLabel());
			String shotString = shotTemplate.replace("${shot-label}", label)
					.replace("${shot-description}", markupParser.toHtml(Strings.nullToEmpty(shot.getDescription())))
					.replaceAll("\\$\\{shot-filename\\}", shot.getFilename());

			buffer.append(shotString);
			buffer.append("\n\n");
			index++;
		}
		return buffer.toString();
	}

	private String renderLinks(Session session, String linksTemplate) {
		StringBuffer buffer = new StringBuffer();
		int index = 1;
		for (ScreenShot shot : session.getShots()) {
			StringBuffer linkText = new StringBuffer();
			linkText.append(index);
			linkText.append(". ");

			if (!Strings.isNullOrEmpty(shot.getLabel())) {
				linkText.append(shot.getLabel());
			}
			String link = linksTemplate.replace("${link-text}", linkText.toString()).replace("${link-href}",
					shot.getFilename());

			buffer.append(link);
			index++;
		}
		return buffer.toString();
	}

	private String loadTemplate(String templateName, String htmlExportFolder) {
		Scanner scanner = new Scanner(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(htmlExportFolder + templateName));
		String template = scanner.useDelimiter("\\Z").next();

		scanner.close();
		return template;
	}

}
