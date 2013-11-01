package com.niklim.clicktrace.view.editor.control;

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
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.view.editor.Editor;

public class ControlView {
	@Inject
	private Editor editor;

	private JPanel panel = new JPanel(new MigLayout());
	private JPanel imagesPanel = new JPanel();
	private JComboBox<ScreenShot> imagesComboBox = new JComboBox<ScreenShot>();

	public ControlView() {
		imagesComboBox.setEditable(false);
		AutoCompleteDecorator.decorate(imagesComboBox);

		imagesPanel.setVisible(false);

		panel.add(imagesPanel);

		imagesPanel.add(new JLabel("go to"));
		imagesPanel.add(imagesComboBox);

		imagesComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ScreenShot shot = (ScreenShot) imagesComboBox.getModel().getSelectedItem();
					if (!Strings.isNullOrEmpty(shot.getName())) {
						for (int i = 0; i < imagesComboBox.getModel().getSize(); i++) {
							if (shot.equals(imagesComboBox.getModel().getElementAt(i))) {
								editor.showScreenShot(i - 1);
								break;
							}
						}
					}
				}
			}
		});
	}

	public void showImagesCombobox(Session session) {
		imagesPanel.setVisible(true);
		List<ScreenShot> shots = session.getShots();
		shots = Lists.reverse(shots);
		ScreenShot emptyScreenShot = new ScreenShot();
		emptyScreenShot.setName("");
		shots.add(emptyScreenShot);
		shots = Lists.reverse(shots);

		imagesComboBox.setModel(new DefaultComboBoxModel<ScreenShot>(shots.toArray(new ScreenShot[0])));
	}

	public void setSessions(List<Session> list, String sessionName) {
		list = Lists.reverse(list);
		Session emptySession = new Session();
		emptySession.setName("");
		list.add(emptySession);
		list = Lists.reverse(list);
	}

	public Component getComponent() {
		return panel;
	}

}