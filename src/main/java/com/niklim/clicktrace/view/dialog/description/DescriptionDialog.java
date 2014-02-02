package com.niklim.clicktrace.view.dialog.description;

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
import com.niklim.clicktrace.view.TextComponentHistory;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

/**
 * Lets the user input Markdown. Offers HTML preview.
 */
@Singleton
public class DescriptionDialog extends AbstractDialog {
	private static final String MAIN_PANEL_CONTENT_LAYOUT = "push, grow, w 600, h 300, wrap";
	private static final String MAIN_PANEL_LAYOUT = "grow, push,  wrap";
	private JPanel descriptionPlaceholder;
	private JPanel controlPanel;
	
	private JTextArea description;
	private TextComponentHistory history;
	private DescriptionDialogCallback callback;
	
	@Inject
	private EditPreviewDescriptionToggle editPreviewToggle;
	
	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]"));
		
		createPlaceholderPanelWithDescription();
		
		dialog.add(descriptionPlaceholder, MAIN_PANEL_LAYOUT);
		
		JCheckBox previewCheckbox = createPreviewComponent();
		controlPanel = createControlPanel("Save", previewCheckbox);
		dialog.add(controlPanel);
		
		createListeners();
		postInit();
	}
	
	private JCheckBox createPreviewComponent() {
		editPreviewToggle.init(dialog, descriptionPlaceholder, description,
				MAIN_PANEL_CONTENT_LAYOUT);
		return editPreviewToggle.getCheckBox();
	}
	
	private void createPlaceholderPanelWithDescription() {
		description = new JTextArea();
		initTextWrapping(description);
		history = new TextComponentHistory(description);
		
		descriptionPlaceholder = new JPanel(new MigLayout("insets 0", "[fill]"));
		descriptionPlaceholder.add(new JScrollPane(description), MAIN_PANEL_CONTENT_LAYOUT);
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
		editPreviewToggle.reset();
	}
	
	private void resetHistory(String initialText) {
		history.reset(initialText);
	}
	
	public void close() {
		dialog.setVisible(false);
	}
	
}
