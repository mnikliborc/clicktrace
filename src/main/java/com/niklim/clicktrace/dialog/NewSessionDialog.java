package com.niklim.clicktrace.dialog;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.dialog.description.DescriptionView;
import com.niklim.clicktrace.dialog.description.EditPreviewDescriptionToggle;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.view.TextComponentHistory;

@Singleton
public class NewSessionDialog extends AbstractDialog<NewSessionView> {

	private NewSessionCallback callback;

	private TextComponentHistory descriptionHistory;

	private EditPreviewDescriptionToggle editPreviewToggle;

	@Inject
	private UserProperties props;

	@Inject
	public void init() {
		initTextWrapping(view.sessionDescription);
		descriptionHistory = new TextComponentHistory(view.sessionDescription);
		descriptionHistory.reset("");
		createListeners();

		postInit();
	}

	public void open(NewSessionCallback callback) {
		this.callback = callback;
		initModel();

		center();
		view.dialog.setVisible(true);
	}

	private void initModel() {
		view.sessionName.setText("");
		view.sessionDescription.setText("");
		editPreviewToggle.reset(props.getMarkupSyntax());
	}

	private void createListeners() {
		view.sessionDescription.addKeyListener(new TextComponentHistory.DefaultKeyAdapter(descriptionHistory));

		view.sessionName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tryCreateSession();
				}
			}
		});
	}

	private void tryCreateSession() {
		String name = view.sessionName.getText();
		if (StringUtils.isEmpty(name.trim())) {
			JOptionPane.showMessageDialog(view.dialog, InfoMsgs.SESSION_NO_NAME);
		} else {
			if (callback.create(name, view.sessionDescription.getText())) {
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

	@Override
	protected NewSessionView createView() {
		NewSessionView view = new NewSessionView();
		createPreviewComponent(view);
		return view;
	}

	private void createPreviewComponent(NewSessionView view) {
		editPreviewToggle = new EditPreviewDescriptionToggle(view.dialog(), view.previewCheckbox,
				view.descriptionPlaceholder, view.sessionDescription, DescriptionView.MAIN_PANEL_CONTENT_LAYOUT);
	}
}
