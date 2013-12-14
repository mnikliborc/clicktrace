package com.niklim.clicktrace.view.session;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.data.TableCell;
import org.fest.swing.exception.WaitTimedOutError;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.SystemTestSteps;
import com.niklim.clicktrace.TestSessionsData;
import com.niklim.clicktrace.controller.ActiveSession;

public class OpenSessionTest extends AbstractSystemTest {

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldOpenOnDoubleClick() throws InterruptedException {
		// when
		editorFixture.menuItemWithPath("File", "Open session").click();
		editorFixture.dialog().table().cell(TableCell.row(1).column(0)).doubleClick();

		// then
		try {
			assertThat(editorFixture.dialog()).isNull();
		} catch (WaitTimedOutError ex) {
		}

		assertOpen(TestSessionsData.SOME.getSessionNames()[1]);
	}

	@Test
	public void shouldOpenOnOkClick() throws InterruptedException {
		// when
		SystemTestSteps.openSession(editorFixture, 2);

		// then
		try {
			assertThat(editorFixture.dialog()).isNull();
		} catch (WaitTimedOutError ex) {
		}

		assertOpen(TestSessionsData.SOME.getSessionNames()[2]);
	}

	private void assertOpen(String sessionName) {
		assertThat(editorFixture.comboBox().contents()).hasSize(4);

		ActiveSession activeSession = injector.getInstance(ActiveSession.class);
		assertThat(activeSession.isSessionLoaded()).isTrue();
		assertThat(activeSession.getSession()).isNotNull();
		assertThat(activeSession.getSession().getName()).isEqualTo(sessionName);
	}
}
