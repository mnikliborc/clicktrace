package com.niklim.clicktrace.dialog.description;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

import com.niklim.clicktrace.props.UserProperties.MarkupSyntax;
import com.niklim.clicktrace.service.MarkupParser;

/**
 * Switches between EDIT and PREVIEW mode on its {@link JCheckBox} change. In
 * PREVIEW mode takes text from {@link JTextArea} and uses {@link MarkupParser}
 * to render HTML from Markdown. Displays it in {@link JPanel} placeholder. In
 * EDIT mode gets back to {@link JTextArea} edition.
 */
public class EditPreviewDescriptionToggle {
	private static final String XML_DOC_TYPE = "<?xml version=\"1.0\"?><!DOCTYPE some_name [<!ENTITY nbsp \"&#160;\">]> ";

	private JDialog dialog;
	private JPanel descriptionPlaceholder;
	private JTextArea textarea;
	private String migLayoutConstraints;

	private JCheckBox previewCheckbox;

	MarkupParser markupParser;

	public EditPreviewDescriptionToggle(JDialog dialog, JCheckBox previewCheckbox, JPanel descriptionPlaceholder,
			JTextArea description, String migLayoutConstraints) {
		this.dialog = dialog;
		this.descriptionPlaceholder = descriptionPlaceholder;
		this.textarea = description;
		this.migLayoutConstraints = migLayoutConstraints;
		this.previewCheckbox = previewCheckbox;

		createListeners();
	}

	public static JCheckBox createPreviewCheckbox() {
		JCheckBox previewCheckbox = new JCheckBox("preview");
		previewCheckbox.setToolTipText("show HTML of provided Markdown text");
		return previewCheckbox;
	}

	private void createListeners() {
		previewCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				toggle();
			}
		});

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previewCheckbox.setSelected(!previewCheckbox.isSelected());
				toggle();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void toggle() {
		if (previewCheckbox.isSelected()) {
			descriptionPlaceholder.removeAll();
			Component htmlComponent = toHtmlPanel(textarea.getText());
			descriptionPlaceholder.add(htmlComponent, migLayoutConstraints);
		} else {
			descriptionPlaceholder.removeAll();
			descriptionPlaceholder.add(new JScrollPane(textarea), migLayoutConstraints);
		}
		dialog.pack();
		dialog.repaint();
	}

	private Component toHtmlPanel(String markup) {
		return flyingsaucer(markupParser.toHtml(markup));
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

	public void reset(MarkupSyntax syntax) {
		markupParser = new MarkupParser(syntax);
	}

}
