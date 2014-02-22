package com.niklim.clicktrace.dialog.description;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.dialog.AbstractDialog;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.view.TextComponentHistory;

/**
 * Lets the user input Markup. Offers HTML preview.
 */
@Singleton
public class DescriptionDialog extends AbstractDialog<DescriptionView> {
	private TextComponentHistory history;
	private DescriptionDialogCallback callback;

	private EditPreviewDescriptionToggle editPreviewToggle;

	@Inject
	private UserProperties props;

	@Inject
	public void init() {
		initTextWrapping(view.description);
		history = new TextComponentHistory(view.description);

		createListeners();
		postInit();
	}

	public void createListeners() {
		view.description.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				history.store();
			}
		});

		view.description.addKeyListener(new TextComponentHistory.DefaultKeyAdapter(history));
		view.okButton().setToolTipText("[Ctrl+S]");

		view.dialog().getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	@Override
	protected void okAction() {
		callback.setText(view.description.getText());
		close();
	}

	public void open(DescriptionDialogCallback callback) {
		this.callback = callback;

		view.dialog().setTitle(callback.getTitle());
		showEditPanel();

		view.description.setText(callback.getText());
		view.description.grabFocus();

		resetHistory(callback.getText());

		center();
		view.dialog().setVisible(true);
	}

	private void showEditPanel() {
		editPreviewToggle.reset(props.getMarkupSyntax());
	}

	private void resetHistory(String initialText) {
		history.reset(initialText);
	}

	public void close() {
		view.dialog().setVisible(false);
	}

	@Override
	protected DescriptionView createView() {
		DescriptionView view = new DescriptionView();
		createPreviewComponent(view);
		return view;
	}

	private void createPreviewComponent(DescriptionView view) {
		editPreviewToggle = new EditPreviewDescriptionToggle(view.dialog(), view.previewCheckbox,
				view.descriptionPlaceholder, view.description, DescriptionView.MAIN_PANEL_CONTENT_LAYOUT);
	}
}
