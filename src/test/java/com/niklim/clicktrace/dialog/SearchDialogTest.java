package com.niklim.clicktrace.dialog;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.data.TableCell;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.TestSessionsData;
import com.niklim.clicktrace.controller.ActiveSession;

public class SearchDialogTest extends AbstractSystemTest {
	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	@Test
	public void shouldFindByDescription() throws InterruptedException {
		editorFixture.menuItemWithPath("Tools", "Find").click();
		editorFixture.dialog().textBox().setText("desc");

		editorFixture.dialog().button(JButtonMatcher.withText("Search")).click();

		editorFixture.dialog().table().requireCellValue(TableCell.row(0).column(3), "some description");

		editorFixture.dialog().table().selectRows(0).doubleClick();

		assertThat(editorFixture.comboBox().contents()).hasSize(4);

		ActiveSession activeSession = injector.getInstance(ActiveSession.class);
		assertThat(activeSession.isSessionLoaded()).isTrue();
		assertThat(activeSession.getSession()).isNotNull();
		assertThat(activeSession.getSession().getName()).isEqualTo("one");
	}
}
