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
import com.niklim.clicktrace.view.Buttons;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

/**
 * Controls for screenshot view.
 */
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
	private JComboBox imagesComboBox = new JComboBox();

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

		firstButton = Buttons.create("First screenshot ", Icons.FIRST_SCREENSHOT, OperationsShortcutEnum.SHOT_FIRST);
		prevButton = Buttons.create("Previous screenshot ", Icons.PREV_SCREENSHOT, OperationsShortcutEnum.SHOT_PREV);
		nextButton = Buttons.create("Next screenshot ", Icons.NEXT_SCREENSHOT, OperationsShortcutEnum.SHOT_NEXT);
		lastButton = Buttons.create("Last screenshot ", Icons.LAST_SCREENSHOT, OperationsShortcutEnum.SHOT_LAST);

		deleteButton = Buttons.create("delete", "Delete screenshot ", Icons.DELETE_SCREENSHOT,
				OperationsShortcutEnum.SHOT_DELETE);
		editButton = Buttons.create("edit", "Open image editor ", Icons.EDIT_SCREENSHOT,
				OperationsShortcutEnum.SHOT_EDIT);
		refreshButton = Buttons.create("refresh", "Refresh screenshot ", Icons.REFRESH_SCREENSHOT,
				OperationsShortcutEnum.SHOT_REFRESH);

		checkbox = new JCheckBox();
		checkbox.setToolTipText("Select " + OperationsShortcutEnum.SHOT_SELECT.text);

		descriptionButton = Buttons.create("description", "Edit screenshot description ", Icons.DESCRIPTION_SCREENSHOT,
				OperationsShortcutEnum.SHOT_DESCRIPTION);

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
		imagesComboBox.setModel(new DefaultComboBoxModel(shots.toArray(new ScreenShot[0])));
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
		descriptionButton
				.setText(Strings.isNullOrEmpty(shot.getDescription()) ? "add description" : "show description");

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
