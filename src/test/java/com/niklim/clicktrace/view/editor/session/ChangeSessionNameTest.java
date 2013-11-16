package com.niklim.clicktrace.view.editor.session;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.data.TableCell;
import org.junit.Test;

import com.niklim.clicktrace.view.editor.AbstractEditorTest;
import com.niklim.clicktrace.view.editor.TestSessionsData;

public class ChangeSessionNameTest extends AbstractEditorTest {
	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldChangeName() throws InterruptedException {
		editorFixture.menuItemWithPath("File", "Open session").click();
		editorFixture.dialog().table().cell(TableCell.row(0).column(0)).requireValue("one");
		editorFixture.dialog().table().cell(TableCell.row(0).column(0)).click();
		editorFixture.dialog().button(JButtonMatcher.withText("Open")).click();
		editorFixture.maximize();

		editorFixture.menuItemWithPath("Session", "Change name").click();
		editorFixture.optionPane().textBox().setText("not one");
		editorFixture.optionPane().okButton().click();

		editorFixture.menuItemWithPath("Session", "Refresh session").click();
		editorFixture.menuItemWithPath("File", "Open session").click();
		editorFixture.dialog().table().cell(TableCell.row(0).column(0)).requireValue("not one");
	}
}
