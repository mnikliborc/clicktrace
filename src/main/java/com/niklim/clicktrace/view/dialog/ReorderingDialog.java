package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.session.RefreshSessionOperation;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.helper.SessionPropertiesWriter;
import com.niklim.clicktrace.service.SessionManager;
import com.niklim.clicktrace.view.Buttons;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

public class ReorderingDialog extends AbstractDialog {
	JTable table;
	JTextArea description;

	JButton prev;
	JButton next;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private RefreshSessionOperation refreshSessionOperation;

	@Inject
	private SessionManager sessionManager;

	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout("", "[]", "[grow]"));
		dialog.setTitle("Reorder screenshots");

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 300, 590, 560);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		description = new JTextArea();
		description.setEditable(false);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(new JScrollPane(table));
		splitPane.setBottomComponent(new JScrollPane(description));
		splitPane.setResizeWeight(0.4);

		dialog.add(splitPane, "push, grow, wrap");

		prev = Buttons.create("Move one before", Icons.SCREENSHOT_PREV, OperationsShortcutEnum.SHOT_MOVE_PREV);
		next = Buttons.create("Move one next", Icons.SCREENSHOT_NEXT, OperationsShortcutEnum.SHOT_MOVE_NEXT);

		JPanel navigPanel = new JPanel(new MigLayout());
		navigPanel.add(prev);
		navigPanel.add(next);

		JPanel buttonPanel = new JPanel(new MigLayout("", "[]push[]"));
		buttonPanel.add(navigPanel);
		buttonPanel.add(createControlPanel("Save"));
		dialog.add(buttonPanel, "push, grow, wrap");

		createListeners();
	}

	private void createListeners() {
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				refreshDescription();
				recalculateNavigationState();
			}

			private void recalculateNavigationState() {
				int shotsCount = activeSession.getSession().getShots().size();
				if (table.getSelectionModel().getMaxSelectionIndex() >= shotsCount - 1) {
					next.setEnabled(false);
				} else if (table.getSelectedRowCount() > 0) {
					next.setEnabled(true);
				} else {
					next.setEnabled(false);
				}

				if (table.getSelectionModel().getMinSelectionIndex() == 0) {
					prev.setEnabled(false);
				} else if (table.getSelectedRowCount() > 0) {
					prev.setEnabled(true);
				} else {
					prev.setEnabled(false);
				}
			}
		});

		final ActionListener movePrev = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ListSelectionModel selection = table.getSelectionModel();

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.moveRow(selection.getMinSelectionIndex(), selection.getMaxSelectionIndex(),
						selection.getMinSelectionIndex() - 1);

				table.setRowSelectionInterval(selection.getMinSelectionIndex() - 1,
						selection.getMaxSelectionIndex() - 1);
			}
		};
		prev.addActionListener(movePrev);

		final ActionListener moveNext = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ListSelectionModel selection = table.getSelectionModel();

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.moveRow(selection.getMinSelectionIndex(), selection.getMaxSelectionIndex(),
						selection.getMinSelectionIndex() + 1);

				table.setRowSelectionInterval(selection.getMinSelectionIndex() + 1,
						selection.getMaxSelectionIndex() + 1);
			}
		};
		next.addActionListener(moveNext);

		dialog.getRootPane().registerKeyboardAction(
				movePrev,
				KeyStroke.getKeyStroke(OperationsShortcutEnum.SHOT_MOVE_PREV.code,
						OperationsShortcutEnum.SHOT_MOVE_PREV.modifier), JComponent.WHEN_IN_FOCUSED_WINDOW);
		dialog.getRootPane().registerKeyboardAction(
				moveNext,
				KeyStroke.getKeyStroke(OperationsShortcutEnum.SHOT_MOVE_NEXT.code,
						OperationsShortcutEnum.SHOT_MOVE_NEXT.modifier), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void refreshDescription() {
		if (table.getSelectedRowCount() == 1) {
			description.setEnabled(true);
			int selectedRow = table.getSelectedRow();
			ScreenShot shot = (ScreenShot) table.getModel().getValueAt(selectedRow, 0);
			if (shot != null) {
				description.setText(shot.getDescription());
			}
		} else {
			description.setEnabled(false);
			description.setText("");
		}
	}

	public void open() {
		loadShots();
		refreshDescription();
		dialog.setVisible(true);
	}

	@Override
	public void okAction() {
		saveOrdering();
		refreshSessionOperation.perform();
		close();
	}

	private void saveOrdering() {
		SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(activeSession.getSession());

		List<ScreenShot> shots = Lists.newArrayList();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			shots.add((ScreenShot) model.getValueAt(i, 0));
		}

		writer.saveOrdering(Lists.transform(shots, new Function<ScreenShot, String>() {
			public String apply(ScreenShot input) {
				return input.getFilename();
			}
		}));
	}

	private void loadShots() {
		List<ScreenShot> shots = Lists.newArrayList(activeSession.getSession().getShots());
		DefaultTableModel dataModel = new DefaultTableModel(new String[] { "Screenshot" }, shots.size()) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells not editable
				return false;
			}
		};
		table.setModel(dataModel);
		table.getSelectionModel().setSelectionInterval(0, 0);

		int i = 0;
		for (ScreenShot shot : shots) {
			table.getModel().setValueAt(shot, i, 0);
			i++;
		}
	}
}
