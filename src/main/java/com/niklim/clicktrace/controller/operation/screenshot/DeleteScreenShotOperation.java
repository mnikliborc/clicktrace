package com.niklim.clicktrace.controller.operation.screenshot;

import javax.swing.JOptionPane;

import com.atlassian.util.concurrent.Effect;
import com.atlassian.util.concurrent.Promises;
import com.google.common.util.concurrent.FutureCallback;
import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.MainView;

public class DeleteScreenShotOperation extends AbstractOperation {
	@Inject
	private MainView mainView;
	@Inject
	private MainController controller;
	@Inject
	private ActiveSession activeSesssion;

	@Override
	public void perform() {
		if (activeSesssion.getSession() == null || activeSesssion.getActiveShot() == null) {
			return;
		}

		FutureCallback<Integer> p = Promises.futureCallback(new Effect<Integer>() {
			public void apply(Integer a) {
			}

		}, new Effect<Throwable>() {
			public void apply(Throwable a) {
			}
		});

		// Promises.forFuture(Futures.);p.onSuccess(result);
		String screenShot = activeSesssion.getActiveShot().toString();
		int answer = JOptionPane.showConfirmDialog(mainView.getFrame(), "Are you sure to delete '" + screenShot
				+ "' screenshot?", "", JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			controller.deleteActiveScreenShot();
		}

	}
}
