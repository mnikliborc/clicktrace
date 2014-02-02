package com.niklim.clicktrace.view.dialog.description;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import com.google.inject.Inject;
import com.niklim.clicktrace.service.MarkdownService;

/**
 * Switches between EDIT and PREVIEW mode on its {@link JCheckBox} change. In
 * PREVIEW mode takes text from {@link JTextArea} and uses
 * {@link MarkdownService} to render HTML from Markdown. Displays it in
 * {@link JPanel} placeholder. In EDIT mode gets back to {@link JTextArea}
 * edition.
 */
public class EditPreviewDescriptionToggle {
	
	private JDialog dialog;
	private JPanel descriptionPlaceholder;
	private JTextArea textarea;
	private String migLayoutConstraints;
	
	private JCheckBox previewCheckbox;
	
	@Inject
	private MarkdownService markdownParser;
	
	public void init(JDialog dialog, JPanel descriptionPlaceholder, JTextArea description,
			String migLayoutConstraints) {
		this.dialog = dialog;
		this.descriptionPlaceholder = descriptionPlaceholder;
		this.textarea = description;
		this.migLayoutConstraints = migLayoutConstraints;
		
		createPreviewCheckbox();
		initListener();
	}
	
	protected void createPreviewCheckbox() {
		previewCheckbox = new JCheckBox("preview");
		previewCheckbox.setToolTipText("show HTML of provided Markdown text");
		
		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previewCheckbox.setSelected(!previewCheckbox.isSelected());
				toggle();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	private void initListener() {
		previewCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				toggle();
			}
		});
	}
	
	public void toggle() {
		if (previewCheckbox.isSelected()) {
			descriptionPlaceholder.removeAll();
			Component htmlComponent = markdownParser.toHtmlPanel(textarea.getText());
			descriptionPlaceholder.add(htmlComponent, migLayoutConstraints);
		} else {
			descriptionPlaceholder.removeAll();
			descriptionPlaceholder.add(new JScrollPane(textarea), migLayoutConstraints);
		}
		dialog.pack();
		dialog.repaint();
	}
	
	public JCheckBox getCheckBox() {
		return previewCheckbox;
	}
	
	public void reset() {
		previewCheckbox.setSelected(false);
		toggle();
	}
	
}
