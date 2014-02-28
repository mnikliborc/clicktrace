package com.niklim.clicktrace.capture.voter;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niklim.clicktrace.TimeMeter;

/**
 * Samples image horizontally and vertically with lines. Each line shifted by
 * LINE_SHIFT pixels from top to bottom and left to right. Each line is compared
 * with corresponding line of the second image.
 * 
 * If more than LINE_COUNT_THRESHOLD_COEFF lines differ, then change is
 * detected. Two lines differ when at least
 * {@link ChangeSensitivity#lineCountThresholdCoeff} their corresponding pixels
 * differ. Two pixels differ when: see {@link LineVoter#PIXEL_DIFF_THRESHOLD}
 * JavaDoc.
 * 
 */
public class LineVoter implements ChangeVoter {
	private static final Logger log = LoggerFactory.getLogger(LineVoter.class);

	/**
	 * Defines how sensitive {@link LineVoter} is.
	 */
	public static enum ChangeSensitivity {
		HIGH(0, 0.005, 10, 3), NORMAL(0.005, 0.01, 10, 5), LOW(0.05, 0.05, 30, 5);

		/**
		 * used to define when two corresponding lines differ
		 */
		final double lineDiffThresholdCoeff;

		/**
		 * used to define how many corresponding lines between two shots must
		 * differ to decide that the shots differ
		 */
		final double lineCountThresholdCoeff;

		/**
		 * defines when two pixels differ: put pixels in 3dim RGB space and
		 * calculate Manhattan distance, if distance is greater than
		 * PIXEL_DIFF_THRESHOLD, then pixels differ
		 */
		final int pixelDiffThreshold;

		/**
		 * defines line sampling rate in pixels. i.e. if lineShift = 5, then we
		 * take lines at 1st pixel, 6th pixel, 11th pixel...
		 */
		final int lineShift;

		ChangeSensitivity(double lineDiffThresholdCoeff, double lineCountThresholdCoeff, int pixelDiffThreshold,
				int lineShift) {
			this.lineDiffThresholdCoeff = lineDiffThresholdCoeff;
			this.lineCountThresholdCoeff = lineCountThresholdCoeff;
			this.pixelDiffThreshold = pixelDiffThreshold;
			this.lineShift = lineShift;
		}
	}

	private ChangeSensitivity sensitivity;

	public LineVoter(ChangeSensitivity sensitivity) {
		this.sensitivity = sensitivity;
	}

	/**
	 * Returns pixel from BufferedImage depending on line orientation.
	 */
	private static interface PixelPicker {
		Color pick(BufferedImage prev, int discretePos, int continuousPos);
	}

	PixelPicker verticalPixelPicker = new PixelPicker() {
		public Color pick(BufferedImage prev, int discretePos, int continuousPos) {
			return new Color(prev.getRGB(discretePos, continuousPos));
		}
	};

	PixelPicker horizontalPixelPicker = new PixelPicker() {
		public Color pick(BufferedImage prev, int discretePos, int continuousPos) {
			return new Color(prev.getRGB(continuousPos, discretePos));
		}
	};

	@Override
	public Vote vote(BufferedImage prev, BufferedImage current) {
		TimeMeter tm = TimeMeter.start("LineVoter.vote", log);

		if (prev.getWidth() != current.getWidth() || prev.getHeight() != current.getHeight()) {
			throw new RuntimeException("Prev and current images have different size");
		}
		int width = prev.getWidth();
		int height = prev.getHeight();

		int differingLinesCount = 0;
		double verticalLineDiffThreshold = height * sensitivity.lineDiffThresholdCoeff;
		double horizontalLineDiffThreshold = width * sensitivity.lineDiffThresholdCoeff;

		differingLinesCount += calculateDifferingLinesCount(prev, current, width, height, verticalLineDiffThreshold,
				verticalPixelPicker);

		differingLinesCount += calculateDifferingLinesCount(prev, current, height, width, horizontalLineDiffThreshold,
				horizontalPixelPicker);

		tm.stop();
		if (differingLinesCount > (width / sensitivity.lineShift + height / sensitivity.lineShift)
				* sensitivity.lineCountThresholdCoeff) {
			return Vote.SAVE;
		} else {
			return Vote.ABSTAIN;
		}
	}

	private int calculateDifferingLinesCount(BufferedImage prev, BufferedImage current, int lengthDiscrete,
			int lengthContinuous, double lineDiffThreshold, PixelPicker pixelPicker) {
		int differingLinesCounter = 0;
		for (int i = 0; i < lengthDiscrete; i += sensitivity.lineShift) {
			int pixelDiffCount = 0;
			for (int j = 0; j < lengthContinuous; j++) {
				Color prevColor = pixelPicker.pick(prev, i, j);
				Color currentColor = pixelPicker.pick(current, i, j);
				int pxDiff = calculatePixelDifference(prevColor, currentColor);
				if (pxDiff > sensitivity.pixelDiffThreshold) {
					pixelDiffCount++;
				}
			}
			if (lineDiffThreshold < pixelDiffCount) {
				differingLinesCounter++;
			}
		}
		return differingLinesCounter;
	}

	private int calculatePixelDifference(Color prevColor, Color currentColor) {
		int redDiff = Math.abs(prevColor.getRed() - currentColor.getRed());
		int greenDiff = Math.abs(prevColor.getGreen() - currentColor.getGreen());
		int blueDiff = Math.abs(prevColor.getBlue() - currentColor.getBlue());
		int diff = redDiff + greenDiff + blueDiff;
		return diff;
	}
}
