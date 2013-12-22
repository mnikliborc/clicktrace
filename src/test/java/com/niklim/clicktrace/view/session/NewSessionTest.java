package com.niklim.clicktrace.view.session;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.exception.WaitTimedOutError;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.Files;
import com.niklim.clicktrace.TestSessionsData;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.service.SessionManager;

public class NewSessionTest extends AbstractSystemTest {

	@Test
	public void shouldCreateNewSession() throws InterruptedException, ConfigurationException {
		// given
		final String sessionName = "test session";
		final String sessionDescription = null;

		// when
		editorFixture.menuItemWithPath("File", "New session").click();
		DialogFixture dialogPane = editorFixture.dialog();
		dialogPane.textBox("name").setText(sessionName);
		dialogPane.button(JButtonMatcher.withText("Create")).click();

		// then
		try {
			assertThat(editorFixture.optionPane()).isNull();
		} catch (WaitTimedOutError ex) {
		}

		assertThat(activeSession.isSessionLoaded()).isTrue();
		assertThat(activeSession.getSession()).isNotNull();
		assertThat(activeSession.getSession().getName()).isEqualTo(sessionName);

		assertDataPersisted(sessionName, sessionDescription);
		assertControlsState();
	}

	@Test
	public void shouldCreateNewSessionWithDescription() throws InterruptedException, ConfigurationException {
		// given
		final String sessionName = "test session";
		final String sessionDescription = "test session description";

		// when
		editorFixture.menuItemWithPath("File", "New session").click();
		DialogFixture dialogPane = editorFixture.dialog();
		dialogPane.textBox("name").setText(sessionName);
		dialogPane.textBox("description").setText(sessionDescription);
		dialogPane.button(JButtonMatcher.withText("Create")).click();

		// then
		try {
			assertThat(editorFixture.optionPane()).isNull();
		} catch (WaitTimedOutError ex) {
		}

		assertThat(activeSession.isSessionLoaded()).isTrue();
		assertThat(activeSession.getSession()).isNotNull();
		assertThat(activeSession.getSession().getDescription()).isEqualTo(sessionDescription);

		assertDataPersisted(sessionName, sessionDescription);
		assertControlsState();
	}

	private void assertDataPersisted(String sessionName, String sessionDescription) throws ConfigurationException {
		boolean sessionDirExists = Files.exists(FileManager.SESSIONS_DIR + sessionName);
		assertThat(sessionDirExists).isTrue();

		String propsPath = FileManager.SESSIONS_DIR + sessionName + File.separator + FileManager.SESSION_PROPS_FILENAME;
		boolean propsFileExists = Files.exists(propsPath);

		assertThat(propsFileExists).isTrue();

		PropertiesConfiguration props = new PropertiesConfiguration(propsPath);
		assertThat(props.getString(SessionManager.PROP_SESSION_DESCRIPTION)).isEqualTo(sessionDescription);
	}

	private void assertControlsState() {
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
		DialogFixture dialogPane = editorFixture.dialog();
		dialogPane.textBox("name").setText(sessionName);
		dialogPane.button(JButtonMatcher.withText("Create")).click();

		editorFixture.menuItemWithPath("File", "New session").click();
		dialogPane.textBox("name").setText(sessionName);
		dialogPane.button(JButtonMatcher.withText("Create")).click();

		// then
		JOptionPaneFixture optionPane = editorFixture.optionPane();
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
