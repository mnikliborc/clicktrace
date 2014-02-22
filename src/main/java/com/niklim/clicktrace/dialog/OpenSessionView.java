package com.niklim.clicktrace.dialog;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

public class OpenSessionView extends AbstractDialogView {
	JTable table;
	JTextArea sessionDescription;

	public OpenSessionView() {
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]"));
		dialog.setTitle("Open session");

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		sessionDescription = new JTextArea();
		sessionDescription.setEditable(false);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(new JScrollPane(table));
		splitPane.setBottomComponent(new JScrollPane(sessionDescription));
		splitPane.setResizeWeight(0.4);

		dialog.add(splitPane, "push, grow, wrap, w 600");
		dialog.add(createControlPanel("Open"), "align r");
	}
}
