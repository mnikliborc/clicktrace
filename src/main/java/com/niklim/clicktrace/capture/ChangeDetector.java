package com.niklim.clicktrace.capture;

import java.awt.image.BufferedImage;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.voter.ChangeVoter;
import com.niklim.clicktrace.capture.voter.Vote;

/**
 * Decides whether given screenshot should be saved, using {@link ChangeVoter}s.
 */
@Singleton
class ChangeDetector {
	@Inject
	List<ChangeVoter> voters;
	
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
		long currentTimeMillis = System.currentTimeMillis();
		boolean shouldSave = performVoting(lastImage, currentImage);
		System.out.println("detectorTime=" + (System.currentTimeMillis() - currentTimeMillis));
		
		return shouldSave;
	}
	
	private boolean performVoting(BufferedImage lastImage, BufferedImage currentImage) {
		boolean shouldSave = false;
		for (ChangeVoter voter : voters) {
			Vote vote = voter.vote(lastImage, currentImage);
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
	
}