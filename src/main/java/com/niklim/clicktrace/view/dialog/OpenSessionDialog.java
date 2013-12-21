package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.SessionMetadata;
import com.niklim.clicktrace.service.SessionManager;
import com.niklim.clicktrace.view.MainFrameHolder;

@SuppressWarnings("serial")
@Singleton
public class OpenSessionDialog extends AbstractDialog {

	@Inject
	private MainController controller;

	@Inject
	private SessionManager sessionManager;

	JTable table;

	public OpenSessionDialog() {
	}

	@Inject
	public void init() {
		dialog = new JDialog(MainFrameHolder.get(), true);
		dialog.getContentPane().setLayout(new MigLayout());
		dialog.setTitle("Open session");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 490, 400);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JButton openButton = new JButton("Open");
		JButton cancelButton = new JButton("Cancel");

		JPanel buttonPanel = new JPanel(new MigLayout());
		buttonPanel.add(cancelButton, "tag cancel");
		buttonPanel.add(openButton, "tag apply");

		dialog.add(new JScrollPane(table), "wrap");
		dialog.add(buttonPanel, "align r");

		createListeners(openButton, cancelButton);
	}

	public void createListeners(JButton openButton, JButton cancelButton) {
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					dialog.setVisible(false);
					controller.openSession(sessionManager.loadAll().get(table.getSelectedRow()));
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					dialog.setVisible(false);
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
	}

	public void open() {
		loadSessions();
		dialog.setVisible(true);
	}

	private void close(boolean openSession) {
		dialog.setVisible(false);
		if (openSession) {
			controller.openSession(sessionManager.loadAll().get(table.getSelectedRow()));
		}
	}

	private void loadSessions() {
		List<Session> sessions = sessionManager.loadAll();
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
