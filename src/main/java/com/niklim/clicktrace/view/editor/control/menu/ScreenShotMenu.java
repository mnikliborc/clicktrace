package com.niklim.clicktrace.view.editor.control.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.view.editor.action.screenshot.ChangeScreenShotNameActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.DeleteScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.EditScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.RefreshScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.SetScreenShotDescritpionActionListener;

@Singleton
public class ScreenShotMenu {
	private JMenu menu;

	private JMenuItem changeName;
	private JMenuItem setDescription;
	private JMenuItem delete;
	private JMenuItem refresh;
	private JMenuItem edit;

	@Inject
	private ChangeScreenShotNameActionListener changeScreenShotNameActionListener;
	@Inject
	private SetScreenShotDescritpionActionListener setScreenShotDescritpionActionListener;
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

		changeName = createChangeName();
		setDescription = createSetDescription();
		delete = createDelete();
		edit = createEdit();
		refresh = createRefresh();

		menu.add(changeName);
		menu.add(setDescription);
		menu.addSeparator();
		menu.add(refresh);
		menu.add(edit);
		menu.addSeparator();
		menu.add(delete);
	}

	private JMenuItem createChangeName() {
		JMenuItem menuItem = Menu.createMenuItem("Change name", changeScreenShotNameActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSetDescription() {
		JMenuItem menuItem = Menu.createMenuItem("Set description", setScreenShotDescritpionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createDelete() {
		JMenuItem menuItem = Menu.createMenuItem("Delete screen shot", Icons.DELETE_SCREENSHOT, deleteScreenShotActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createRefresh() {
		JMenuItem menuItem = Menu.createMenuItem("Refresh screen shot", Icons.REFRESH_SCREENSHOT, refreshScreenShotActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createEdit() {
		JMenuItem menuItem = Menu.createMenuItem("Edit screen shot", Icons.EDIT_SCREENSHOT, editScreenShotActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	public JMenu getMenu() {
		return menu;
	}

	public void sessionStateChanged() {
		changeName.setEnabled(activeSession.isActiveShotOpen());
		delete.setEnabled(activeSession.isActiveShotOpen());
		refresh.setEnabled(activeSession.isActiveShotOpen());
		edit.setEnabled(activeSession.isActiveShotOpen());
		setDescription.setEnabled(activeSession.isActiveShotOpen());
	}
}
