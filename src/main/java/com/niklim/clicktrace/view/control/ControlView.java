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
import com.niklim.clicktrace.OSUtils;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.NavigationController;
import com.niklim.clicktrace.controller.operation.screenshot.ChangeScreenShotDescriptionOperation;
import com.niklim.clicktrace.controller.operation.screenshot.DeleteScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.EditScreenShotOperation;
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
	private MainController mainController;

	@Inject
	private NavigationController navigationController;

	@Inject
	private ToolbarView toolbar;

	@Inject
	private EditScreenShotOperation editScreenShotOperation;
	@Inject
	private RefreshScreenShotOperation refreshScreenShotOperation;
	@Inject
	private DeleteScreenShotOperation deleteScreenShotOperation;
	@Inject
	private ChangeScreenShotDescriptionOperation changeScreenShotDescritpionOperation;

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

		createNavigationButtons();

		deleteButton = Buttons.create("delete", "Delete screenshot ", Icons.SCREENSHOT_DELETE,
				OperationsShortcutEnum.SHOT_DELETE);
		editButton = Buttons.create("edit", "Open image editor ", Icons.SCREENSHOT_EDIT,
				OperationsShortcutEnum.SHOT_EDIT);
		refreshButton = Buttons.create("refresh", "Refresh screenshot ", Icons.SCREENSHOT_REFRESH,
				OperationsShortcutEnum.SHOT_REFRESH);

		checkbox = new JCheckBox();
		checkbox.setToolTipText("Select screenshot " + OperationsShortcutEnum.SHOT_SELECT.text);

		descriptionButton = Buttons.create("description", "Edit screenshot description ", Icons.SCREENSHOT_DESCRIPTION,
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

	private void createNavigationButtons() {
		firstButton = Buttons.create("First screenshot ", Icons.SCREENSHOT_FIRST, OperationsShortcutEnum.SHOT_FIRST);
		lastButton = Buttons.create("Last screenshot ", Icons.SCREENSHOT_LAST, OperationsShortcutEnum.SHOT_LAST);

		OperationsShortcutEnum nextShorcut = OSUtils.isOnMac() ? OperationsShortcutEnum.SHOT_NEXT_MAC
				: OperationsShortcutEnum.SHOT_NEXT;
		OperationsShortcutEnum prevShorcut = OSUtils.isOnMac() ? OperationsShortcutEnum.SHOT_PREV_MAC
				: OperationsShortcutEnum.SHOT_PREV;
		nextButton = Buttons.create("Next screenshot ", Icons.SCREENSHOT_NEXT, nextShorcut);
		prevButton = Buttons.create("Previous screenshot ", Icons.SCREENSHOT_PREV, prevShorcut);
	}

	@Inject
	public void init() {
		firstButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				navigationController.showFirstScreenShot();
			}
		});
		prevButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				navigationController.showPrevScreenShot();
			}
		});
		nextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				navigationController.showNextScreenShot();
			}
		});
		lastButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				navigationController.showLastScreenShot();
			}
		});

		imagesComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ScreenShot shot = (ScreenShot) imagesComboBox.getModel().getSelectedItem();
					int index = getItemIndex(shot);
					navigationController.showScreenShot(index);
				}
			}

			private int getItemIndex(ScreenShot shot) {
				for (int i = 0; i < imagesComboBox.getModel().getSize(); i++) {
					if (shot.equals(imagesComboBox.getModel().getElementAt(i))) {
						return i;
					}
				}
				return -1;
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
				mainController.selectScreenShot(checkbox.isSelected());
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
