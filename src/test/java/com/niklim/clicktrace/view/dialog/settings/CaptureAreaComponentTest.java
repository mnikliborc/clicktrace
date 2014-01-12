package com.niklim.clicktrace.view.dialog.settings;

import static org.fest.assertions.Assertions.assertThat;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.fixture.DialogFixture;
import org.junit.Test;

import com.niklim.clicktrace.AbstractSystemTest;
import com.niklim.clicktrace.TestSessionsData;
import com.niklim.clicktrace.capture.ScreenUtils;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.view.dialog.settings.CaptureAreaComponent.PointWidget;

public class CaptureAreaComponentTest extends AbstractSystemTest {
	@Override
	protected TestSessionsData getSessionsData() {
		return TestSessionsData.SOME;
	}

	private Robot robot() {
		try {
			return new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void shouldSetAndCancelAndSetCaptureArea() throws InterruptedException {
		editorFixture.menuItemWithPath("Tools", "Settings").click();

		DialogFixture dialog = editorFixture.dialog();

		dialog.checkBox(CaptureAreaComponent.FULL_SCREEN_CHECKBOX_NAME).click();
		assertButtonsTextIsChange(dialog);

		testSettingAndCancellingPoint(dialog, PointWidget.START, new Point(400, 600), new Point(0, 0));
		assertButtonsTextIsChange(dialog);

		testSettingAndCancellingPoint(dialog, PointWidget.END, new Point(200, 300),
				new Point(ScreenUtils.getPrimarySize().width, ScreenUtils.getPrimarySize().height));
		assertButtonsTextIsChange(dialog);

		dialog.button(JButtonMatcher.withText("Save")).click();

		UserProperties props = injector.getInstance(UserProperties.class);
		assertThat(props.getCaptureFullScreen()).isFalse();
		assertThat(props.getCaptureRectangle()).isEqualTo(new Rectangle(200, 300, 200, 300));
	}

	private void testSettingAndCancellingPoint(DialogFixture dialog, PointWidget pointWidget, Point targetPoint,
			Point defaultPoint) {
		dialog.button(pointWidget.buttonName).click();
		dialog.label(JLabelMatcher.withText(pointWidget.labelText)).requireVisible();

		moveMouseToPointAndAssertPointTextFieldText(dialog, pointWidget.textFieldName, targetPoint, targetPoint);

		cancelSettingPoint(dialog, pointWidget.buttonName, pointWidget.textFieldName, defaultPoint);
		setPoint(dialog, pointWidget.buttonName, targetPoint);

		// let CTRL+ENTER KeyEvent be caught by listener
		delay();

		moveMouseToPointAndAssertPointTextFieldText(dialog, pointWidget.textFieldName, new Point(targetPoint.x + 10,
				targetPoint.y + 10), targetPoint);
	}

	private void setPoint(DialogFixture dialog, String buttonName, Point point) {
		dialog.button(buttonName).click();
		Robot robot = robot();
		robot.mouseMove(point.x, point.y);

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	private void cancelSettingPoint(DialogFixture dialog, String buttonName, String textfieldName, Point defaultPoint) {
		dialog.button(buttonName).click();
		assertThat(dialog.textBox(textfieldName).text()).isEqualTo(CaptureAreaComponent.pointToStr(defaultPoint));
	}

	private void moveMouseToPointAndAssertPointTextFieldText(DialogFixture dialog, String textfieldName,
			Point targetPoint, Point expectedTextFieldPoint) {
		robot().mouseMove(targetPoint.x, targetPoint.y);
		assertThat(dialog.textBox(textfieldName).text()).isEqualTo(
				CaptureAreaComponent.pointToStr(expectedTextFieldPoint));
	}

	private void assertButtonsTextIsChange(DialogFixture dialog) {
		assertThat(dialog.button(PointWidget.START.buttonName).text()).isEqualTo(CaptureAreaComponent.CHANGE_TXT);
		assertThat(dialog.button(PointWidget.END.buttonName).text()).isEqualTo(CaptureAreaComponent.CHANGE_TXT);
	}
}
