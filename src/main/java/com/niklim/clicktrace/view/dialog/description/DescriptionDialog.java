package com.niklim.clicktrace.view.dialog.description;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.view.MainFrameHolder;
import com.niklim.clicktrace.view.TextComponentHistory;

@Singleton
public class DescriptionDialog {

	private JDialog dialog;

	private JTextArea textarea;

	private TextComponentHistory history;

	private DescriptionDialogCallback callback;

	public DescriptionDialog() {
	}

	@Inject
	public void init() {
		dialog = new JDialog(MainFrameHolder.get(), true);
		dialog.getContentPane().setLayout(new MigLayout());
		textarea = new JTextArea();
		history = new TextComponentHistory(textarea);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 490,
				400);
		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");

		JPanel buttonPanel = new JPanel(new MigLayout());
		buttonPanel.add(saveButton, "tag apply");
		buttonPanel.add(cancelButton, "tag cancel");

		dialog.add(new JScrollPane(textarea), "w 100%, h 100%, wrap");
		dialog.add(buttonPanel, "align r");

		createListeners(saveButton, cancelButton);
	}

	public void createListeners(JButton saveButton, JButton cancelButton) {
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

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}

		});
		saveButton.setToolTipText("[Ctrl+s]");

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void save() {
		callback.setText(textarea.getText());
		dialog.setVisible(false);
	}

	public void open(DescriptionDialogCallback callback) {
		this.callback = callback;

		dialog.setTitle(callback.getTitle());
		textarea.setText(callback.getText());

		resetHistory(callback.getText());

		dialog.setVisible(true);
	}

	private void resetHistory(String initialText) {
		history.reset(initialText);
	}

	public void close() {
		dialog.setVisible(false);
	}

}
