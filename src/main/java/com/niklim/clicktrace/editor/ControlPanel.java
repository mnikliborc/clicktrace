package com.niklim.clicktrace.editor;

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

public class ControlPanel {
	@Inject
	private Editor editor;

	private JPanel panel = new JPanel(new MigLayout());
	private JComboBox<String> comboBox = new JComboBox<String>();

	public ControlPanel() {
		comboBox.setEditable(false);
		AutoCompleteDecorator.decorate(comboBox);

		panel.add(new JLabel("Sessions"));
		panel.add(comboBox);

		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String sessionName = (String) comboBox.getModel().getSelectedItem();
					if (!Strings.isNullOrEmpty(sessionName)) {
						editor.showSession(sessionName);
					}
				}
			}
		});
	}

	public void setSessions(List<String> sessionNames, String sessionName) {
		sessionNames = Lists.reverse(sessionNames);
		sessionNames.add("");
		sessionNames = Lists.reverse(sessionNames);

		comboBox.setModel(new DefaultComboBoxModel<String>(sessionNames.toArray(new String[0])));
		comboBox.getModel().setSelectedItem(sessionName);
	}

	public JPanel getPanel() {
		return panel;
	}

}
