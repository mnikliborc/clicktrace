package com.niklim.clicktrace.capture.voter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LineVoter implements ChangeVoter {
	
	private static final int LINE_SHIFT = 5;
	private static final double LINE_DIFF_THRESHOLD = 0.01;
	private static final double LINE_COUNT_THRESHOLD = 0.01;
	
	@Override
	public Vote vote(BufferedImage prev, BufferedImage current) {
		if (prev.getWidth() != current.getWidth() || prev.getHeight() != current.getHeight()) {
			throw new RuntimeException("Prev and current images have different size");
		}
		int width = prev.getWidth();
		int height = prev.getHeight();
		
		double lineDiff = height * LINE_DIFF_THRESHOLD;
		System.out.println("lineDiff=" + lineDiff);
		
		int diffLineCounter = 0;
		diffLineCounter += diffVertical(prev, current, width, height, lineDiff);
		// diffLineCounter += diffHorizontal(prev, current, width, height,
		// lineDiff);
		
		System.out.println("diffLineCounter=" + diffLineCounter);
		System.out.println("diffLineThreshold=" + (width / LINE_SHIFT + height / LINE_SHIFT) * LINE_COUNT_THRESHOLD);
		
		if (diffLineCounter > (width / LINE_SHIFT + height / LINE_SHIFT) * LINE_COUNT_THRESHOLD) {
			return Vote.SAVE;
		} else {
			return Vote.ABSTAIN;
		}
	}
	
	private int diffVertical(BufferedImage prev, BufferedImage current, int width, int height, double lineDiff) {
		int diffLineCounter = 0;
		for (int i = 0; i < width; i += LINE_SHIFT) {
			int pixelDiffSum = 0;
			for (int j = 0; j < height; j++) {
				Color prevColor = new Color(prev.getRGB(i, j));
				Color currentColor = new Color(current.getRGB(i, j));
				pixelDiffSum += calculateDifference(prevColor, currentColor);
			}
			if (lineDiff < pixelDiffSum) {
				diffLineCounter++;
			}
		}
		return diffLineCounter;
	}
	
	private int diffHorizontal(BufferedImage prev, BufferedImage current, int width, int height, double lineDiff) {
		int diffLineCounter = 0;
		for (int i = 0; i < height; i += LINE_SHIFT) {
			int pixelDiffSum = 0;
			for (int j = 0; j < width; j++) {
				Color prevColor = new Color(prev.getRGB(j, i));
				Color currentColor = new Color(current.getRGB(j, i));
				pixelDiffSum += calculateDifference(prevColor, currentColor);
			}
			if (lineDiff < pixelDiffSum) {
				diffLineCounter++;
			}
		}
		return diffLineCounter;
	}
	
	private int calculateDifference(Color prevColor, Color currentColor) {
		int redDiff = Math.abs(prevColor.getRed() - currentColor.getRed());
		int greenDiff = Math.abs(prevColor.getGreen() - currentColor.getGreen());
		int blueDiff = Math.abs(prevColor.getBlue() - currentColor.getBlue());
		int diff = redDiff + greenDiff + blueDiff;
		return diff;
	}
}
