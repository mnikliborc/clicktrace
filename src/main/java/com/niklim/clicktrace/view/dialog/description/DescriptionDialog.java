package com.niklim.clicktrace.view.dialog.description;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.service.MarkdownService;
import com.niklim.clicktrace.view.TextComponentHistory;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

/**
 * Lets the user input Markdown. Offers HTML preview.
 */
@Singleton
public class DescriptionDialog extends AbstractDialog {
	private static final String MAIN_PANEL_CONTENT_LAYOUT = "push, grow, w 600, h 300, wrap";
	private static final String MAIN_PANEL_LAYOUT = "grow, push,  wrap";
	private JPanel editPanel;
	private JPanel previewPanel;
	private JPanel controlPanel;
	
	private JTextArea description;
	private TextComponentHistory history;
	private DescriptionDialogCallback callback;
	
	private JCheckBox previewCheckbox;
	
	@Inject
	private MarkdownService markdownParser;
	
	private EditPreviewComponentToggle editPreviewToggle = new EditPreviewComponentToggle();
	
	/**
	 * Switches between EDIT and PREVIEW mode.
	 */
	private class EditPreviewComponentToggle implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toggle();
		}
		
		public void toggle() {
			if (previewCheckbox.isSelected()) {
				previewPanel.removeAll();
				Component htmlComponent = markdownParser.toHtmlPanel(description.getText(),
						editPanel.getSize());
				previewPanel.add(htmlComponent, MAIN_PANEL_CONTENT_LAYOUT);
				replacePanels(editPanel, previewPanel);
			} else {
				replacePanels(previewPanel, editPanel);
			}
		}
		
		private void replacePanels(JPanel remove, JPanel add) {
			dialog.remove(remove);
			dialog.remove(controlPanel);
			dialog.add(add, MAIN_PANEL_LAYOUT);
			dialog.add(controlPanel);
			dialog.pack();
			dialog.repaint();
		}
		
	}
	
	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]"));
		
		description = new JTextArea();
		initTextWrapping(description);
		history = new TextComponentHistory(description);
		
		createEditPanel();
		createPreviewPanel();
		
		dialog.add(editPanel, MAIN_PANEL_LAYOUT);
		
		JCheckBox previewCheckbox = createPreviewCheckbox();
		controlPanel = createControlPanel("Save", previewCheckbox);
		dialog.add(controlPanel);
		
		createListeners();
		postInit();
	}
	
	private void createPreviewPanel() {
		previewPanel = new JPanel(new MigLayout("insets 0", "[fill]"));
	}
	
	private void createEditPanel() {
		editPanel = new JPanel(new MigLayout("insets 0", "[fill]"));
		editPanel.add(new JScrollPane(description), MAIN_PANEL_CONTENT_LAYOUT);
	}
	
	private JCheckBox createPreviewCheckbox() {
		previewCheckbox = new JCheckBox("preview");
		previewCheckbox.setToolTipText("show provided Markdown text in HTML");
		
		previewCheckbox.addActionListener(editPreviewToggle);
		
		return previewCheckbox;
	}
	
	public void createListeners() {
		description.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
			}
			
			@Override
			public void focusLost(FocusEvent arg0) {
				history.store();
			}
		});
		
		description.addKeyListener(new TextComponentHistory.DefaultKeyAdapter(history));
		okButton.setToolTipText("[Ctrl+S]");
		
		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	@Override
	protected void okAction() {
		callback.setText(description.getText());
		close();
	}
	
	public void open(DescriptionDialogCallback callback) {
		this.callback = callback;
		
		dialog.setTitle(callback.getTitle());
		showEditPanel();
		
		description.setText(callback.getText());
		description.grabFocus();
		
		resetHistory(callback.getText());
		
		center();
		dialog.setVisible(true);
	}
	
	private void showEditPanel() {
		previewCheckbox.setSelected(false);
		editPreviewToggle.toggle();
	}
	
	private void resetHistory(String initialText) {
		history.reset(initialText);
	}
	
	public void close() {
		dialog.setVisible(false);
	}
	
}
