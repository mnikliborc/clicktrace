package com.niklim.clicktrace.view.editor.control;

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
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.view.editor.action.screenshot.DeleteScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.EditScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.OpenScreenShotDescriptionActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.RefreshScreenShotActionListener;
import com.niklim.clicktrace.view.editor.session.ScreenShotView;

@Singleton
public class ControlView {
	@Inject
	private Controller controller;

	@Inject
	private ScreenShotView screenShotView;

	@Inject
	private Toolbar toolbar;

	@Inject
	private EditScreenShotActionListener editScreenShotActionListener;
	@Inject
	private RefreshScreenShotActionListener refreshScreenShotActionListener;
	@Inject
	private DeleteScreenShotActionListener deleteScreenShotActionListener;
	@Inject
	private OpenScreenShotDescriptionActionListener changeScreenShotDescritpionActionListener;

	private JPanel panel = new JPanel(new MigLayout());
	private JPanel controlPanel = new JPanel();
	private JComboBox<ScreenShot> imagesComboBox = new JComboBox<ScreenShot>();

	private JButton deleteButton;
	private JButton editButton;
	private JButton refreshButton;
	private JCheckBox checkbox;
	private JButton descriptionButton;

	public ControlView() {
		imagesComboBox.setEditable(false);

		controlPanel.setVisible(false);

		deleteButton = new JButton("delete", new ImageIcon(Icons.createIconImage(Icons.DELETE_SCREENSHOT, "delete")));
		editButton = new JButton("edit", new ImageIcon(Icons.createIconImage(Icons.EDIT_SCREENSHOT, "edit")));
		refreshButton = new JButton("refresh", new ImageIcon(Icons.createIconImage(Icons.REFRESH_SCREENSHOT, "refresh")));
		checkbox = new JCheckBox();
		descriptionButton = new JButton("description", new ImageIcon(Icons.createIconImage(Icons.REFRESH_SCREENSHOT, "description")));

		controlPanel.add(new JLabel("Screen shot"));
		controlPanel.add(imagesComboBox);
		controlPanel.add(refreshButton);
		controlPanel.add(editButton);
		controlPanel.add(deleteButton);
		controlPanel.add(descriptionButton);
		controlPanel.add(checkbox);

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
		refreshButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshScreenShotActionListener.actionPerformed(null);
			}
		});

		editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		editButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				editScreenShotActionListener.actionPerformed(null);
			}
		});

		deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				deleteScreenShotActionListener.actionPerformed(null);
			}
		});

		checkbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.selectScreenShot(checkbox.isSelected());
			}
		});
		descriptionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		descriptionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changeScreenShotDescritpionActionListener.actionPerformed(null);
			}
		});
	}

	@Inject
	public void init() {
		panel.add(toolbar.getToolbar(), "wrap");
		panel.add(controlPanel);
	}

	public void showImagesCombobox(Session session) {
		controlPanel.setVisible(true);
		List<ScreenShot> shots = new ArrayList<ScreenShot>(session.getShots());
		imagesComboBox.setModel(new DefaultComboBoxModel<ScreenShot>(shots.toArray(new ScreenShot[0])));
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
		descriptionButton.setText(Strings.isNullOrEmpty(shot.getDescription()) ? "add description" : "show description");
	}

}
