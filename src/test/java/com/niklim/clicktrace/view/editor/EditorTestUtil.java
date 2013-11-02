package com.niklim.clicktrace.view.editor;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.FrameFixture;

public class EditorTestUtil {
	public static void openSession(FrameFixture editorFixture, int sessionRow) {
		editorFixture.menuItemWithPath("File", "Open session").click();
		editorFixture.dialog().table().cell(TableCell.row(sessionRow).column(0)).click();
		editorFixture.dialog().button(JButtonMatcher.withText("Open")).click();
	}
}
