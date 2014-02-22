package com.niklim.clicktrace.dialog;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

public class SearchView extends AbstractDialogView {

	JTable resultTable;
	JRadioButton activeSessionRadio;
	JRadioButton allSessionsRadio;
	JCheckBox matchCase;
	JTextField searchQuery;
	JButton searchButton;

	public SearchView() {
		dialog.setTitle("Search");
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]rel[]"));

		allSessionsRadio = new JRadioButton("All sessions");
		activeSessionRadio = new JRadioButton("Active session");
		matchCase = new JCheckBox("Match case");
		resultTable = new JTable();
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		searchQuery = new JTextField();
		searchButton = new JButton("Search");

		layoutElements();
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
		dialog.add(searchPanel, "wrap, span 2");
		dialog.add(new JScrollPane(resultTable), "w 600, h 300, span 2, wrap, grow, push");
	}
}
