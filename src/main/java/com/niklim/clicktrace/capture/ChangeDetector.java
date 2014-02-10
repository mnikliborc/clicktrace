package com.niklim.clicktrace.capture;

import java.awt.image.BufferedImage;

import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.voter.ChangeVoter;
import com.niklim.clicktrace.capture.voter.Vote;

/**
 * Decides whether given screenshot should be saved, using {@link ChangeVoter}s.
 */
@Singleton
class ChangeDetector {
	private ChangeVoter voter;

	/**
	 * Decides whether change was detected (screenshot should be saved).
	 * 
	 * @param currentImage
	 *            current screenshot
	 * @param image
	 * @return true if given screenshot should be saved
	 */
	public synchronized boolean detect(BufferedImage lastImage, BufferedImage currentImage) {
		if (lastImage == null) {
			return true;
		}
		boolean shouldSave = performVoting(lastImage, currentImage);

		return shouldSave;
	}

	private boolean performVoting(BufferedImage lastImage, BufferedImage currentImage) {
		boolean shouldSave = false;

		Vote vote = voter.vote(lastImage, currentImage);
		if (vote == Vote.SAVE) {
			shouldSave = true;
		} else if (vote == Vote.DISCARD) {
			shouldSave = false;
		}

		return shouldSave;
	}

	public void setVoter(ChangeVoter voter) {
		this.voter = voter;
	}

}