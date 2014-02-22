package com.niklim.clicktrace.dialog.settings;

import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.Lists;
import com.niklim.clicktrace.capture.voter.LineVoter.ChangeSensitivity;
import com.niklim.clicktrace.props.UserProperties.MarkupSyntax;

public class AdvancedSettingsComponent {
	private JCheckBox advanced;

	private JRadioButton markdownSyntaxRadio;
	private JRadioButton confluenceSyntaxRadio;
	private ButtonGroup markupSyntaxRadioGroup;

	private JRadioButton changeSensitivityHighRadio;
	private JRadioButton changeSensitivityNormalRadio;
	private JRadioButton changeSensitivityLowRadio;
	private ButtonGroup changeSensitivityRadioGroup;

	private JDialog settingsDialog;

	private List<Component> components = Lists.newArrayList();

	public AdvancedSettingsComponent(final JDialog settingsDialog) {
		this.settingsDialog = settingsDialog;
		createCheckbox();

		settingsDialog.add(advanced, "wrap");

		createMarkupSyntaxPanel();
		createCaptureChangeDetectionLevelPanel();

		advanced.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				toggleVisibility();

				settingsDialog.pack();
			}
		});

		toggleVisibility();
	}

	private void createCheckbox() {
		advanced = new JCheckBox("Advanced");
		Font font = advanced.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		advanced.setFont(boldFont);
	}

	private void toggleVisibility() {
		for (Component c : components) {
			c.setVisible(advanced.isSelected());
		}
	}

	public void init(ChangeSensitivity changeSensitivity, MarkupSyntax markupSyntax) {
		initChangeSensitivity(changeSensitivity);
		initMarkupSyntax(markupSyntax);
	}

	private void initMarkupSyntax(MarkupSyntax markupSyntax) {
		if (markupSyntax == MarkupSyntax.MARKDOWN) {
			markupSyntaxRadioGroup.setSelected(markdownSyntaxRadio.getModel(), true);
		} else if (markupSyntax == MarkupSyntax.CONFLUENCE) {
			markupSyntaxRadioGroup.setSelected(confluenceSyntaxRadio.getModel(), true);
		}
	}

	private void initChangeSensitivity(ChangeSensitivity changeSensitivity) {
		if (changeSensitivity == ChangeSensitivity.HIGH) {
			changeSensitivityRadioGroup.setSelected(changeSensitivityHighRadio.getModel(), true);
		} else if (changeSensitivity == ChangeSensitivity.NORMAL) {
			changeSensitivityRadioGroup.setSelected(changeSensitivityNormalRadio.getModel(), true);
		} else if (changeSensitivity == ChangeSensitivity.LOW) {
			changeSensitivityRadioGroup.setSelected(changeSensitivityLowRadio.getModel(), true);
		}
	}

	public ChangeSensitivity getChangeSensitivity() {
		if (changeSensitivityRadioGroup.isSelected(changeSensitivityHighRadio.getModel())) {
			return ChangeSensitivity.HIGH;
		} else if (changeSensitivityRadioGroup.isSelected(changeSensitivityNormalRadio.getModel())) {
			return ChangeSensitivity.NORMAL;
		} else if (changeSensitivityRadioGroup.isSelected(changeSensitivityLowRadio.getModel())) {
			return ChangeSensitivity.LOW;
		} else {
			return null;
		}
	}

	public MarkupSyntax getMarkupSyntax() {
		if (markupSyntaxRadioGroup.isSelected(markdownSyntaxRadio.getModel())) {
			return MarkupSyntax.MARKDOWN;
		} else if (markupSyntaxRadioGroup.isSelected(confluenceSyntaxRadio.getModel())) {
			return MarkupSyntax.CONFLUENCE;
		} else {
			return null;
		}
	}

	private void createMarkupSyntaxPanel() {
		markdownSyntaxRadio = new JRadioButton("Markdown");
		confluenceSyntaxRadio = new JRadioButton("Confluence/JIRA");

		JLabel label = new JLabel("Markup syntax");
		settingsDialog.add(label);
		JPanel radioPanel = new JPanel(new MigLayout("fill, insets 0"));

		radioPanel.add(confluenceSyntaxRadio);
		radioPanel.add(markdownSyntaxRadio);
		settingsDialog.add(radioPanel, "align l, wrap");

		markupSyntaxRadioGroup = new ButtonGroup();
		markupSyntaxRadioGroup.add(markdownSyntaxRadio);
		markupSyntaxRadioGroup.add(confluenceSyntaxRadio);

		components.add(label);
		components.add(markdownSyntaxRadio);
		components.add(confluenceSyntaxRadio);
	}

	private void createCaptureChangeDetectionLevelPanel() {
		changeSensitivityHighRadio = new JRadioButton("high");
		changeSensitivityHighRadio.setToolTipText("detect blinking text cursor");
		changeSensitivityNormalRadio = new JRadioButton("normal");
		changeSensitivityNormalRadio.setToolTipText("default sensitivity");
		changeSensitivityLowRadio = new JRadioButton("low");

		JLabel label = new JLabel("Change sensitivity");
		settingsDialog.add(label);
		JPanel radioPanel = new JPanel(new MigLayout("fill, insets 0"));

		radioPanel.add(changeSensitivityHighRadio);
		radioPanel.add(changeSensitivityNormalRadio);
		radioPanel.add(changeSensitivityLowRadio);
		settingsDialog.add(radioPanel, "grow, wrap");

		changeSensitivityRadioGroup = new ButtonGroup();
		changeSensitivityRadioGroup.add(changeSensitivityHighRadio);
		changeSensitivityRadioGroup.add(changeSensitivityNormalRadio);
		changeSensitivityRadioGroup.add(changeSensitivityLowRadio);

		components.add(label);
		components.add(changeSensitivityHighRadio);
		components.add(changeSensitivityNormalRadio);
		components.add(changeSensitivityLowRadio);
	}

}
