package com.niklim.clicktrace.editor;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.niklim.clicktrace.SessionsManager;

public class ControlView {
	@Inject
	private Editor editor;

	private JPanel panel = new JPanel(new MigLayout());
	private JComboBox<String> sessionsComboBox = new JComboBox<String>();
	private JPanel imagesPanel = new JPanel();
	private JComboBox<String> imagesComboBox = new JComboBox<String>();

	public ControlView() {
		sessionsComboBox.setEditable(false);
		imagesComboBox.setEditable(false);
		AutoCompleteDecorator.decorate(sessionsComboBox);
		AutoCompleteDecorator.decorate(imagesComboBox);

		imagesPanel.setVisible(false);

		panel.add(new JLabel("Sessions"));
		panel.add(sessionsComboBox);
		panel.add(imagesPanel);

		imagesPanel.add(new JLabel("go to"));
		imagesPanel.add(imagesComboBox);

		sessionsComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String sessionName = (String) sessionsComboBox.getModel().getSelectedItem();
					if (!Strings.isNullOrEmpty(sessionName)) {
						editor.showSession(sessionName);
						showImagesCombobox(sessionName);
					}
				}
			}
		});

		imagesComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String imageName = (String) imagesComboBox.getModel().getSelectedItem();
					if (!Strings.isNullOrEmpty(imageName)) {
						for (int i = 0; i < imagesComboBox.getModel().getSize(); i++) {
							if (imageName.equals(imagesComboBox.getModel().getElementAt(i))) {
								editor.showImage(i);
								break;
							}
						}
					}
				}
			}
		});
	}

	private void showImagesCombobox(String sessionName) {
		imagesPanel.setVisible(true);
		List<String> imageNames = SessionsManager.loadSession(sessionName);
		imageNames = Lists.reverse(imageNames);
		imageNames.add("");
		imageNames = Lists.reverse(imageNames);

		imagesComboBox.setModel(new DefaultComboBoxModel<String>(imageNames.toArray(new String[0])));
	}

	public void setSessions(List<String> sessionNames, String sessionName) {
		sessionNames = Lists.reverse(sessionNames);
		sessionNames.add("");
		sessionNames = Lists.reverse(sessionNames);

		sessionsComboBox.setModel(new DefaultComboBoxModel<String>(sessionNames.toArray(new String[0])));
		sessionsComboBox.getModel().setSelectedItem(sessionName);
	}

	public Component getComponent() {
		return panel;
	}

}
