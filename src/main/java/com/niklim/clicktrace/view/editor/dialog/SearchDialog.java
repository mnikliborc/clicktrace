package com.niklim.clicktrace.view.editor.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.service.SearchService;
import com.niklim.clicktrace.view.editor.Editor;
import com.niklim.clicktrace.view.editor.TextComponentHistory;

@Singleton
public class SearchDialog {
	@Inject
	private Editor editor;

	@Inject
	private SearchService searchService;

	@Inject
	private Controller controller;

	@Inject
	private ActiveSession activeSession;

	private JDialog dialog;
	private JTable resultTable;
	private JRadioButton activeSessionRadio;
	private JRadioButton allSessionsRadio;
	private JCheckBox matchCase;
	private JTextField searchQuery;
	private JButton searchButton;

	private TextComponentHistory history;

	private String[] resultTableColumns = new String[] { "Label", "Filename", "Session", "Phrase" };

	@Inject
	public void init() {
		dialog = new JDialog(editor.getFrame(), true);
		dialog.setTitle("Search");
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]rel[]"));

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 820, 470);

		allSessionsRadio = new JRadioButton("All sessions");
		activeSessionRadio = new JRadioButton("Active session");
		matchCase = new JCheckBox("Match case");
		resultTable = new JTable();
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		searchQuery = new JTextField();
		history = new TextComponentHistory(searchQuery);
		searchButton = new JButton("Search");

		layoutElements();
		createActionListeners();
	}

	private void createActionListeners() {
		final ButtonGroup searchType = new ButtonGroup();
		searchType.add(allSessionsRadio);
		searchType.add(activeSessionRadio);
		searchType.setSelected(allSessionsRadio.getModel(), true);

		resultTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					dialog.setVisible(false);
					ScreenShot selectedShot = (ScreenShot) resultTable.getModel().getValueAt(
							resultTable.getSelectedRow(), 0);
					controller.openSessionOnScreenShot(selectedShot);
				}
			}
		});

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				search(searchQuery.getText(), searchType.isSelected(allSessionsRadio.getModel()),
						matchCase.isSelected());
			}
		});

		searchQuery.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				if (event.getKeyChar() == '\n') {
					search(searchQuery.getText(), searchType.isSelected(allSessionsRadio.getModel()),
							matchCase.isSelected());
				}
			}
		});
		searchQuery.addKeyListener(new TextComponentHistory.DefaultKeyAdapter(history));

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void layoutElements() {
		JPanel controlPanel = new JPanel(new MigLayout("left"));
		controlPanel.add(allSessionsRadio);
		controlPanel.add(activeSessionRadio);
		controlPanel.add(matchCase);

		JPanel searchPanel = new JPanel(new MigLayout("fill"));
		searchPanel.add(searchQuery, "grow");
		searchPanel.add(searchButton);

		dialog.add(controlPanel, "wrap");
		dialog.add(searchPanel, "wrap, span 2, grow");
		dialog.add(new JScrollPane(resultTable), "w 800, span 2, wrap");
	}

	public void open() {
		if (activeSession.isSessionOpen()) {
			activeSessionRadio.setEnabled(true);
		} else {
			activeSessionRadio.setEnabled(false);
			allSessionsRadio.setSelected(true);
		}

		if (resultTable.getModel().getRowCount() == 0) {
			resultTable.setModel(new DefaultTableModel());
		}

		history.reset(searchQuery.getText());
		dialog.setVisible(true);
	}

	private void close() {
		dialog.setVisible(false);
	}

	@SuppressWarnings("serial")
	public void search(String query, boolean allSessions, boolean matchCase) {
		List<SimpleImmutableEntry<ScreenShot, String>> shots = searchService.search(query, allSessions, matchCase);
		DefaultTableModel dataModel = new DefaultTableModel(resultTableColumns, shots.size()) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells not editable
				return false;
			}
		};
		resultTable.setModel(dataModel);

		int i = 0;
		for (SimpleImmutableEntry<ScreenShot, String> shot : shots) {
			resultTable.getModel().setValueAt(shot.getKey(), i, 0);
			resultTable.getModel().setValueAt(shot.getKey().getFilename(), i, 1);
			resultTable.getModel().setValueAt(shot.getKey().getSession().getName(), i, 2);
			resultTable.getModel().setValueAt(shot.getValue(), i, 3);
			i++;
		}
	}
}
