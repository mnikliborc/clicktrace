package com.niklim.clicktrace;

import org.apache.commons.lang.StringUtils;
import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.FrameFixture;

public class SystemTestSteps {
	public static void openSession(FrameFixture editorFixture, int sessionRow) {
		editorFixture.menuItemWithPath("File", "Open session").click();
		doubleClickSessionAndMaximize(editorFixture, sessionRow);
	}

	private static void doubleClickSessionAndMaximize(FrameFixture editorFixture, int sessionRow) {
		editorFixture.dialog().table().cell(TableCell.row(sessionRow).column(0)).doubleClick();
		editorFixture.maximize();
	}

	public static int openSession(FrameFixture editorFixture, String name) {
		editorFixture.menuItemWithPath("File", "Open session").click();

		int index = findSession(editorFixture, name);
		doubleClickSessionAndMaximize(editorFixture, index);
		return index;
	}

	private static int findSession(FrameFixture editorFixture, String name) {
		int index = 0;
		int rowCount = editorFixture.dialog().table().rowCount();
		for (; index < rowCount; index++) {
			String value = editorFixture.dialog().table().cell(TableCell.row(index).column(0)).value();
			if (StringUtils.equals(name, value)) {
				return index;
			}
		}
		throw new RuntimeException(String.format("Session '%s' was not found", name));
	}

	public static void deleteShot(FrameFixture editorFixture, int index) {
		editorFixture.comboBox().selectItem(index);
		editorFixture.button(JButtonMatcher.withText("delete")).click();
		editorFixture.optionPane().okButton().click();
	}

	public static void refreshSession(FrameFixture editorFixture) {
		editorFixture.menuItemWithPath("Session", "Refresh session").click();
	}
}
