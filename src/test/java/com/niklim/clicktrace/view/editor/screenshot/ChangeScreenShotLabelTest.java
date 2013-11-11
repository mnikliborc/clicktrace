package com.niklim.clicktrace.view.editor.screenshot;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.niklim.clicktrace.view.editor.AbstractEditorTest;
import com.niklim.clicktrace.view.editor.EditorTestSteps;
import com.niklim.clicktrace.view.editor.TestSessionsData;

public class ChangeScreenShotLabelTest extends AbstractEditorTest {
	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldChangeLabel() throws InterruptedException {
		EditorTestSteps.openSession(editorFixture, 0);

		assertThat(editorFixture.comboBox().contents()[0]).isEqualTo("some screenshot");
		editorFixture.comboBox().selectItem(1);

		editorFixture.menuItemWithPath("Screenshot", "Change label").click();
		editorFixture.optionPane().textBox().setText("another screenshot");
		editorFixture.optionPane().okButton().click();

		assertThat(editorFixture.comboBox().contents()[1]).isEqualTo("another screenshot");

		editorFixture.menuItemWithPath("Session", "Refresh session").click();
		assertThat(editorFixture.comboBox().contents()[1]).isEqualTo("another screenshot");
	}
}
