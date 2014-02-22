package com.niklim.clicktrace.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.session.RefreshSessionOperation;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.dao.SessionPropertiesWriter;
import com.niklim.clicktrace.service.SessionManager;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

@Singleton
public class ReorderingDialog extends AbstractDialog<ReorderingView> {
	@Inject
	private ActiveSession activeSession;

	@Inject
	private RefreshSessionOperation refreshSessionOperation;

	@Inject
	private SessionManager sessionManager;

	@Inject
	public void init() {
		initTextWrapping(view.sessionDescription);
		createListeners();

		postInit();
	}

	private void createListeners() {
		view.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				refreshDescription();
				recalculateNavigationState();
			}

			private void recalculateNavigationState() {
				int shotsCount = activeSession.getSession().getShots().size();
				if (view.table.getSelectionModel().getMaxSelectionIndex() >= shotsCount - 1) {
					view.next.setEnabled(false);
				} else if (view.table.getSelectedRowCount() > 0) {
					view.next.setEnabled(true);
				} else {
					view.next.setEnabled(false);
				}

				if (view.table.getSelectionModel().getMinSelectionIndex() == 0) {
					view.prev.setEnabled(false);
				} else if (view.table.getSelectedRowCount() > 0) {
					view.prev.setEnabled(true);
				} else {
					view.prev.setEnabled(false);
				}
			}
		});

		final ActionListener movePrev = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ListSelectionModel selection = view.table.getSelectionModel();

				DefaultTableModel model = (DefaultTableModel) view.table.getModel();
				model.moveRow(selection.getMinSelectionIndex(), selection.getMaxSelectionIndex(),
						selection.getMinSelectionIndex() - 1);

				view.table.setRowSelectionInterval(selection.getMinSelectionIndex() - 1,
						selection.getMaxSelectionIndex() - 1);
			}
		};
		view.prev.addActionListener(movePrev);

		final ActionListener moveNext = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ListSelectionModel selection = view.table.getSelectionModel();

				DefaultTableModel model = (DefaultTableModel) view.table.getModel();
				model.moveRow(selection.getMinSelectionIndex(), selection.getMaxSelectionIndex(),
						selection.getMinSelectionIndex() + 1);

				view.table.setRowSelectionInterval(selection.getMinSelectionIndex() + 1,
						selection.getMaxSelectionIndex() + 1);
			}
		};
		view.next.addActionListener(moveNext);

		view.dialog.getRootPane().registerKeyboardAction(
				movePrev,
				KeyStroke.getKeyStroke(OperationsShortcutEnum.SHOT_MOVE_PREV.code,
						OperationsShortcutEnum.SHOT_MOVE_PREV.modifier), JComponent.WHEN_IN_FOCUSED_WINDOW);
		view.dialog.getRootPane().registerKeyboardAction(
				moveNext,
				KeyStroke.getKeyStroke(OperationsShortcutEnum.SHOT_MOVE_NEXT.code,
						OperationsShortcutEnum.SHOT_MOVE_NEXT.modifier), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void refreshDescription() {
		if (view.table.getSelectedRowCount() == 1) {
			view.sessionDescription.setEnabled(true);
			int selectedRow = view.table.getSelectedRow();
			ScreenShot shot = (ScreenShot) view.table.getModel().getValueAt(selectedRow, 0);
			if (shot != null) {
				view.sessionDescription.setText(shot.getDescription());
			}
		} else {
			view.sessionDescription.setEnabled(false);
			view.sessionDescription.setText("");
		}
	}

	public void open() {
		initModel();

		center();
		view.dialog.setVisible(true);
	}

	private void initModel() {
		loadShots();
		refreshDescription();
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
		DefaultTableModel model = (DefaultTableModel) view.table.getModel();
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
		view.table.setModel(dataModel);
		view.table.getSelectionModel().setSelectionInterval(0, 0);

		int i = 0;
		for (ScreenShot shot : shots) {
			view.table.getModel().setValueAt(shot, i, 0);
			i++;
		}
	}

	@Override
	protected ReorderingView createView() {
		return new ReorderingView();
	}
}
