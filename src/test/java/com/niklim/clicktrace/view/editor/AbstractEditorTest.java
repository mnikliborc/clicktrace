package com.niklim.clicktrace.view.editor;

import java.io.File;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;

import com.google.inject.Injector;
import com.niklim.clicktrace.ImageFileManager;

public class AbstractEditorTest {
	protected FrameFixture editorFixture;
	protected Injector injector;

	@Before
	public void setUp() {
		prepareTestDir();

		injector = TestGuiceContext.load();
		Editor editor = injector.getInstance(Editor.class);
		editorFixture = new FrameFixture(editor.getFrame());
		editorFixture.show();
	}

	private void prepareTestDir() {
		ImageFileManager.SESSIONS_DIR = "target/testsessions/";
		File sessionDir = new File(ImageFileManager.SESSIONS_DIR);

		if (!sessionDir.exists()) {
			sessionDir.mkdir();
		} else {
			cleanup();
		}
	}

	@After
	public void cleanup() {
		File sessionDir = new File(ImageFileManager.SESSIONS_DIR);

		for (File file : sessionDir.listFiles()) {
			file.delete();
		}

		if (editorFixture != null) {
			editorFixture.cleanUp();
		}
	}
}
