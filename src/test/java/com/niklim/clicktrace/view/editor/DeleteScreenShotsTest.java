package com.niklim.clicktrace.view.editor;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class DeleteScreenShotsTest extends AbstractEditorTest {

	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldDeleteAll() {
		// given
		EditorTestUtil.openSession(editorFixture, 0);

		// when
		editorFixture.menuItemWithPath("Session", "Select all screenshots").click();
		editorFixture.menuItemWithPath("Session", "Delete selected screenshots").click();
		editorFixture.optionPane().okButton().click();

		// then
		assertThat(editorFixture.comboBox().contents()).hasSize(1);
	}
}