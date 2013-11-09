package com.niklim.clicktrace.view.editor;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.exception.WaitTimedOutError;
import org.junit.Test;

public class DeleteSessionTest extends AbstractEditorTest {

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldDeleteSession() {
		// given
		EditorTestSteps.openSession(editorFixture, 0);

		// when
		editorFixture.menuItemWithPath("Session", "Delete current session").click();
		editorFixture.optionPane().okButton().click();

		// then
		editorFixture.menuItemWithPath("File", "Open session").click();
		editorFixture.dialog().table().requireRowCount(1);
		editorFixture.dialog().button(JButtonMatcher.withText("Cancel")).click();

		try {
			assertThat(editorFixture.comboBox()).isNull();
		} catch (WaitTimedOutError ex) {
		} catch (ComponentLookupException ex) {
		}

		for (String item : new String[] { "Start recording", "Stop recording", "Refresh session", "Select all screenshots",
				"Deselect all screenshots", "Delete selected screenshots", "Delete current session" }) {
			editorFixture.menuItemWithPath("Session", item).requireDisabled();
		}
	}
}
