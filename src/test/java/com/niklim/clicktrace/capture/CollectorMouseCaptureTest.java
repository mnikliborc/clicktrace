package com.niklim.clicktrace.capture;

import static org.fest.assertions.Assertions.assertThat;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.List;

import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.SystemTestSteps;
import com.niklim.clicktrace.TestSessionsData;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.service.SessionManager;

public class CollectorMouseCaptureTest extends AbstractSystemTest {

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void test() throws InterruptedException, AWTException {
		// given
		int sessionIndex = SystemTestSteps.openSession(editorFixture, "empty");
		startRecording();

		// when
		Thread.sleep(200);
		Robot r = new Robot();
		r.mouseMove(100, 100);
		Thread.sleep(200);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		r.mouseMove(150, 100);
		Thread.sleep(200);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		Thread.sleep(200);
		stopRecording();

		// then
		checkAssertions(sessionIndex);
	}

	private void checkAssertions(int sessionIndex) {
		SessionManager manager = injector.getInstance(SessionManager.class);
		Session session = manager.loadAll().get(sessionIndex);

		List<ScreenShot> shots = session.getShots();
		assertThat(shots.size()).isPositive();
		assertThat(shots.get(shots.size() - 1).getClicks().size()).isEqualTo(2);
	}

	private void startRecording() throws InterruptedException {
		MainController controller = injector.getInstance(MainController.class);
		controller.startRecording(false);
		Thread.sleep(1000);
	}

	private void stopRecording() {
		MainController controller = injector.getInstance(MainController.class);
		controller.stopRecording();
	}

}
