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
import com.niklim.clicktrace.view.editor.action.DeleteCurrentSessionActionListener;
import com.niklim.clicktrace.view.editor.action.DeleteSelectedScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.DeselectAllScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.NewSessionActionListener;
import com.niklim.clicktrace.view.editor.action.OpenSessionActionListener;
import com.niklim.clicktrace.view.editor.action.RefreshSessionActionListener;
import com.niklim.clicktrace.view.editor.action.SelectAllScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.StartSessionActionListener;
import com.niklim.clicktrace.view.editor.action.StopSessionActionListener;

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

		toolbar.add(createIcon("New session", Icons.NEW_SESSION, newSessionActionListener));
		toolbar.addSeparator();
		toolbar.add(createIcon("Open session", Icons.OPEN_SESSION, openSessionActionListener));
		toolbar.addSeparator();

		deleteSession = createIcon("Delete session", Icons.DELETE_SESSION, deleteCurrentSessionActionListener);
		deleteSession.setEnabled(false);
		toolbar.add(deleteSession);

		toolbar.addSeparator();
		refreshSession = createIcon("Refresh session", Icons.REFRESH_SESSION, refreshSessionActionListener);
		refreshSession.setEnabled(false);
		toolbar.add(refreshSession);

		toolbar.addSeparator();
		startSession = createIcon("Record", Icons.START_SESSION, startSessionActionListener);
		startSession.setEnabled(false);
		toolbar.add(startSession);

		toolbar.addSeparator();
		stopSession = createIcon("Pause", Icons.STOP_SESSION, stopSessionActionListener);
		stopSession.setEnabled(false);
		toolbar.add(stopSession);

	}

	public JButton createIcon(String tooltip, String icon, final ActionListener listener) {
		JButton newSession = new JButton(new ImageIcon(Icons.createIconImage(icon, tooltip)));
		newSession.setToolTipText(tooltip);
		newSession.addActionListener(listener);
		return newSession;
	}

	public JToolBar getToolbar() {
		return toolbar;
	}

	public void sessionStateChanged() {
		deleteSession.setEnabled(activeSession.getActive());
		refreshSession.setEnabled(activeSession.getActive());
		startSession.setEnabled(activeSession.getActive() && !activeSession.getRecording());
		stopSession.setEnabled(activeSession.getActive() && activeSession.getRecording());
	}

}
