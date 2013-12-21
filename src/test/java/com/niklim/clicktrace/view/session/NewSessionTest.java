package com.niklim.clicktrace.view.session;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.fest.swing.exception.WaitTimedOutError;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.Files;
import com.niklim.clicktrace.TestSessionsData;
import com.niklim.clicktrace.service.FileManager;

public class NewSessionTest extends AbstractSystemTest {

	@Test
	public void shouldCreateNewSession() throws InterruptedException {
		// given
		final String sessionName = "test session";

		// when
		editorFixture.menuItemWithPath("File", "New session").click();
		JOptionPaneFixture optionPane = editorFixture.optionPane();
		optionPane.textBox().setText(sessionName);
		optionPane.okButton().click();

		// then
		try {
			assertThat(editorFixture.optionPane()).isNull();
		} catch (WaitTimedOutError ex) {
		}

		assertThat(activeSession.isSessionLoaded()).isTrue();
		assertThat(activeSession.getSession()).isNotNull();
		assertThat(activeSession.getSession().getName()).isEqualTo(sessionName);

		boolean sessionDirExists = Files.exists(FileManager.SESSIONS_DIR + sessionName);
		assertThat(sessionDirExists).isTrue();
		boolean propsFileExists = Files.exists(FileManager.SESSIONS_DIR + sessionName + File.separator
				+ FileManager.SESSION_PROPS_FILENAME);
		assertThat(propsFileExists).isTrue();

		for (String item : new String[] { "Start recording", "Refresh session", "Select all screenshots",
				"Deselect all screenshots", "Delete selected screenshots", "Delete current session" }) {
			editorFixture.menuItemWithPath("Session", item).requireEnabled();
		}
		editorFixture.menuItemWithPath("Session", "Stop recording").requireDisabled();
	}

	@Test
	public void shouldNotCreateExistingSession() throws InterruptedException {
		// given
		final String sessionName = "test session";

		// when
		editorFixture.menuItemWithPath("File", "New session").click();
		JOptionPaneFixture optionPane = editorFixture.optionPane();
		optionPane.textBox().setText(sessionName);
		optionPane.okButton().click();

		editorFixture.menuItemWithPath("File", "New session").click();
		optionPane = editorFixture.optionPane();
		optionPane.textBox().setText(sessionName);
		optionPane.okButton().click();

		// then
		optionPane = editorFixture.optionPane();
		assertThat(optionPane).isNotNull();
		optionPane.button().click();

		boolean sessionDirExists = Files.exists(FileManager.SESSIONS_DIR + sessionName);
		assertThat(sessionDirExists).isTrue();
	}

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.EMPTY;
	}
}
