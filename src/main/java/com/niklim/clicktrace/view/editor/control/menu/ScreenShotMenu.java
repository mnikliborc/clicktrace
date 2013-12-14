package com.niklim.clicktrace.view.editor.control.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.view.editor.action.screenshot.ChangeScreenShotLabelActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.DeleteScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.EditScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.OpenScreenShotDescriptionActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.RefreshScreenShotActionListener;

@Singleton
public class ScreenShotMenu {
	private JMenu menu;

	private JMenuItem changeLabel;
	private JMenuItem setDescription;
	private JMenuItem delete;
	private JMenuItem refresh;
	private JMenuItem edit;

	@Inject
	private ChangeScreenShotLabelActionListener changeScreenShotLabelActionListener;
	@Inject
	private OpenScreenShotDescriptionActionListener changeScreenShotDescritpionActionListener;
	@Inject
	private DeleteScreenShotActionListener deleteScreenShotActionListener;
	@Inject
	private RefreshScreenShotActionListener refreshScreenShotActionListener;
	@Inject
	private EditScreenShotActionListener editScreenShotActionListener;

	@Inject
	private ActiveSession activeSession;

	@Inject
	public void init() {
		menu = new JMenu("Screenshot");

		changeLabel = createChangeLabel();
		setDescription = createChangeDescription();
		delete = createDelete();
		edit = createEdit();
		refresh = createRefresh();

		menu.add(changeLabel);
		menu.add(setDescription);
		menu.addSeparator();
		menu.add(refresh);
		menu.add(edit);
		menu.addSeparator();
		menu.add(delete);
	}

	private JMenuItem createChangeLabel() {
		JMenuItem menuItem = Menu.createMenuItem("Change label", changeScreenShotLabelActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createChangeDescription() {
		JMenuItem menuItem = Menu.createMenuItem("Change description", Icons.DESCRIPTION_SCREENSHOT,
				changeScreenShotDescritpionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createDelete() {
		JMenuItem menuItem = Menu.createMenuItem("Delete screenshot", Icons.DELETE_SCREENSHOT, deleteScreenShotActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createRefresh() {
		JMenuItem menuItem = Menu.createMenuItem("Refresh screenshot", Icons.REFRESH_SCREENSHOT, refreshScreenShotActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createEdit() {
		JMenuItem menuItem = Menu.createMenuItem("Edit screenshot", Icons.EDIT_SCREENSHOT, editScreenShotActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	public JMenu getMenu() {
		return menu;
	}

	public void sessionStateChanged() {
		changeLabel.setEnabled(activeSession.isActiveShotOpen());
		delete.setEnabled(activeSession.isActiveShotOpen());
		refresh.setEnabled(activeSession.isActiveShotOpen());
		edit.setEnabled(activeSession.isActiveShotOpen());
		setDescription.setEnabled(activeSession.isActiveShotOpen());
	}
}
