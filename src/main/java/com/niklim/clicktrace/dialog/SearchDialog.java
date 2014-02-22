package com.niklim.clicktrace.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.table.DefaultTableModel;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.service.SearchService;
import com.niklim.clicktrace.service.SearchService.SearchResult;
import com.niklim.clicktrace.view.TextComponentHistory;

@Singleton
public class SearchDialog extends AbstractDialog<SearchView> {
	@Inject
	private SearchService searchService;

	@Inject
	private MainController controller;

	@Inject
	private ActiveSession activeSession;

	private TextComponentHistory history;

	private String[] resultTableColumns = new String[] { "Label", "Filename", "Session", "Phrase" };

	@Inject
	public void init() {
		history = new TextComponentHistory(view.searchQuery);
		createActionListeners();

		postInit();
	}

	private void createActionListeners() {
		final ButtonGroup searchType = new ButtonGroup();
		searchType.add(view.allSessionsRadio);
		searchType.add(view.activeSessionRadio);
		searchType.setSelected(view.allSessionsRadio.getModel(), true);

		view.resultTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					view.dialog.setVisible(false);
					SearchService.SearchResult result = (SearchService.SearchResult) view.resultTable.getModel()
							.getValueAt(view.resultTable.getSelectedRow(), 0);
					openResult(result);
				}
			}
		});

		view.resultTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					view.dialog.setVisible(false);
					ScreenShot selectedShot = (ScreenShot) view.resultTable.getModel().getValueAt(
							view.resultTable.getSelectedRow(), 0);
					controller.openSessionOnScreenShot(selectedShot);
				}
			}
		});

		view.searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				search(view.searchQuery.getText(), searchType.isSelected(view.allSessionsRadio.getModel()),
						view.matchCase.isSelected());
			}
		});

		view.searchQuery.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				if (event.getKeyChar() == '\n') {
					search(view.searchQuery.getText(), searchType.isSelected(view.allSessionsRadio.getModel()),
							view.matchCase.isSelected());
				}
			}
		});
		view.searchQuery.addKeyListener(new TextComponentHistory.DefaultKeyAdapter(history));
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

	public void open() {
		initModel();

		center();
		view.dialog.setVisible(true);
	}

	private void initModel() {
		if (activeSession.isSessionLoaded()) {
			view.activeSessionRadio.setEnabled(true);
		} else {
			view.activeSessionRadio.setEnabled(false);
			view.allSessionsRadio.setSelected(true);
		}

		if (view.resultTable.getModel().getRowCount() == 0) {
			view.resultTable.setModel(new DefaultTableModel());
		}

		history.reset(view.searchQuery.getText());
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
		view.resultTable.setModel(dataModel);

		int i = 0;
		for (SearchService.SearchResult result : shots) {
			view.resultTable.getModel().setValueAt(result, i, 0);

			if (result instanceof SearchService.ShotSearchResult) {
				SearchService.ShotSearchResult r = (SearchService.ShotSearchResult) result;
				view.resultTable.getModel().setValueAt(r.shot.getFilename(), i, 1);
				view.resultTable.getModel().setValueAt(r.shot.getSession().getName(), i, 2);
			} else if (result instanceof SearchService.SessionSearchResult) {
				view.resultTable.getModel().setValueAt("-", i, 1);
				view.resultTable.getModel().setValueAt("-", i, 2);
			}
			view.resultTable.getModel().setValueAt(result.highlight, i, 3);
			i++;
		}
	}

	@Override
	protected void okAction() {
	}

	@Override
	protected SearchView createView() {
		return new SearchView();
	}

	@Override
	protected void createControlListeners() {

	}
}