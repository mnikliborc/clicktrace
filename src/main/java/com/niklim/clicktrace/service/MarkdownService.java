package com.niklim.clicktrace.service;

import java.awt.Component;
import java.io.ByteArrayInputStream;

import javax.swing.ScrollPaneConstants;

import org.pegdown.PegDownProcessor;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * Transforms Markdown to HTML.
 */
public class MarkdownService {
	
	public String toHtml(String markdown) {
		return new PegDownProcessor().markdownToHtml(markdown);
	}
	
	public Component toHtmlPanel(String markdown) {
		return flyingsaucer(markdown);
	}
	
	private Component flyingsaucer(String markdown) {
		String html = toHtml(markdown);
		
		html = wrapWithRootElement(html);
		
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
