package com.niklim.clicktrace.capture;

import java.awt.image.BufferedImage;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.voter.ChangeVoter;
import com.niklim.clicktrace.capture.voter.Vote;

/**
 * Decides whether given screenshot should be saved, using {@link ChangeVoter}s.
 */
@Singleton
class ChangeDetector {
	private static final Logger log = LoggerFactory.getLogger(ChangeDetector.class);

	@Inject
	List<ChangeVoter> voters;

	private BufferedImage prevImage;

	/**
	 * Decides whether change was detected (screenshot should be saved).
	 * 
	 * @param currentImage current screenshot
	 * @return true if given screenshot should be saved
	 */
	public synchronized boolean detect(BufferedImage currentImage) {
		if (prevImage == null) {
			prevImage = currentImage;
			return true;
		}

		boolean shouldSave = performVoting(currentImage);

		prevImage = currentImage;
		return shouldSave;
	}

	public boolean performVoting(BufferedImage currentImage) {
		boolean shouldSave = false;
		for (ChangeVoter voter : voters) {
			Vote vote = voter.vote(prevImage, currentImage);
			if (vote == Vote.SAVE) {
				shouldSave = true;
				break;
			} else if (vote == Vote.DISCARD) {
				shouldSave = false;
				break;
			}
		}
		return shouldSave;
	}

	public void reset() {
		prevImage = null;
	}
}