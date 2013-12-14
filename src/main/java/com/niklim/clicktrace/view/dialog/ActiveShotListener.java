package com.niklim.clicktrace.view.dialog;

import com.niklim.clicktrace.model.session.ScreenShot;

public interface ActiveShotListener {
	void shotChanged(ScreenShot shot);
}
