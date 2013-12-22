package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.SessionMetadata;
import com.niklim.clicktrace.service.SessionManager;

@Singleton
public class OpenSessionDialog extends AbstractDialog {

	@Inject
	private MainController controller;

	@Inject
	private SessionManager sessionManager;

	JTable table;
	JTextArea textarea;
	List<Session> sessions;

	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout());
		dialog.setTitle("Open session");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 300, 590, 600);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		textarea = new JTextArea();
		textarea.setEditable(false);

		dialog.add(new JScrollPane(table), "h 30%, w 100%, wrap");
		dialog.add(new JScrollPane(textarea), "h 60%, w 100%, wrap");
		dialog.add(createControlPanel("Open"), "align r");

		createListeners();
	}

	private void createListeners() {
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					dialog.setVisible(false);
					controller.showSession(sessionManager.loadAll().get(table.getSelectedRow()));
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					dialog.setVisible(false);
					controller.showSession(sessionManager.loadAll().get(table.getSelectedRow()));
				}
			}
		});
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1 || sessions.isEmpty()) {
			textarea.setText("");
		} else {
			textarea.setText(sessions.get(selectedRow).getDescription());
		}
	}

	public void open() {
		loadSessions();
		dialog.setVisible(true);
	}

	private void close(boolean openSession) {
		dialog.setVisible(false);
		if (openSession) {
			controller.showSession(sessionManager.loadAll().get(table.getSelectedRow()));
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
		table.setModel(dataModel);
		table.getSelectionModel().setSelectionInterval(0, 0);

		int i = 0;
		for (Session session : sessions) {
			SessionMetadata metadata = session.loadMetadata();
			table.getModel().setValueAt(session, i, 0);
			table.getModel().setValueAt(metadata.getSize(), i, 1);
			table.getModel().setValueAt(metadata.getModified(), i, 2);
			i++;
		}
	}
}
