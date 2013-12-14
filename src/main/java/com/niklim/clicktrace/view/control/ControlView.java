package com.niklim.clicktrace.view.control;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.screenshot.DeleteScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.EditScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.OpenScreenShotDescriptionOperation;
import com.niklim.clicktrace.controller.operation.screenshot.RefreshScreenShotOperation;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.view.ControlShortcutEnum;

@Singleton
public class ControlView {
	@Inject
	private MainController controller;

	@Inject
	private ToolbarView toolbar;

	@Inject
	private EditScreenShotOperation editScreenShotOperation;
	@Inject
	private RefreshScreenShotOperation refreshScreenShotOperation;
	@Inject
	private DeleteScreenShotOperation deleteScreenShotOperation;
	@Inject
	private OpenScreenShotDescriptionOperation changeScreenShotDescritpionOperation;

	private JPanel panel = new JPanel(new MigLayout("ins 0"));
	private JPanel controlPanel = new JPanel();
	private JComboBox<ScreenShot> imagesComboBox = new JComboBox<ScreenShot>();

	private JButton deleteButton;
	private JButton editButton;
	private JButton refreshButton;
	private JCheckBox checkbox;
	private JButton descriptionButton;

	private JButton firstButton;
	private JButton prevButton;
	private JButton nextButton;
	private JButton lastButton;

	public ControlView() {
		imagesComboBox.setEditable(false);

		controlPanel.setVisible(false);

		firstButton = createButton("First screenshot ", Icons.FIRST_SCREENSHOT,
				ControlShortcutEnum.SHOT_FIRST);
		prevButton = createButton("Previous screenshot ", Icons.PREV_SCREENSHOT,
				ControlShortcutEnum.SHOT_PREV);
		nextButton = createButton("Next screenshot ", Icons.NEXT_SCREENSHOT,
				ControlShortcutEnum.SHOT_NEXT);
		lastButton = createButton("Last screenshot ", Icons.LAST_SCREENSHOT,
				ControlShortcutEnum.SHOT_LAST);

		deleteButton = createButton("delete", "Delete screenshot ", Icons.DELETE_SCREENSHOT,
				ControlShortcutEnum.SHOT_DELETE);
		editButton = createButton("edit", "Open image editor ", Icons.EDIT_SCREENSHOT,
				ControlShortcutEnum.SHOT_EDIT);
		refreshButton = createButton("refresh", "Refresh screenshot ", Icons.REFRESH_SCREENSHOT,
				ControlShortcutEnum.SHOT_REFRESH);

		checkbox = new JCheckBox();
		checkbox.setToolTipText("Select " + ControlShortcutEnum.SHOT_SELECT.text);

		descriptionButton = createButton("description", "Edit screenshot description ",
				Icons.DESCRIPTION_SCREENSHOT, ControlShortcutEnum.SHOT_DESCRIPTION);

		controlPanel.add(new JLabel("Screen shot"));
		controlPanel.add(imagesComboBox);

		controlPanel.add(firstButton);
		controlPanel.add(prevButton);
		controlPanel.add(nextButton);
		controlPanel.add(lastButton);

		controlPanel.add(refreshButton);
		controlPanel.add(editButton);
		controlPanel.add(deleteButton);
		controlPanel.add(descriptionButton);
		controlPanel.add(checkbox);
	}
	
	private JButton createButton(String tooltip, String icon, ControlShortcutEnum shortcut) {
		JButton button = new JButton(new ImageIcon(Icons.createIconImage(icon, tooltip)));
		button.setToolTipText(tooltip + shortcut.text);
		return button;
	}

	private JButton createButton(String label, String tooltip, String icon, ControlShortcutEnum shortcut) {
		JButton button = new JButton(label, new ImageIcon(Icons.createIconImage(icon, label)));
		button.setToolTipText(tooltip + shortcut.text);
		return button;
	}

	@Inject
	public void init() {
		firstButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.showFirstScreenShot();
			}
		});
		prevButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.showPrevScreenShot();
			}
		});
		nextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.showNextScreenShot();
			}
		});
		lastButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.showLastScreenShot();
			}
		});

		imagesComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ScreenShot shot = (ScreenShot) imagesComboBox.getModel().getSelectedItem();
					for (int i = 0; i < imagesComboBox.getModel().getSize(); i++) {
						if (shot.equals(imagesComboBox.getModel().getElementAt(i))) {
							controller.showScreenShot(i);
							break;
						}
					}
				}
			}
		});

		refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		refreshButton.addMouseListener(refreshScreenShotOperation.mouse());

		editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		editButton.addMouseListener(editScreenShotOperation.mouse());

		deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		deleteButton.addMouseListener(deleteScreenShotOperation.mouse());

		checkbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.selectScreenShot(checkbox.isSelected());
			}
		});
		descriptionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		descriptionButton.addMouseListener(changeScreenShotDescritpionOperation.mouse());

		panel.add(toolbar.getToolbar(), "wrap");
		panel.add(controlPanel);
	}

	public void showImagesCombobox(Session session) {
		controlPanel.setVisible(true);
		List<ScreenShot> shots = new ArrayList<ScreenShot>(session.getShots());
		imagesComboBox.setModel(new DefaultComboBoxModel<ScreenShot>(shots
				.toArray(new ScreenShot[0])));
	}

	public Component getComponent() {
		return panel;
	}

	public void hide() {
		controlPanel.setVisible(false);
	}

	public void setActiveScreenShotSelected(boolean selected) {
		checkbox.setSelected(selected);
	}

	public void setActiveScreenShot(ScreenShot shot) {
		imagesComboBox.getModel().setSelectedItem(shot);
		descriptionButton.setText(Strings.isNullOrEmpty(shot.getDescription()) ? "add description"
				: "show description");

		changeNavigationButtonState();
	}

	private void changeNavigationButtonState() {
		int selectedIndex = Math.max(0, imagesComboBox.getSelectedIndex());
		firstButton.setEnabled(selectedIndex != 0);
		prevButton.setEnabled(selectedIndex != 0);
		nextButton.setEnabled(selectedIndex < imagesComboBox.getModel().getSize() - 1);
		lastButton.setEnabled(selectedIndex < imagesComboBox.getModel().getSize() - 1);
	}

}
