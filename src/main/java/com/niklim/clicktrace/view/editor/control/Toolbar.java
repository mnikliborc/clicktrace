package com.niklim.clicktrace.view.editor.control;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.view.editor.action.session.DeleteCurrentSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.DeleteSelectedScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.session.DeselectAllScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.session.NewSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.OpenSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.RefreshSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.SelectAllScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.session.StartSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.StopSessionActionListener;

@Singleton
public class Toolbar {
	private JToolBar toolbar;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private NewSessionActionListener newSessionActionListener;

	@Inject
	private DeleteCurrentSessionActionListener deleteCurrentSessionActionListener;

	@Inject
	private StartSessionActionListener startSessionActionListener;

	@Inject
	private StopSessionActionListener stopSessionActionListener;

	@Inject
	private RefreshSessionActionListener refreshSessionActionListener;

	@Inject
	private DeleteSelectedScreenShotsActionListener deleteSelectedScreenShotsActionListener;

	@Inject
	private SelectAllScreenShotsActionListener selectAllScreenShotsActionListener;

	@Inject
	private DeselectAllScreenShotsActionListener deselectAllScreenShotsActionListener;

	@Inject
	private OpenSessionActionListener openSessionActionListener;

	private JButton deleteSession;

	private JButton refreshSession;

	private JButton startSession;

	private JButton stopSession;

	public Toolbar() {

	}

	@Inject
	public void init() {
		toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.setFloatable(false);
		toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));

		toolbar.add(createButton("New session", Icons.NEW_SESSION, newSessionActionListener));
		toolbar.addSeparator();
		toolbar.add(createButton("Open session", Icons.OPEN_SESSION, openSessionActionListener));
		toolbar.addSeparator();

		deleteSession = createButton("Delete session", Icons.DELETE_SESSION, deleteCurrentSessionActionListener);
		deleteSession.setEnabled(false);
		toolbar.add(deleteSession);

		toolbar.addSeparator();
		refreshSession = createButton("Refresh session", Icons.REFRESH_SESSION, refreshSessionActionListener);
		refreshSession.setEnabled(false);
		toolbar.add(refreshSession);

		toolbar.addSeparator();
		startSession = createButton("Record", Icons.START_SESSION, startSessionActionListener);
		startSession.setEnabled(false);
		toolbar.add(startSession);

		toolbar.addSeparator();
		stopSession = createButton("Pause", Icons.STOP_SESSION, stopSessionActionListener);
		stopSession.setEnabled(false);
		toolbar.add(stopSession);
	}

	public JButton createButton(String tooltip, String icon, final ActionListener listener) {
		JButton button = new JButton(new ImageIcon(Icons.createIconImage(icon, tooltip)));
		button.setToolTipText(tooltip);
		button.addActionListener(listener);
		button.setFocusPainted(false);
		return button;
	}

	public JToolBar getToolbar() {
		return toolbar;
	}

	public void sessionStateChanged() {
		deleteSession.setEnabled(activeSession.isSessionOpen());
		refreshSession.setEnabled(activeSession.isSessionOpen());
		startSession.setEnabled(activeSession.isSessionOpen() && !activeSession.getRecording());
		stopSession.setEnabled(activeSession.isSessionOpen() && activeSession.getRecording());
	}

}
