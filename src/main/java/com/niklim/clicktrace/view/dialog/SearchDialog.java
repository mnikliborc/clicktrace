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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.service.SearchService;
import com.niklim.clicktrace.service.SearchService.SearchResult;
import com.niklim.clicktrace.view.MainFrameHolder;
import com.niklim.clicktrace.view.TextComponentHistory;

@Singleton
public class SearchDialog extends AbstractDialog {
	@Inject
	private SearchService searchService;

	@Inject
	private MainController controller;

	@Inject
	private ActiveSession activeSession;

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
		dialog = new JDialog(MainFrameHolder.get(), true);
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
					SearchService.SearchResult result = (SearchService.SearchResult) resultTable.getModel().getValueAt(
							resultTable.getSelectedRow(), 0);
					openResult(result);
				}
			}
		});

		resultTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
	}

	private void openResult(SearchResult result) {
		if (result instanceof SearchService.ShotSearchResult) {
			SearchService.ShotSearchResult r = (SearchService.ShotSearchResult) result;
			controller.openSessionOnScreenShot(r.shot);
		} else if (result instanceof SearchService.SessionSearchResult) {
			SearchService.SessionSearchResult r = (SearchService.SessionSearchResult) result;
			controller.showSession(r.session);
		}
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
		if (activeSession.isSessionLoaded()) {
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

	@SuppressWarnings("serial")
	public void search(String query, boolean allSessions, boolean matchCase) {
		List<SearchService.SearchResult> shots = searchService.search(query, allSessions, matchCase);
		DefaultTableModel dataModel = new DefaultTableModel(resultTableColumns, shots.size()) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells not editable
				return false;
			}
		};
		resultTable.setModel(dataModel);

		int i = 0;
		for (SearchService.SearchResult result : shots) {
			resultTable.getModel().setValueAt(result, i, 0);

			if (result instanceof SearchService.ShotSearchResult) {
				SearchService.ShotSearchResult r = (SearchService.ShotSearchResult) result;
				resultTable.getModel().setValueAt(r.shot.getFilename(), i, 1);
				resultTable.getModel().setValueAt(r.shot.getSession().getName(), i, 2);
			} else if (result instanceof SearchService.SessionSearchResult) {
				resultTable.getModel().setValueAt("-", i, 1);
				resultTable.getModel().setValueAt("-", i, 2);
			}
			resultTable.getModel().setValueAt(result.highlight, i, 3);
			i++;
		}
	}
}
