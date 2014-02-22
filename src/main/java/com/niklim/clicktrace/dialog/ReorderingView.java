package com.niklim.clicktrace.dialog;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.view.Buttons;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

public class ReorderingView extends AbstractDialogView {
	JTable table;
	JTextArea sessionDescription;

	JButton prev;
	JButton next;

	public ReorderingView() {
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]"));
		dialog.setTitle("Reorder screenshots");

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		sessionDescription = new JTextArea();
		sessionDescription.setEditable(false);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(new JScrollPane(table));
		splitPane.setBottomComponent(new JScrollPane(sessionDescription));
		splitPane.setResizeWeight(0.4);

		dialog.add(splitPane, "push, grow, wrap, w 600");

		prev = Buttons.create("Move one before", Icons.SCREENSHOT_PREV, OperationsShortcutEnum.SHOT_MOVE_PREV);
		prev.setName("prev");
		next = Buttons.create("Move one next", Icons.SCREENSHOT_NEXT, OperationsShortcutEnum.SHOT_MOVE_NEXT);
		next.setName("next");

		dialog.add(createControlPanel("Save", prev, next), "push, grow, wrap");
	}
}
