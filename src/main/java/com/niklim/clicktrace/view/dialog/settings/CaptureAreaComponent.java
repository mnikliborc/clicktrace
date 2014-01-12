package com.niklim.clicktrace.view.dialog.settings;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;

import com.google.common.base.Optional;
import com.niklim.clicktrace.capture.ScreenUtils;

/**
 * Responsible for setting capture area. User may choose full screen or a
 * fragment of screen confined by two points.
 */
public class CaptureAreaComponent {
	static final String FULL_SCREEN_CHECKBOX_NAME = "fullScreenCheckbox";

	static enum PointWidget {
		START("startPanel", "startButton", "startTextFieldName", "start point"), END("endPanel", "endButton",
				"endTextFieldName", "end point");
		String panelName;
		String buttonName;
		String textFieldName;
		String labelText;

		PointWidget(String panelName, String buttonName, String textFieldName, String labelText) {
			this.panelName = panelName;
			this.buttonName = buttonName;
			this.textFieldName = textFieldName;
			this.labelText = labelText;
		}
	}

	static final String CHANGE_TXT = "change";
	static final String CANCEL_TXT = "cancel";

	static final String FULLSCREEN_TXT = "full screen";
	static final String SET_POINT_TXT = "Press CTRL+ENTER to set point.";

	private JDialog settingsDialog;

	private JCheckBox fullScreen;
	private JTextField startPointField;
	private JTextField endPointField;

	private JPanel startPanel;
	private JPanel endPanel;
	private JPanel infoPanel;

	private JButton startButton;
	private JButton endButton;

	private static interface MouseMoveAction {
		void moved(int x, int y);
	}

	private static class MouseMoveCapture implements NativeMouseMotionListener {
		private MouseMoveAction action;

		public MouseMoveCapture() {
			GlobalScreen.getInstance().addNativeMouseMotionListener(this);
		}

		public void setAction(MouseMoveAction action) {
			this.action = action;
		}

		public void cancelAction() {
			this.action = null;
		}

		public void nativeMouseMoved(NativeMouseEvent e) {
			if (action != null) {
				action.moved(e.getX(), e.getY());
			}
		}

		public void nativeMouseDragged(NativeMouseEvent arg0) {
		}
	};

	private class AreaSettingAction implements ActionListener {

		private JTextField pointTextField;
		private JButton button;

		private MouseMoveCapture capture = new MouseMoveCapture();

		private Point oldPoint;
		private Point newPoint = new Point();

		public AreaSettingAction(Point oldPoint, JButton button, JTextField pointTextField) {
			this.oldPoint = oldPoint;
			this.button = button;
			this.pointTextField = pointTextField;
		}

		MouseMoveAction pointTextFieldUpdaterOnMouseMove = new MouseMoveAction() {
			public void moved(int x, int y) {
				newPoint.x = x;
				newPoint.y = y;
				pointTextField.setText(pointToStr(newPoint));
			}
		};

		ActionListener cancelSettingPoint = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				awaitSetting();
				newPoint = new Point(oldPoint);
				pointTextField.setText(pointToStr(newPoint));
			}
		};

		ActionListener acceptSettingPoint = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				awaitSetting();
			}
		};

		public void actionPerformed(ActionEvent e) {
			button.setText(CANCEL_TXT);
			infoPanel.setVisible(true);
			settingsDialog.pack();

			capture.setAction(pointTextFieldUpdaterOnMouseMove);

			replaceButtonListener(this, cancelSettingPoint);

			settingsDialog.getRootPane().registerKeyboardAction(acceptSettingPoint,
					KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK),
					JComponent.WHEN_IN_FOCUSED_WINDOW);
		}

		private void awaitSetting() {
			button.setText(CHANGE_TXT);
			infoPanel.setVisible(false);
			settingsDialog.pack();

			capture.cancelAction();
			settingsDialog.getRootPane().unregisterKeyboardAction(
					KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK));

			replaceButtonListener(cancelSettingPoint, this);
		}

		private void replaceButtonListener(ActionListener oldListener, ActionListener newListener) {
			button.removeActionListener(oldListener);
			button.addActionListener(newListener);
		}
	}

	private AreaSettingAction startSettingAction;
	private AreaSettingAction endSettingAction;

	public CaptureAreaComponent(final JDialog settingsDialog) {
		this.settingsDialog = settingsDialog;

		fullScreen = new JCheckBox(FULLSCREEN_TXT);
		fullScreen.setName(FULL_SCREEN_CHECKBOX_NAME);

		startPointField = new JTextField();
		startPointField.setName(PointWidget.START.textFieldName);
		startPointField.setEditable(false);

		endPointField = new JTextField();
		endPointField.setName(PointWidget.END.textFieldName);
		endPointField.setEditable(false);

		startButton = new JButton(CHANGE_TXT);
		startButton.setName(PointWidget.START.buttonName);
		endButton = new JButton(CHANGE_TXT);
		endButton.setName(PointWidget.END.buttonName);

		startPanel = new JPanel(new MigLayout());
		startPanel.setName(PointWidget.START.panelName);
		startPanel.add(new JLabel(PointWidget.START.labelText), "w 65");
		startPanel.add(startPointField, "w 100");
		startPanel.add(startButton);

		endPanel = new JPanel(new MigLayout());
		endPanel.setName(PointWidget.END.panelName);
		endPanel.add(new JLabel(PointWidget.END.labelText), "w 65");
		endPanel.add(endPointField, "w 100");
		endPanel.add(endButton);

		infoPanel = new JPanel(new MigLayout());
		infoPanel.add(new JLabel(SET_POINT_TXT));
		infoPanel.setVisible(false);

		layoutWidgets(settingsDialog);

		fullScreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				startPanel.setVisible(!fullScreen.isSelected());
				endPanel.setVisible(!fullScreen.isSelected());
				infoPanel.setVisible(infoPanel.isVisible() && !fullScreen.isSelected());

				settingsDialog.pack();
			}
		});
	}

	public void layoutWidgets(JDialog dialog) {
		dialog.add(new JLabel("Capture area"));
		dialog.add(fullScreen, "wrap");

		layoutPanel(dialog, startPanel);
		layoutPanel(dialog, endPanel);
		layoutPanel(dialog, infoPanel);
	}

	private void layoutPanel(JDialog dialog, JPanel panel) {
		JPanel gapPanel = new JPanel();
		gapPanel.setVisible(false);
		dialog.add(gapPanel);
		dialog.add(panel, "span 2, wrap");
	}

	public void init(boolean captureFullScreen, Rectangle captureRectangle) {
		this.fullScreen.setSelected(captureFullScreen);

		Model model = createModel(captureRectangle);

		if (captureRectangle != null) {
			model.startPoint = new Point(captureRectangle.x, captureRectangle.y);
			model.endPoint = new Point(captureRectangle.x + captureRectangle.width, captureRectangle.y
					+ captureRectangle.height);
		} else {
			Dimension screenSize = ScreenUtils.getPrimarySize();
			model.startPoint = new Point(0, 0);
			model.endPoint = new Point(screenSize.width, screenSize.height);
		}

		startPointField.setText(pointToStr(model.startPoint));
		endPointField.setText(pointToStr(model.endPoint));

		startSettingAction = new AreaSettingAction(model.startPoint, startButton, startPointField);
		startButton.addActionListener(startSettingAction);
		endSettingAction = new AreaSettingAction(model.endPoint, endButton, endPointField);
		endButton.addActionListener(endSettingAction);
	}

	private Model createModel(Rectangle captureRectangle) {
		final Point startPoint;
		final Point endPoint;

		if (captureRectangle != null) {
			startPoint = new Point(captureRectangle.x, captureRectangle.y);
			endPoint = new Point(captureRectangle.x + captureRectangle.width, captureRectangle.y
					+ captureRectangle.height);
		} else {
			Dimension screenSize = ScreenUtils.getPrimarySize();
			startPoint = new Point(0, 0);
			endPoint = new Point(screenSize.width, screenSize.height);
		}
		return new Model(startPoint, endPoint);
	}

	private static class Model {
		Point startPoint;
		Point endPoint;

		public Model(Point startPoint, Point endPoint) {
			this.startPoint = startPoint;
			this.endPoint = endPoint;
		}
	}

	static String pointToStr(Point point) {
		return point.x + "," + point.y;
	}

	public Optional<Rectangle> getCaptureRectangleOpt() {
		if (fullScreen.isSelected()) {
			return Optional.<Rectangle> absent();
		}

		int leftTopX = Math.min(startSettingAction.newPoint.x, endSettingAction.newPoint.x);
		int leftTopY = Math.min(startSettingAction.newPoint.y, endSettingAction.newPoint.y);
		int width = Math.abs(startSettingAction.newPoint.x - endSettingAction.newPoint.x);
		int height = Math.abs(startSettingAction.newPoint.y - endSettingAction.newPoint.y);

		return Optional.of(new Rectangle(leftTopX, leftTopY, width, height));
	}
}
