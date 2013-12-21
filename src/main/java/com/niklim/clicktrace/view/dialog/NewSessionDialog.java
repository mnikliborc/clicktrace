package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Messages;

@Singleton
public class NewSessionDialog extends AbstractDialog {
	private JTextField sessionName;
	private JTextArea sessionDescription;
	private NewSessionCallback callback;

	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout());
		dialog.setTitle("New session");

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 300, 590, 600);

		sessionName = new JTextField();
		sessionDescription = new JTextArea();

		JButton createButton = new JButton("Create");
		JButton cancelButton = new JButton("Cancel");

		JPanel buttonPanel = new JPanel(new MigLayout());
		buttonPanel.add(cancelButton, "tag cancel");
		buttonPanel.add(createButton, "tag apply");

		dialog.add(new JLabel("Name"), "wrap");
		dialog.add(sessionName, "wrap, w 580");

		dialog.add(new JLabel("Description"), "span 2, wrap");
		dialog.add(new JScrollPane(sessionDescription), "span 2, w 580, h 500, wrap");

		dialog.add(buttonPanel, "align r, span 2");

		createListeners(createButton, cancelButton);
	}

	public void open(NewSessionCallback callback) {
		sessionName.setText("");
		sessionDescription.setText("");

		this.callback = callback;
		dialog.setVisible(true);
	}

	private void createListeners(JButton createButton, JButton cancelButton) {
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = sessionName.getText();
				if (StringUtils.isEmpty(name.trim())) {
					JOptionPane.showMessageDialog(dialog, Messages.NEW_SESSION_NO_NAME);
				} else {
					callback.create(name, sessionDescription.getText());
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
	}

	public static interface NewSessionCallback {
		void create(String name, String description);
	}

}
