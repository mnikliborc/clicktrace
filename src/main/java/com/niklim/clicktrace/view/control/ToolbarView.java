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
import com.niklim.clicktrace.controller.operation.screenshot.OpenSearchDialogOperation;
import com.niklim.clicktrace.controller.operation.session.DeleteCurrentSessionOperation;
import com.niklim.clicktrace.controller.operation.session.NewSessionOperation;
import com.niklim.clicktrace.controller.operation.session.OpenOpenSessionDialogOperation;
import com.niklim.clicktrace.controller.operation.session.RefreshSessionOperation;
import com.niklim.clicktrace.controller.operation.session.StartSessionOperation;
import com.niklim.clicktrace.controller.operation.session.StopSessionOperation;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

@Singleton
public class ToolbarView {
	private JToolBar toolbar;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private NewSessionOperation newSessionOperation;

	@Inject
	private DeleteCurrentSessionOperation deleteCurrentSessionOperation;

	@Inject
	private StartSessionOperation startSessionOperation;

	@Inject
	private StopSessionOperation stopSessionOperation;

	@Inject
	private RefreshSessionOperation refreshSessionOperation;

	@Inject
	private OpenOpenSessionDialogOperation openSessionOperation;

	@Inject
	private OpenSearchDialogOperation openSearchDialogOperation;

	private JButton deleteSession;

	private JButton refreshSession;

	private JButton startSession;

	private JButton stopSession;

	public ToolbarView() {

	}

	@Inject
	public void init() {
		toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.setFloatable(false);
		toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));

		toolbar.add(createButton("New session " + OperationsShortcutEnum.SESSION_NEW.text,
 Icons.NEW_SESSION,
				newSessionOperation.action()));
		toolbar.addSeparator();
		toolbar.add(createButton("Open session " + OperationsShortcutEnum.SESSION_OPEN.text,
 Icons.OPEN_SESSION,
				openSessionOperation.action()));
		toolbar.addSeparator();

		toolbar.add(createButton("Find " + OperationsShortcutEnum.FIND.text, Icons.SEARCH,
				openSearchDialogOperation.action()));
		toolbar.addSeparator();

		deleteSession = createButton("Delete session " + OperationsShortcutEnum.SESSION_DELETE.text,
 Icons.DELETE_SESSION,
				deleteCurrentSessionOperation.action());
		deleteSession.setEnabled(false);
		toolbar.add(deleteSession);

		toolbar.addSeparator();
		refreshSession = createButton(
				"Refresh session " + OperationsShortcutEnum.SESSION_REFRESH.text,
				Icons.REFRESH_SESSION, refreshSessionOperation.action());
		refreshSession.setEnabled(false);
		toolbar.add(refreshSession);

		toolbar.addSeparator();
		startSession = createButton("Record [Ctrl+Shift+R]", Icons.START_SESSION,
 startSessionOperation.action());
		toolbar.add(startSession);

		toolbar.addSeparator();
		stopSession = createButton("Pause [Ctrl+Shift+S]", Icons.STOP_SESSION,
 stopSessionOperation.action());
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
		deleteSession.setEnabled(activeSession.isSessionLoaded());
		refreshSession.setEnabled(activeSession.isSessionLoaded());
		startSession.setEnabled(!activeSession.isRecording());
		stopSession.setEnabled(activeSession.isSessionLoaded() && activeSession.isRecording());
	}

}
