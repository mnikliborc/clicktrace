package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.view.TextComponentHistory;

@Singleton
public class NewSessionDialog extends AbstractDialog {
	private JTextField sessionName;
	private JTextArea sessionDescription;
	private NewSessionCallback callback;

	private TextComponentHistory descriptionHistory;

	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout());
		dialog.setTitle("New session");

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 300, 590, 400);

		sessionName = new JTextField();
		sessionName.setName("name");

		sessionDescription = new JTextArea();
		sessionDescription.setName("description");

		descriptionHistory = new TextComponentHistory(sessionDescription);
		descriptionHistory.reset("");

		dialog.add(new JLabel("Name"), "wrap");
		dialog.add(sessionName, "wrap, w 100%");

		dialog.add(new JLabel("Description"), "span 2, wrap");
		dialog.add(new JScrollPane(sessionDescription), "span 2, w 100%, h 100%, wrap");

		dialog.add(createControlPanel("Create"), "align r, span 2");

		createListeners();
	}

	public void open(NewSessionCallback callback) {
		sessionName.setText("");
		sessionDescription.setText("");

		this.callback = callback;
		dialog.setVisible(true);
	}

	private void createListeners() {
		sessionDescription.addKeyListener(new TextComponentHistory.DefaultKeyAdapter(descriptionHistory));

		sessionName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tryCreateSession();
				}
			}
		});
	}

	private void tryCreateSession() {
		String name = sessionName.getText();
		if (StringUtils.isEmpty(name.trim())) {
			JOptionPane.showMessageDialog(dialog, InfoMsgs.SESSION_NO_NAME);
		} else {
			if (callback.create(name, sessionDescription.getText())) {
				close();
			}
		}
	}

	public static interface NewSessionCallback {
		boolean create(String name, String description);
	}

	@Override
	protected void okAction() {
		tryCreateSession();
	}

}
