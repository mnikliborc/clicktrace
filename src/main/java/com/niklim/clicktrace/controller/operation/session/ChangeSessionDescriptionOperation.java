package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.dao.SessionPropertiesWriter;
import com.niklim.clicktrace.service.SessionManager;
import com.niklim.clicktrace.view.dialog.description.DescriptionDialog;
import com.niklim.clicktrace.view.dialog.description.DescriptionDialogCallback;

public class ChangeSessionDescriptionOperation extends AbstractOperation {
	@Inject
	private ActiveSession activeSession;

	@Inject
	private SessionManager sessionManager;

	@Inject
	private DescriptionDialog descriptionDialog;

	@Override
	public void perform() {
		final Session session = activeSession.getSession();
		if (session != null) {
			descriptionDialog.open(new DescriptionDialogCallback() {

				@Override
				public void setText(String text) {
					session.setDescription(text);
					SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(session);
					writer.saveSessionDescription();
				}

				@Override
				public String getTitle() {
					return session.getName() + " - description";
				}

				@Override
				public String getText() {
					return session.getDescription();
				}
			});
		}
	}
}
