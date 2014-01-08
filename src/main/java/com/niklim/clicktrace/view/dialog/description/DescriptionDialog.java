package com.niklim.clicktrace.view.dialog.description;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.view.TextComponentHistory;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

@Singleton
public class DescriptionDialog extends AbstractDialog {

	private JTextArea textarea;
	private TextComponentHistory history;
	private DescriptionDialogCallback callback;

	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout());
		textarea = new JTextArea();
		history = new TextComponentHistory(textarea);

		dialog.add(new JScrollPane(textarea), "w 600, h 300, wrap");
		dialog.add(createControlPanel("Save"), "align r");

		createListeners();
		pack();
	}

	public void createListeners() {
		textarea.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				history.store();
			}
		});

		textarea.addKeyListener(new TextComponentHistory.DefaultKeyAdapter(history));
		okButton.setToolTipText("[Ctrl+S]");

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	@Override
	protected void okAction() {
		callback.setText(textarea.getText());
		close();
	}

	public void open(DescriptionDialogCallback callback) {
		this.callback = callback;

		dialog.setTitle(callback.getTitle());
		textarea.setText(callback.getText());

		resetHistory(callback.getText());

		center();
		dialog.setVisible(true);
	}

	private void resetHistory(String initialText) {
		history.reset(initialText);
	}

	public void close() {
		dialog.setVisible(false);
	}

}
