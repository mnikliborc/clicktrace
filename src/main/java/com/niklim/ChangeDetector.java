package com.niklim;

import java.awt.image.BufferedImage;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

class ChangeDetector {
	private static final Logger log = LoggerFactory.getLogger(ChangeDetector.class);

	@Inject
	List<ChangeVoter> voters;

	private BufferedImage prevImage;

	public boolean detect(BufferedImage currentImage) {
		if (prevImage == null) {
			return true;
		}

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

		log.info("save decision = " + shouldSave);
		prevImage = currentImage;
		return shouldSave;
	}
}