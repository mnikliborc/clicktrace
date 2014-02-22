package com.niklim.clicktrace.dialog;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.niklim.clicktrace.dialog.description.EditPreviewDescriptionToggle;

public class NewSessionView extends AbstractDialogView {
	static final String MAIN_PANEL_CONTENT_LAYOUT = "push, grow, w 600, h 300, wrap";
	private static final String MAIN_PANEL_LAYOUT = "grow, push,  wrap";

	JPanel descriptionPlaceholder;
	JTextField sessionName;
	JTextArea sessionDescription;
	JCheckBox previewCheckbox;

	public NewSessionView() {
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]"));
		dialog.setTitle("New session");

		sessionName = new JTextField();
		sessionName.setName("name");

		createPlaceholderPanelWithDescription();

		dialog.add(new JLabel("Name"), "wrap");
		dialog.add(sessionName, "wrap, grow");

		dialog.add(new JLabel("Description"), "wrap");
		dialog.add(descriptionPlaceholder, MAIN_PANEL_LAYOUT);

		previewCheckbox = EditPreviewDescriptionToggle.createPreviewCheckbox();
		dialog.add(createControlPanel("Create", previewCheckbox));
	}

	private void createPlaceholderPanelWithDescription() {
		sessionDescription = new JTextArea();
		sessionDescription.setName("description");

		descriptionPlaceholder = new JPanel(new MigLayout("insets 0", "[fill]"));
		descriptionPlaceholder.add(new JScrollPane(sessionDescription), MAIN_PANEL_CONTENT_LAYOUT);
	}

}
