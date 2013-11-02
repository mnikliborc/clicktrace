package com.niklim.clicktrace.view.editor;

import static org.fest.assertions.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.fest.swing.exception.WaitTimedOutError;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.junit.Test;

import com.niklim.clicktrace.ImageFileManager;
import com.niklim.clicktrace.controller.ActiveSession;

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

		ActiveSession activeSession = injector.getInstance(ActiveSession.class);
		assertThat(activeSession.getActive()).isTrue();
		assertThat(activeSession.getSession()).isNotNull();
		assertThat(activeSession.getSession().getName()).isEqualTo(sessionName);

		boolean sessionDirExists = Files.exists(Paths.get(ImageFileManager.SESSIONS_DIR + sessionName));
		assertThat(sessionDirExists).isTrue();
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
}
