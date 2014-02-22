package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.dialog.NewSessionDialog;
import com.niklim.clicktrace.dialog.NewSessionDialog.NewSessionCallback;

public class NewSessionOperation extends AbstractOperation {
	@Inject
	private MainController controller;

	@Inject
	private NewSessionDialog newSessionDialog;

	@Override
	public void perform() {
		newSessionDialog.open(new NewSessionCallback() {
			@Override
			public boolean create(String name, String description) {
				boolean created = controller.newSession(name, description);
				if (created) {
					newSessionDialog.close();
				}
				return created;
			}
		});
	}

	public void createSession(NewSessionCallback callback) {
		newSessionDialog.open(callback);
	}

	public void closeDialog() {
		newSessionDialog.close();
	}
}
