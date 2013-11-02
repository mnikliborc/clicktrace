package com.niklim.clicktrace.view.editor.menu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionManager;
import com.niklim.clicktrace.model.session.SessionMetadata;

@SuppressWarnings("serial")
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

		add(new JScrollPane(table), "wrap");
		add(buttonPanel, "align r");

		createListeners(openButton, cancelButton);
	}

	public void createListeners(JButton openButton, JButton cancelButton) {
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					OpenSessionDialog.this.setVisible(false);
					controller.openSession(sessionManager.loadAll().get(table.getSelectedRow()));
				}
			}
		});

		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close(true);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close(false);
			}
		});

		getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close(false);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void open() {
		loadSessions();
		this.setVisible(true);
	}

	private void close(boolean openSession) {
		OpenSessionDialog.this.setVisible(false);
		if (openSession) {
			controller.openSession(sessionManager.loadAll().get(table.getSelectedRow()));
		}
	}

	@SuppressWarnings("serial")
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
