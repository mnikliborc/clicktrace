package com.niklim.clicktrace.capture.voter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

import com.niklim.clicktrace.capture.voter.LineVoter.ChangeSensitivity;

public class LineVoterTest {
	private static final String SESSION_DIR = "src/test/resources/test-data/voter-sessions/line-voter/";

	@Test
	public void normalShouldNotDetectAnyChange() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.NORMAL);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("1.png"));
		Assert.assertEquals(Vote.ABSTAIN, vote);
	}

	@Test
	public void normalShouldNotDetectCursor() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.NORMAL);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("2.png"));
		Assert.assertEquals(Vote.ABSTAIN, vote);
	}

	@Test
	public void normalShouldDetectCursorAndRect() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.NORMAL);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("3.png"));
		Assert.assertEquals(Vote.SAVE, vote);
	}

	@Test
	public void normalShouldDetectRect() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.NORMAL);
		Vote vote = voter.vote(loadImage("2.png"), loadImage("3.png"));
		Assert.assertEquals(Vote.SAVE, vote);
	}

	@Test
	public void highShouldNotDetectAnyChange() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.HIGH);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("1.png"));
		Assert.assertEquals(Vote.ABSTAIN, vote);
	}

	@Test
	public void highShouldDetectCursor() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.HIGH);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("2.png"));
		Assert.assertEquals(Vote.SAVE, vote);
	}

	@Test
	public void highShouldDetectCursorAndRect() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.HIGH);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("3.png"));
		Assert.assertEquals(Vote.SAVE, vote);
	}

	@Test
	public void highShouldDetectRect() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.HIGH);
		Vote vote = voter.vote(loadImage("2.png"), loadImage("3.png"));
		Assert.assertEquals(Vote.SAVE, vote);
	}

	@Test
	public void lowShouldNotDetectAnyChange() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.LOW);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("1.png"));
		Assert.assertEquals(Vote.ABSTAIN, vote);
	}

	@Test
	public void lowShouldNotDetectCursor() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.LOW);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("2.png"));
		Assert.assertEquals(Vote.ABSTAIN, vote);
	}

	@Test
	public void lowShouldNotDetectCursorAndRect() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.LOW);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("3.png"));
		Assert.assertEquals(Vote.ABSTAIN, vote);
	}

	@Test
	public void lowShouldNotDetectRect() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.LOW);
		Vote vote = voter.vote(loadImage("2.png"), loadImage("3.png"));
		Assert.assertEquals(Vote.ABSTAIN, vote);
	}

	@Test
	public void lowShouldDetectCircle() throws IOException {
		LineVoter voter = new LineVoter(ChangeSensitivity.LOW);
		Vote vote = voter.vote(loadImage("1.png"), loadImage("4.png"));
		Assert.assertEquals(Vote.SAVE, vote);
	}

	private BufferedImage loadImage(String name) throws IOException {
		return ImageIO.read(new File(SESSION_DIR + name));
	}
}
