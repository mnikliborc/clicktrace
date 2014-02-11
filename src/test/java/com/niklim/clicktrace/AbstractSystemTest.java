package com.niklim.clicktrace;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;

import com.google.inject.Injector;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.view.MainView;

public abstract class AbstractSystemTest {
	protected FrameFixture editorFixture;
	protected ActiveSession activeSession;
	protected MainView mainView;

	protected Injector injector;

	abstract protected TestSessionsData getSessionsData();

	@Before
	public void setUp() {
		prepareTestDir();
		deleteUserProperties();

		injector = loadInjector();
		mainView = injector.getInstance(MainView.class);
		editorFixture = new FrameFixture(mainView.getFrame());
		editorFixture.show();
		editorFixture.maximize();

		activeSession = injector.getInstance(ActiveSession.class);
	}

	private void deleteUserProperties() {
		File file = new File("user.properties");
		if (file.exists()) {
			file.delete();
		}
	}

	protected Injector loadInjector() {
		return DefaultTestGuiceContext.load();
	}

	private void prepareTestDir() {
		FileManager.SESSIONS_DIR = "target/testsessions/";
		File sessionDir = new File(FileManager.SESSIONS_DIR);

		if (!sessionDir.exists()) {
			sessionDir.mkdir();
		} else {
			fileCleanup();
		}

		copySessions();
	}

	private void copySessions() {
		try {
			File testSessionDir = new File("src/test/resources/" + getSessionsData().getPath());
			for (File session : testSessionDir.listFiles()) {
				if (session.isDirectory()) {
					FileUtils.copyDirectory(session, new File(FileManager.SESSIONS_DIR + session.getName()));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void fileCleanup() {
		File sessionDir = new File(FileManager.SESSIONS_DIR);

		for (File file : sessionDir.listFiles()) {
			if (file.isDirectory()) {
				for (File sub : file.listFiles()) {
					sub.delete();
				}
			}
			file.delete();
		}
	}

	@After
	public void fixtureCleanup() {
		if (editorFixture != null) {
			editorFixture.cleanUp();
		}
		deleteUserProperties();
	}

	protected void delayLong() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void delay() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
