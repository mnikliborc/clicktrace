package com.niklim.clicktrace;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.fest.swing.fixture.FrameFixture;
import org.junit.Before;

import com.google.inject.Injector;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.view.editor.Editor;

public abstract class AbstractSystemTest {
	protected FrameFixture editorFixture;
	protected ActiveSession activeSession;
	protected Editor editor;

	protected Injector injector;

	abstract protected TestSessionsData getSessionsData();

	@Before
	public void setUp() {
		prepareTestDir();

		injector = loadInjector();
		editor = injector.getInstance(Editor.class);
		editorFixture = new FrameFixture(editor.getFrame());
		editorFixture.show();

		activeSession = injector.getInstance(ActiveSession.class);
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
			cleanup();
		}

		copySessions();
	}

	private void copySessions() {
		try {
			File testSessionDir = new File("src/test/resources/"
					+ getSessionsData().getPath());
			for (File session : testSessionDir.listFiles()) {
				FileUtils.copyDirectory(session, new File(
						FileManager.SESSIONS_DIR + session.getName()));
			}
		} catch (IOException e) {
			new RuntimeException(e);
		}
	}

	// @After
	public void cleanup() {
		File sessionDir = new File(FileManager.SESSIONS_DIR);

		for (File file : sessionDir.listFiles()) {
			for (File sub : file.listFiles()) {
				sub.delete();
			}
			file.delete();
		}

		if (editorFixture != null) {
			editorFixture.cleanUp();
		}
	}
}
