package com.niklim.clicktrace.view.editor.menu;

import com.niklim.clicktrace.model.session.Session;

public interface MenuController {

	void newSession(String name);

	void openSession(Session session);

	void deleteActiveSession();

	void setSelectedAllScreenshots(boolean selected);

	void deleteSelectedScreenshots();

}
