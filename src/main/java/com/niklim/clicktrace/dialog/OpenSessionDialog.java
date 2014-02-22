package com.niklim.clicktrace.dialog;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.SessionMetadata;
import com.niklim.clicktrace.service.SessionManager;

@Singleton
public class OpenSessionDialog extends AbstractDialog<OpenSessionView> {

	@Inject
	private MainController controller;

	@Inject
	private SessionManager sessionManager;

	List<Session> sessions;

	@Inject
	public void init() {
		initTextWrapping(view.sessionDescription);
		createListeners();

		postInit();
	}

	private void createListeners() {
		view.table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					view.dialog.setVisible(false);
					controller.showSession(sessionManager.loadAll().get(view.table.getSelectedRow()));
				}
			}
		});
		view.table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					view.dialog.setVisible(false);
					controller.showSession(sessionManager.loadAll().get(view.table.getSelectedRow()));
				}
			}
		});
		view.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				refreshDescription();
			}
		});
	}

	@Override
	protected void okAction() {
		close(true);
	}

	@Override
	protected void cancelAction() {
		close(false);
	}

	private void refreshDescription() {
		int selectedRow = view.table.getSelectedRow();
		if (selectedRow == -1 || sessions.isEmpty()) {
			view.sessionDescription.setText("");
		} else {
			view.sessionDescription.setText(sessions.get(selectedRow).getDescription());
		}
	}

	public void open() {
		loadSessions();

		center();
		view.dialog.setVisible(true);
	}

	private void close(boolean openSession) {
		view.dialog.setVisible(false);
		if (openSession) {
			controller.showSession(sessionManager.loadAll().get(view.table.getSelectedRow()));
		}
	}

	@SuppressWarnings("serial")
	private void loadSessions() {
		sessions = sessionManager.loadAll();
		DefaultTableModel dataModel = new DefaultTableModel(new String[] { "Name", "Screenshots", "Modified" },
				sessions.size()) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells not editable
				return false;
			}
		};
		view.table.setModel(dataModel);
		view.table.getSelectionModel().setSelectionInterval(0, 0);

		int i = 0;
		for (Session session : sessions) {
			SessionMetadata metadata = session.loadMetadata();
			view.table.getModel().setValueAt(session, i, 0);
			view.table.getModel().setValueAt(metadata.getSize(), i, 1);
			view.table.getModel().setValueAt(metadata.getModified(), i, 2);
			i++;
		}
	}

	@Override
	protected OpenSessionView createView() {
		return new OpenSessionView();
	}
}