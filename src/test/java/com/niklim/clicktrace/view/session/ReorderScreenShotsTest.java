package com.niklim.clicktrace.view.session;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.SystemTestSteps;
import com.niklim.clicktrace.TestSessionsData;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.model.ScreenShot;

public class ReorderScreenShotsTest extends AbstractSystemTest {

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void reorderTest() {
		// given-when
		ScreenShot firstShot = reorder("one");
		// then
		assertThat(firstShot.getFilename()).isEqualTo(activeSession.getShot(1).getFilename());
	}

	private ScreenShot reorder(String sessionName) {
		// given
		SystemTestSteps.openSession(editorFixture, sessionName);
		ScreenShot firstShot = activeSession.getShot(0);

		// when
		editorFixture.menuItemWithPath("Session", "Reorder screenshots").click();
		editorFixture.dialog().button("next").click();
		editorFixture.dialog().button(JButtonMatcher.withText("Save")).click();
		return firstShot;
	}

	@Test
	public void reorderAndRecordTest() throws InterruptedException {
		// given-when
		ScreenShot firstShot = reorder("one");

		// when
		startRecording();
		stopRecording();

		// then
		assertThat(firstShot.getFilename()).isEqualTo(activeSession.getShot(1).getFilename());
		assertThat(activeSession.getSession().getShots()).hasSize(5);
	}

	private void startRecording() throws InterruptedException {
		MainController controller = injector.getInstance(MainController.class);
		controller.startRecording(false);
		Thread.sleep(2000);
	}

	private void stopRecording() {
		MainController controller = injector.getInstance(MainController.class);
		controller.stopRecording();
	}

	@Test
	public void reorderAndDeleteTest() {
		// given-when
		ScreenShot firstShot = reorder("two");

		// when
		SystemTestSteps.deleteShot(editorFixture, 1);
		SystemTestSteps.refreshSession(editorFixture);

		// then
		assertThat(editorFixture.comboBox().contents()).hasSize(3);
	}

}
