package com.niklim.clicktrace.view.editor.dialog;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.junit.Test;

import com.niklim.clicktrace.AppProperties;
import com.niklim.clicktrace.view.editor.AbstractEditorTest;
import com.niklim.clicktrace.view.editor.TestSessionsData;

public class SettingsTest extends AbstractEditorTest {
	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldSaveJiraConfig() throws InterruptedException {
		editorFixture.menuItemWithPath("Tools", "Settings").click();
		editorFixture.dialog().textBox("jiraUrl").setText("xyz");
		editorFixture.dialog().textBox("jiraUsername").setText("abc");

		editorFixture.dialog().button(JButtonMatcher.withText("Save")).click();

		AppProperties props = injector.getInstance(AppProperties.class);
		assertThat(props.getJiraConfig().getUrl()).isEqualTo("xyz");
		assertThat(props.getJiraConfig().getUsername()).isEqualTo("abc");

		editorFixture.menuItemWithPath("Tools", "Settings").click();
		editorFixture.dialog().textBox("jiraUrl").requireText("xyz");
		editorFixture.dialog().textBox("jiraUsername").requireText("abc");
	}
}
