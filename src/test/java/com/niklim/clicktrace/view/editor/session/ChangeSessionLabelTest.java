package com.niklim.clicktrace.view.editor.session;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.data.TableCell;
import org.junit.Test;

import com.niklim.clicktrace.view.editor.AbstractEditorTest;
import com.niklim.clicktrace.view.editor.TestSessionsData;

public class ChangeSessionLabelTest extends AbstractEditorTest {
	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldChangeLabel() throws InterruptedException {
		editorFixture.menuItemWithPath("File", "Open session").click();
		editorFixture.dialog().table().cell(TableCell.row(0).column(0)).requireValue("some label");
		editorFixture.dialog().table().cell(TableCell.row(0).column(0)).click();
		editorFixture.dialog().button(JButtonMatcher.withText("Open")).click();
		editorFixture.maximize();

		editorFixture.menuItemWithPath("Session", "Change label").click();
		editorFixture.optionPane().textBox().setText("another label");
		editorFixture.optionPane().okButton().click();

		editorFixture.menuItemWithPath("Session", "Refresh session").click();
		editorFixture.menuItemWithPath("File", "Open session").click();
		editorFixture.dialog().table().cell(TableCell.row(0).column(0)).requireValue("another label");
	}
}
