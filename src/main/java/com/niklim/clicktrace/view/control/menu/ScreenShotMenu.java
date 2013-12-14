package com.niklim.clicktrace.view.control.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.screenshot.ChangeScreenShotLabelOperation;
import com.niklim.clicktrace.controller.operation.screenshot.DeleteScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.EditScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.OpenScreenShotDescriptionOperation;
import com.niklim.clicktrace.controller.operation.screenshot.RefreshScreenShotOperation;

@Singleton
public class ScreenShotMenu {
	private JMenu menu;

	private JMenuItem changeLabel;
	private JMenuItem setDescription;
	private JMenuItem delete;
	private JMenuItem refresh;
	private JMenuItem edit;

	@Inject
	private ChangeScreenShotLabelOperation changeScreenShotLabelOperation;
	@Inject
	private OpenScreenShotDescriptionOperation changeScreenShotDescritpionOperation;
	@Inject
	private DeleteScreenShotOperation deleteScreenShotOperation;
	@Inject
	private RefreshScreenShotOperation refreshScreenShotOperation;
	@Inject
	private EditScreenShotOperation editScreenShotOperation;

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
		JMenuItem menuItem = MenuBar.createMenuItem("Change label", changeScreenShotLabelOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createChangeDescription() {
		JMenuItem menuItem = MenuBar.createMenuItem("Change description", Icons.DESCRIPTION_SCREENSHOT,
				changeScreenShotDescritpionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createDelete() {
		JMenuItem menuItem = MenuBar.createMenuItem("Delete screenshot", Icons.DELETE_SCREENSHOT,
				deleteScreenShotOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createRefresh() {
		JMenuItem menuItem = MenuBar.createMenuItem("Refresh screenshot", Icons.REFRESH_SCREENSHOT,
				refreshScreenShotOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createEdit() {
		JMenuItem menuItem = MenuBar.createMenuItem("Edit screenshot", Icons.EDIT_SCREENSHOT,
				editScreenShotOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	public JMenu getMenu() {
		return menu;
	}

	public void sessionStateChanged() {
		changeLabel.setEnabled(activeSession.isActiveShotLoaded());
		delete.setEnabled(activeSession.isActiveShotLoaded());
		refresh.setEnabled(activeSession.isActiveShotLoaded());
		edit.setEnabled(activeSession.isActiveShotLoaded());
		setDescription.setEnabled(activeSession.isActiveShotLoaded());
	}
}
