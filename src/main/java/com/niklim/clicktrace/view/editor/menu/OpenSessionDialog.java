package com.niklim.clicktrace.view.editor.menu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionManager;
import com.niklim.clicktrace.model.session.SessionMetadata;

public class OpenSessionDialog extends JDialog {
	@Inject
	private MenuController controller;

	@Inject
	private SessionManager sessionManager;

	JTable table;

	public OpenSessionDialog() {
		this.getContentPane().setLayout(new MigLayout());

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 490, 400);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JButton openButton = new JButton("Open");
		JButton cancelButton = new JButton("Cancel");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(openButton);
		buttonPanel.add(cancelButton);

		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.openSession(sessionManager.loadAll().get(table.getSelectedRow()));
				OpenSessionDialog.this.setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OpenSessionDialog.this.setVisible(false);
			}
		});

		add(new JScrollPane(table), "wrap");
		add(buttonPanel, "align r");
	}

	public void open() {
		loadSessions();
		this.setVisible(true);
	}

	private void loadSessions() {
		List<Session> sessions = sessionManager.loadAll();
		DefaultTableModel dataModel = new DefaultTableModel(
				new String[] { "Name", "Screenshots", "Created", "Modified" }, sessions.size()) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		table.setModel(dataModel);
		table.getSelectionModel().setSelectionInterval(0, 0);

		int i = 0;
		for (Session session : sessions) {
			SessionMetadata metadata = session.loadMetadata();
			table.getModel().setValueAt(session.getName(), i, 0);
			table.getModel().setValueAt(metadata.getSize(), i, 1);
			table.getModel().setValueAt(metadata.getCreated(), i, 2);
			table.getModel().setValueAt(metadata.getModified(), i, 3);
			i++;
		}
	}
}
