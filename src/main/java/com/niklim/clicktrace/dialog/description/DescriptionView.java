package com.niklim.clicktrace.dialog.description;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import com.niklim.clicktrace.dialog.AbstractDialogView;

public class DescriptionView extends AbstractDialogView {
	public static final String MAIN_PANEL_CONTENT_LAYOUT = "push, grow, w 600, h 300, wrap";
	private static final String MAIN_PANEL_LAYOUT = "grow, push,  wrap";
	JPanel descriptionPlaceholder;

	JTextArea description;
	JCheckBox previewCheckbox;

	public DescriptionView() {
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]"));

		createPlaceholderPanelWithDescription();

		dialog.add(descriptionPlaceholder, MAIN_PANEL_LAYOUT);

		previewCheckbox = EditPreviewDescriptionToggle.createPreviewCheckbox();
		dialog.add(createControlPanel("Save", previewCheckbox));
	}

	private void createPlaceholderPanelWithDescription() {
		description = new JTextArea();

		descriptionPlaceholder = new JPanel(new MigLayout("insets 0", "[fill]"));
		descriptionPlaceholder.add(new JScrollPane(description), MAIN_PANEL_CONTENT_LAYOUT);
	}
}
