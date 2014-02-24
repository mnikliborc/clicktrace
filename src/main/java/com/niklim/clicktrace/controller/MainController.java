package com.niklim.clicktrace.controller;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.capture.CaptureManager;
import com.niklim.clicktrace.controller.operation.session.NewSessionOperation;
import com.niklim.clicktrace.dialog.NewSessionDialog.NewSessionCallback;
import com.niklim.clicktrace.dialog.settings.SettingsDialog;
import com.niklim.clicktrace.editor.EditorFrame;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.dao.SessionPropertiesWriter;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.SessionManager;
import com.niklim.clicktrace.service.exception.SessionAlreadyExistsException;
import com.niklim.clicktrace.view.MainView;

/**
 * Main controller.
 */
@Singleton
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Inject
	private CaptureManager capture;

	@Inject
	private MainView mainView;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private SessionManager sessionManager;

	@Inject
	private UserProperties props;

	@Inject
	private SettingsDialog settingsDialog;

	@Inject
	private NewSessionOperation newSessionOperation;

	@Inject
	private NavigationController navigationController;

	@Inject
	private EditorFrame editor;

	@Inject
	public void registerRefreshOnFrameResize() {
		mainView.getFrame().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				refreshScreenShot();
			}
		});
	}

	/**
	 * Opens the app window.
	 */
	public void init() {
		mainView.showSplashScreen();
		mainView.open();
	}

	public void startRecording(boolean hideEditor) {
		if (!activeSession.isSessionLoaded()) {
			newSessionOperation.createSession(new NewSessionCallback() {
				@Override
				public boolean create(String name, String description) {
					boolean created = newSession(name, description);
					if (created) {
						newSessionOperation.closeDialog();
						startRecording(true);
					}
					return created;
				}
			});
			return;
		}

		activeSession.setRecording(true);

		mainView.sessionStateChanged();
		if (hideEditor) {
			mainView.hide();
		}

		capture.start();
	}

	public void stopRecording() {
		if (!activeSession.isSessionLoaded()) {
			return;
		}

		mainView.showWaitingCursor();

		activeSession.setRecording(false);
		mainView.sessionStateChanged();
		capture.stop();

		refreshSession();
		int index = activeSession.getActiveShotIndex();
		if (index >= 0) {
			navigationController.showScreenShot(index);
		}

		if (props.getCaptureSelectAll()) {
			setSelectedAllScreenshots(true);
		}

		mainView.hideWaitingCursor();
	}

	public boolean newSession(String sessionName, String description) {
		try {
			Session session = sessionManager.createSession(sessionName);

			handleNewSessionDescription(description, session);

			showSession(session);
			return true;
		} catch (IOException e) {
			log.error("", e);
			JOptionPane.showMessageDialog(mainView.getFrame(), ErrorMsgs.SESSION_NAME_WRONG_FOLDER);
		} catch (SessionAlreadyExistsException e) {
			JOptionPane.showMessageDialog(mainView.getFrame(), ErrorMsgs.SESSION_NAME_ALREADY_EXIST);
		}
		return false;
	}

	private void handleNewSessionDescription(String description, Session session) {
		if (!StringUtils.isEmpty(description)) {
			session.setDescription(description);
			SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(session);
			writer.saveSessionDescription();
		}
	}

	public void showSession(Session session) {
		capture.stop();

		setActiveSession(session);

		mainView.showSession(session);
		mainView.sessionStateChanged();

		if (session != null) {
			props.setLastSessionName(session.getName());
			props.save();
		}
	}

	private void setActiveSession(Session session) {
		activeSession.setSession(session);
		activeSession.setRecording(false);
		activeSession.setFirstShotActive();
	}

	public void deleteActiveSession() {
		Session session = activeSession.getSession();
		if (session == null) {
			return;
		}

		capture.stop();
		session.delete();

		activeSession.setSession(null);
		activeSession.setRecording(false);

		mainView.sessionStateChanged();
		mainView.hideSession();
	}

	public void setSelectedAllScreenshots(boolean selected) {
		mainView.setSelectedActiveScreenShot(selected);
		if (activeSession.isSessionLoaded()) {
			activeSession.setAllShotsSelected(selected);
		}
	}

	public void refreshSession() {
		Session session = activeSession.getSession();
		if (session == null) {
			return;
		}

		session.loadScreenShots();
		showSession(session);
	}

	public void refreshScreenShot() {
		ScreenShot shot = activeSession.getActiveShot();
		if (shot == null) {
			return;
		}

		shot.loadImage();
		mainView.showScreenShot(shot, activeSession.isShotSelected(shot));
		mainView.refresh();
	}

	public void editScreenShot() {
		ScreenShot activeShot = activeSession.getActiveShot();
		if (activeShot == null) {
			return;
		}

		if (Strings.isNullOrEmpty(props.getImageEditorPath())) {
			JOptionPane.showMessageDialog(mainView.getFrame(), InfoMsgs.CONFIG_NO_EDITOR_PATH);
			settingsDialog.open();
		} else {
			try {
				ProcessBuilder pb = new ProcessBuilder(props.getImageEditorPath(), "sessions" + File.separator
						+ activeShot.getSession().getName() + File.separator + activeShot.getFilename());
				pb.start();
			} catch (IOException e) {
				log.error(InfoMsgs.EDITOR_APP_ERROR, e);
				ErrorNotifier.interrupt(InfoMsgs.EDITOR_APP_ERROR);
			}
		}
	}

	public void deleteActiveScreenShot() {
		ScreenShot shot = activeSession.getActiveShot();
		if (shot == null) {
			return;
		}

		int indexOfNewActive = Math.max(0, activeSession.getSession().getShots().indexOf(shot) - 1);

		activeSession.removeShot(shot);
		shot.delete();

		ScreenShot newShot = activeSession.getShot(indexOfNewActive);

		mainView.resetControl(activeSession.getSession());
		mainView.showScreenShot(newShot, activeSession.isShotSelected(newShot));
		mainView.refresh();
	}

	public void deleteSelectedScreenshots() {
		for (ScreenShot shot : activeSession.getSelectedShots().toArray(new ScreenShot[0])) {
			activeSession.removeShot(shot);
			shot.delete();
		}

		activeSession.setFirstShotActive();

		mainView.showSession(activeSession.getSession());
		if (!activeSession.isShotSelected(activeSession.getActiveShot())) {
			mainView.showScreenShot(activeSession.getActiveShot(), false);
		}
		activeSession.setAllShotsSelected(false);
		mainView.sessionStateChanged();
	}

	public void selectScreenShot(boolean selected) {
		if (selected) {
			activeSession.selectShot(activeSession.getActiveShot());
		} else {
			activeSession.deselectShot(activeSession.getActiveShot());
		}
	}

	public void toggleSelectScreenShot() {
		boolean selected = activeSession.getSelectedShots().contains(activeSession.getActiveShot());
		selectScreenShot(!selected);
		mainView.setSelectedActiveScreenShot(!selected);
	}

	public void changeActiveScreenShotLabel(String label) {
		ScreenShot activeShot = activeSession.getActiveShot();
		activeShot.setLabel(label);

		SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(activeSession.getSession());
		writer.saveShotLabel(activeSession.getActiveShot());

		mainView.refresh();
	}

	public void saveActiveScreenShotDescription() {
		SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(activeSession.getSession());
		writer.saveShotDescription(activeSession.getActiveShot());
	}

	public void changeActiveSessionName(String name) {
		Session session = activeSession.getSession();
		try {
			sessionManager.changeSessionName(session, name);
			mainView.changeAppTitle(session);
			props.setLastSessionName(session.getName());
			props.save();
		} catch (IOException e) {
			log.error("", e);
			JOptionPane.showMessageDialog(mainView.getFrame(), ErrorMsgs.SESSION_NAME_WRONG_FOLDER);
		} catch (SessionAlreadyExistsException e) {
			JOptionPane.showMessageDialog(mainView.getFrame(), ErrorMsgs.SESSION_NAME_ALREADY_EXIST);
		}
	}

	public void openSessionOnScreenShot(ScreenShot selectedShot) {
		showSession(selectedShot.getSession());
		mainView.showScreenShot(selectedShot, false);
	}

}
