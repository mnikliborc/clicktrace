package com.niklim;

import java.awt.image.BufferedImage;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

class ChangeDetector {
	private static final Logger log = LoggerFactory.getLogger(PixelVoter.class);

	@Inject
	List<ChangeVoter> voters;

	private BufferedImage prevImage;

	public boolean detect(BufferedImage currentImage) {
		BufferedImage tempPrevImage = prevImage;
		prevImage = currentImage;

		if (tempPrevImage == null) {
			return true;
		}

		for (ChangeVoter voter : voters) {
			switch (voter.vote(tempPrevImage, currentImage)) {
			case SAVE:
				return true;
			case DISCARD:
				return false;
			default:
				continue;
			}
		}

		return false;
	}
}