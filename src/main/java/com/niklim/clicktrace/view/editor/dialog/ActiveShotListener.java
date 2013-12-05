package com.niklim.clicktrace.view.editor.dialog;

import com.niklim.clicktrace.model.session.ScreenShot;

public interface ActiveShotListener {
	void shotChanged(ScreenShot shot);
}
