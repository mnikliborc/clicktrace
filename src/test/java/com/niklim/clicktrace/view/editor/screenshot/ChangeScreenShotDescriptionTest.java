package com.niklim.clicktrace.view.editor.screenshot;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.junit.Test;

import com.niklim.clicktrace.view.editor.AbstractEditorTest;
import com.niklim.clicktrace.view.editor.EditorTestSteps;
import com.niklim.clicktrace.view.editor.TestSessionsData;

public class ChangeScreenShotDescriptionTest extends AbstractEditorTest {
	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldChangeDescription() throws InterruptedException {
		EditorTestSteps.openSession(editorFixture, 0);

		editorFixture.button(JButtonMatcher.withText("show description")).click();
		assertThat(editorFixture.dialog().textBox().text()).isEqualTo("some description");
		editorFixture.comboBox().selectItem(1);

		editorFixture.menuItemWithPath("Screenshot", "Change description").click();
		editorFixture.dialog().textBox().setText("another description");
		editorFixture.dialog().button(JButtonMatcher.withText("Save")).click();

		editorFixture.menuItemWithPath("Session", "Refresh session").click();
		editorFixture.comboBox().selectItem(1);
		editorFixture.button(JButtonMatcher.withText("show description")).click();
		assertThat(editorFixture.dialog().textBox().text()).isEqualTo("another description");
	}
}