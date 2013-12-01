package com.niklim.clicktrace.view.editor.screenshot;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.SystemTestSteps;
import com.niklim.clicktrace.TestSessionsData;

public class DeleteScreenShotTest extends AbstractSystemTest {
	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldDeleteActive() {
		// given
		SystemTestSteps.openSession(editorFixture, 0);

		// when
		editorFixture.comboBox().selectItem(1);
		editorFixture.checkBox().check();
		editorFixture.comboBox().selectItem(2);
		editorFixture.checkBox().requireNotSelected();
		editorFixture.button(JButtonMatcher.withText("delete")).click();
		editorFixture.optionPane().okButton().click();

		// then
		editorFixture.checkBox().requireSelected();
		editorFixture.comboBox().requireSelection(1);
		assertThat(activeSession.getSession().getShots()).hasSize(3);
		assertThat(activeSession.getActiveShot().getFilename()).isEqualTo(editorFixture.comboBox().contents()[1]);
	}
}
