package com.niklim.clicktrace.view.editor.session;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.exception.ComponentLookupException;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.SystemTestSteps;
import com.niklim.clicktrace.TestSessionsData;

public class DeleteSelectedScreenShotsTest extends AbstractSystemTest {

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldDeleteAll() {
		// given
		SystemTestSteps.openSession(editorFixture, 0);

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
