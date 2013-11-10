package com.niklim.clicktrace.view.editor;

import static org.fest.assertions.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.fest.swing.exception.WaitTimedOutError;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.junit.Test;

import com.niklim.clicktrace.ImageFileManager;

public class NewSessionTest extends AbstractEditorTest {

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

		assertThat(activeSession.isSessionOpen()).isTrue();
		assertThat(activeSession.getSession()).isNotNull();
		assertThat(activeSession.getSession().getName()).isEqualTo(sessionName);

		boolean sessionDirExists = Files.exists(Paths.get(ImageFileManager.SESSIONS_DIR + sessionName));
		assertThat(sessionDirExists).isTrue();

		for (String item : new String[] { "Start recording", "Refresh session", "Select all screenshots", "Deselect all screenshots",
				"Delete selected screenshots", "Delete current session" }) {
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

		boolean sessionDirExists = Files.exists(Paths.get(ImageFileManager.SESSIONS_DIR + sessionName));
		assertThat(sessionDirExists).isTrue();
	}

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.EMPTY;
	}
}
