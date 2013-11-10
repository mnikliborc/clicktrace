package com.niklim.clicktrace.view.editor.session;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.exception.ComponentLookupException;
import org.junit.Test;

import com.niklim.clicktrace.view.editor.AbstractEditorTest;
import com.niklim.clicktrace.view.editor.EditorTestSteps;
import com.niklim.clicktrace.view.editor.TestSessionsData;

public class DeleteSelectedScreenShotsTest extends AbstractEditorTest {

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldDeleteAll() {
		// given
		EditorTestSteps.openSession(editorFixture, 0);

		// when
		editorFixture.menuItemWithPath("Session", "Select all screenshots").click();
		editorFixture.menuItemWithPath("Session", "Delete selected screenshots").click();
		editorFixture.optionPane().okButton().click();

		// then
		try {
			assertThat(editorFixture.comboBox()).isNull();
		} catch (ComponentLookupException ex) {
		}
		assertThat(activeSession.getSelectedShots()).hasSize(0);
		assertThat(activeSession.getSession().getShots()).hasSize(0);
	}
}
