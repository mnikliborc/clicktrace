package com.niklim.clicktrace.service;

import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xwiki.component.embed.EmbeddableComponentManager;
import org.xwiki.rendering.converter.Converter;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.rendering.syntax.Syntax;

import com.niklim.clicktrace.props.UserProperties.MarkupSyntax;

/**
 * Transforms Markup to HTML. See {@link MarkupSyntax} for supported syntax.
 */
public class MarkupParser {

	private static final Logger log = LoggerFactory.getLogger(MarkupParser.class);

	private static EmbeddableComponentManager componentManager = new EmbeddableComponentManager();

	static {
		componentManager.initialize(Thread.currentThread().getContextClassLoader());
	}

	private MarkupSyntax syntax;

	public MarkupParser(MarkupSyntax syntax) {
		this.syntax = syntax;
	}

	public String toHtml(String markup) {
		if (syntax == MarkupSyntax.MARKDOWN) {
			return convertMarkup(markup, Syntax.MARKDOWN_1_0);
		} else if (syntax == MarkupSyntax.CONFLUENCE) {
			return convertMarkup(markup, Syntax.CONFLUENCE_1_0);
		} else {
			log.error("Unknown Markup syntax '{}'", syntax);
			return markup;
		}
	}

	private String convertMarkup(String markup, Syntax sourceSyntax) {
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

}
