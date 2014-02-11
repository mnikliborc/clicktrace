package com.niklim.clicktrace.view.session;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.exception.WaitTimedOutError;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.SystemTestSteps;
import com.niklim.clicktrace.TestSessionsData;

public class DeleteSessionTest extends AbstractSystemTest {

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldDeleteSession() {
		// given
		SystemTestSteps.openSession(editorFixture, "one");

		// when
		editorFixture.menuItemWithPath("Session", "Delete current session").click();
		editorFixture.optionPane().okButton().click();

		// then
		editorFixture.menuItemWithPath("File", "Open session").click();
		editorFixture.dialog().table().requireRowCount(2);
		editorFixture.dialog().button(JButtonMatcher.withText("Cancel")).click();

		try {
			assertThat(editorFixture.comboBox()).isNull();
		} catch (WaitTimedOutError ex) {
		} catch (ComponentLookupException ex) {
		}

		for (String item : new String[] { "Stop recording", "Refresh session", "Select all screenshots",
				"Deselect all screenshots", "Delete selected screenshots", "Delete current session" }) {
			editorFixture.menuItemWithPath("Session", item).requireDisabled();
		}
	}
}
