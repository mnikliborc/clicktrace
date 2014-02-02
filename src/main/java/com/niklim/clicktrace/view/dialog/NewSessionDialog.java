package com.niklim.clicktrace.view.dialog;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
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
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.view.TextComponentHistory;
import com.niklim.clicktrace.view.dialog.description.EditPreviewDescriptionToggle;

@Singleton
public class NewSessionDialog extends AbstractDialog {
	private static final String MAIN_PANEL_CONTENT_LAYOUT = "push, grow, w 600, h 300, wrap";
	private static final String MAIN_PANEL_LAYOUT = "grow, push,  wrap";
	
	private JPanel descriptionPlaceholder;
	private JTextField sessionName;
	private JTextArea sessionDescription;
	private NewSessionCallback callback;
	
	private TextComponentHistory descriptionHistory;
	
	@Inject
	private EditPreviewDescriptionToggle editPreviewToggle;
	
	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout("", "[fill]"));
		dialog.setTitle("New session");
		
		sessionName = new JTextField();
		sessionName.setName("name");
		
		createPlaceholderPanelWithDescription();
		
		JCheckBox previewCheckbox = createPreviewComponent();
		
		dialog.add(new JLabel("Name"), "wrap");
		dialog.add(sessionName, "wrap, grow");
		
		dialog.add(new JLabel("Description"), "wrap");
		dialog.add(descriptionPlaceholder, MAIN_PANEL_LAYOUT);
		
		dialog.add(createControlPanel("Create", previewCheckbox));
		
		createListeners();
		
		postInit();
	}
	
	private void createPlaceholderPanelWithDescription() {
		sessionDescription = new JTextArea();
		sessionDescription.setName("description");
		initTextWrapping(sessionDescription);
		
		descriptionHistory = new TextComponentHistory(sessionDescription);
		descriptionHistory.reset("");
		
		descriptionPlaceholder = new JPanel(new MigLayout("insets 0", "[fill]"));
		descriptionPlaceholder.add(new JScrollPane(sessionDescription), MAIN_PANEL_CONTENT_LAYOUT);
	}
	
	private JCheckBox createPreviewComponent() {
		editPreviewToggle.init(dialog, descriptionPlaceholder, sessionDescription,
				MAIN_PANEL_CONTENT_LAYOUT);
		return editPreviewToggle.getCheckBox();
	}
	
	public void open(NewSessionCallback callback) {
		sessionName.setText("");
		sessionDescription.setText("");
		this.callback = callback;
		
		center();
		dialog.setVisible(true);
	}
	
	private void createListeners() {
		sessionDescription.addKeyListener(new TextComponentHistory.DefaultKeyAdapter(
				descriptionHistory));
		
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
