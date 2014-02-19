package com.niklim.clicktrace.service;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.StringReader;

import javax.swing.ScrollPaneConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xwiki.component.embed.EmbeddableComponentManager;
import org.xwiki.rendering.converter.Converter;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.rendering.syntax.Syntax;

import com.google.inject.Inject;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.MarkupSyntax;

/**
 * Transforms Markup to HTML. See {@link MarkupSyntax} for supported syntax.
 */
public class MarkupService {
	private static final String XML_DOC_TYPE = "<?xml version=\"1.0\"?><!DOCTYPE some_name [<!ENTITY nbsp \"&#160;\">]> ";
	private static final Logger log = LoggerFactory.getLogger(MarkupService.class);
	@Inject
	private UserProperties userProperties;

	EmbeddableComponentManager componentManager = new EmbeddableComponentManager();

	@Inject
	public void init() {
		componentManager.initialize(this.getClass().getClassLoader());
	}

	public String toHtml(String markup) {
		if (userProperties.getMarkupSyntax() == MarkupSyntax.MARKDOWN) {
			return convertMarkup(markup, Syntax.MARKDOWN_1_0);
		} else if (userProperties.getMarkupSyntax() == MarkupSyntax.CONFLUENCE) {
			return convertMarkup(markup, Syntax.CONFLUENCE_1_0);
		} else {
			log.error("Unknown Markup syntax '{}'", userProperties.getMarkupSyntax());
			return markup;
		}
	}

	public String convertMarkup(String markup, Syntax sourceSyntax) {
		WikiPrinter printer = new DefaultWikiPrinter();
		try {
			Converter converter = componentManager.getInstance(Converter.class);
			converter.convert(new StringReader(markup), sourceSyntax, Syntax.XHTML_1_0, printer);
		} catch (Exception e) {
			log.error(sourceSyntax.getType().getName() + " markup conversion error.", e);
			return markup;
		}
		return printer.toString();
	}

	public Component toHtmlPanel(String markup) {
		return flyingsaucer(toHtml(markup));
	}

	private Component flyingsaucer(String html) {
		html = XML_DOC_TYPE + wrapWithRootElement(html);

		// Create a JPanel subclass to render the page
		XHTMLPanel panel = new XHTMLPanel();

		// Set the XHTML document to render. We use the simplest form
		// of the API call, which uses a File reference. There
		// are a variety of overloads for setDocument().
		try {
			panel.setDocument(new ByteArrayInputStream(html.getBytes()), null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Put our panel in a scrolling pane. You can use
		// a regular JScrollPane here, or our FSScrollPane.
		// FSScrollPane is already set up to move the correct
		// amount when scrolling 1 line or 1 page
		FSScrollPane scroll = new FSScrollPane(panel);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		return scroll;
	}

	private String wrapWithRootElement(String html) {
		return "<div>" + html + "</div>";
	}
}
