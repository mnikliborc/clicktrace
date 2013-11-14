package com.niklim.clicktrace.view.editor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.view.editor.action.screenshot.SaveScreenShotDescriptionActionListener;

@Singleton
public class DescriptionEditor {

	private JDialog dialog;

	private JTextArea textarea;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private SaveScreenShotDescriptionActionListener saveScreenShotDescriptionActionListener;

	public DescriptionEditor() {
	}

	@Inject
	public void init() {
		dialog = new JDialog();
		dialog.getContentPane().setLayout(new MigLayout());
		textarea = new JTextArea();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 490, 400);
		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		dialog.add(new JScrollPane(textarea), "w 100%, h 100%,span,wrap");
		dialog.add(buttonPanel, "align r");

		createListeners(saveButton, cancelButton);
	}

	public void createListeners(JButton saveButton, JButton cancelButton) {

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				activeSession.getActiveShot().setDescription(textarea.getText());
				saveScreenShotDescriptionActionListener.actionPerformed(null);
				dialog.setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void open() {
		dialog.setTitle(activeSession.getActiveShot() + " - description");
		textarea.setText(activeSession.getActiveShot().getDescription());
		dialog.setVisible(true);
	}
}
