package com.niklim.clicktrace.capture.voter;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ChangeVoter} measures pixels difference between previous and current
 * screenshot. When difference is greater than threshold, then it votes to save
 * current screenshot.
 */
public class PixelVoter implements ChangeVoter {
	private static final Logger log = LoggerFactory.getLogger(PixelVoter.class);

	private float imgDiffThresholdRatio = 0.02f;

	@Override
	public Vote vote(BufferedImage prev, BufferedImage current) {
		if (prev.getWidth() != current.getWidth() || prev.getHeight() != current.getHeight()) {
			throw new RuntimeException("Prev and current images have different size");
		}

		int imgDiff = 0;
		List<Point> diffPoints = new LinkedList<Point>();
		for (int i = 0; i < prev.getWidth(); i++) {
			for (int j = 0; j < prev.getHeight(); j++) {
				Color prevColor = new Color(prev.getRGB(i, j));
				Color currentColor = new Color(current.getRGB(i, j));

				int pixelDiff = calculateDifference(prevColor, currentColor);
				if (pixelDiff > 0) {
					diffPoints.add(new Point(i, j));
				}

				imgDiff += pixelDiff;
			}
		}

		return decide(prev.getWidth(), prev.getHeight(), imgDiff);
	}

	private Vote decide(int imgWidth, int imgHeight, int imgDiff) {
		int pixelsNum = imgWidth * imgHeight;
		if (imgDiff > pixelsNum * imgDiffThresholdRatio) {
			log.info("SAVE Vote");
			return Vote.SAVE;
		} else {
			log.info("ABSTAIN Vote");
			return Vote.ABSTAIN;
		}
	}

	private int calculateDifference(Color prevColor, Color currentColor) {
		int redDiff = Math.abs(prevColor.getRed() - currentColor.getRed());
		int greenDiff = Math.abs(prevColor.getGreen() - currentColor.getGreen());
		int blueDiff = Math.abs(prevColor.getBlue() - currentColor.getBlue());
		int diff = redDiff + greenDiff + blueDiff;
		return diff;
	}

}