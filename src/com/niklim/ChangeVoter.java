package com.niklim;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ChangeVoter {
	Vote vote(BufferedImage prev, BufferedImage current);
}

enum Vote {
	SAVE, DISCARD, INDIFFERENT;
}

class PixelVoter implements ChangeVoter {
	private static final Logger log = LoggerFactory.getLogger(PixelVoter.class);

	@Override
	public Vote vote(BufferedImage prev, BufferedImage current) {
		if (prev.getWidth() != current.getWidth() || prev.getHeight() != current.getHeight()) {
			throw new RuntimeException("Prev and current images have different size");
		}

		for (int i = 0; i < prev.getWidth(); i++) {
			for (int j = 0; j < prev.getHeight(); i++) {
				if (prev.getRGB(i, j) != current.getRGB(i, j)) {
					log.info("SAVE Vote");
					return Vote.SAVE;
				}
			}
		}

		log.info("INDIFFERENT Vote");
		return Vote.INDIFFERENT;
	}
}

class BlindVoter implements ChangeVoter {
	@Override
	public Vote vote(BufferedImage prev, BufferedImage current) {
		return Vote.SAVE;
	}
}