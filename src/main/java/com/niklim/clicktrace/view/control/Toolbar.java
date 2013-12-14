package com.niklim.clicktrace.view.control;

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
import com.niklim.clicktrace.controller.action.screenshot.OpenSearchDialogActionListener;
import com.niklim.clicktrace.controller.action.session.DeleteCurrentSessionActionListener;
import com.niklim.clicktrace.controller.action.session.DeleteSelectedScreenShotsActionListener;
import com.niklim.clicktrace.controller.action.session.DeselectAllScreenShotsActionListener;
import com.niklim.clicktrace.controller.action.session.NewSessionActionListener;
import com.niklim.clicktrace.controller.action.session.OpenOpenSessionDialogActionListener;
import com.niklim.clicktrace.controller.action.session.RefreshSessionActionListener;
import com.niklim.clicktrace.controller.action.session.SelectAllScreenShotsActionListener;
import com.niklim.clicktrace.controller.action.session.StartSessionActionListener;
import com.niklim.clicktrace.controller.action.session.StopSessionActionListener;
import com.niklim.clicktrace.view.ControlShortcutEnum;

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
	private OpenOpenSessionDialogActionListener openSessionActionListener;

	@Inject
	private OpenSearchDialogActionListener openSearchDialogActionListener;

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

		toolbar.add(createButton("New session " + ControlShortcutEnum.SESSION_NEW.text,
				Icons.NEW_SESSION, newSessionActionListener));
		toolbar.addSeparator();
		toolbar.add(createButton("Open session " + ControlShortcutEnum.SESSION_OPEN.text,
				Icons.OPEN_SESSION, openSessionActionListener));
		toolbar.addSeparator();

		toolbar.add(createButton("Find " + ControlShortcutEnum.FIND.text, Icons.SEARCH,
				openSearchDialogActionListener));
		toolbar.addSeparator();

		deleteSession = createButton("Delete session " + ControlShortcutEnum.SESSION_DELETE.text,
				Icons.DELETE_SESSION, deleteCurrentSessionActionListener);
		deleteSession.setEnabled(false);
		toolbar.add(deleteSession);

		toolbar.addSeparator();
		refreshSession = createButton(
				"Refresh session " + ControlShortcutEnum.SESSION_REFRESH.text,
				Icons.REFRESH_SESSION, refreshSessionActionListener);
		refreshSession.setEnabled(false);
		toolbar.add(refreshSession);

		toolbar.addSeparator();
		startSession = createButton("Record [Ctrl+Shift+R]", Icons.START_SESSION,
				startSessionActionListener);
		toolbar.add(startSession);

		toolbar.addSeparator();
		stopSession = createButton("Pause [Ctrl+Shift+S]", Icons.STOP_SESSION,
				stopSessionActionListener);
		stopSession.setEnabled(false);
		toolbar.add(stopSession);
	}

	public JButton createButton(String tooltip, String icon, final ActionListener listener) {
		JButton button = new JButton(new ImageIcon(Icons.createIconImage(icon, tooltip)));
		button.setName(tooltip);
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
		startSession.setEnabled(!activeSession.getRecording());
		stopSession.setEnabled(activeSession.isSessionOpen() && activeSession.getRecording());
	}

}
