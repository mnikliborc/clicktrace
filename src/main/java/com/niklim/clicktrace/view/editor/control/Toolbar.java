package com.niklim.clicktrace.view.editor.control;

import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
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

	public Toolbar() {

	}

	@Inject
	public void init() {
		toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.setFloatable(false);
		toolbar.setBorderPainted(false);
		toolbar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		toolbar.add(createIcon("New session", Icons.NEW_SESSION, newSessionActionListener));
		toolbar.addSeparator();
		toolbar.add(createIcon("Open session", Icons.OPEN_SESSION, openSessionActionListener));
		toolbar.addSeparator();
		toolbar.add(createIcon("Delete session", Icons.DELETE_SESSION, deleteCurrentSessionActionListener));
		toolbar.addSeparator();
		toolbar.add(createIcon("Refresh session", Icons.REFRESH, refreshSessionActionListener));
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

}
