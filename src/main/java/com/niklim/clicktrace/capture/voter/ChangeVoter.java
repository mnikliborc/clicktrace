package com.niklim.clicktrace.capture.voter;

import java.awt.image.BufferedImage;

/**
 * Decides whether there was a significant change between two following
 * screenshots.
 */
public interface ChangeVoter {
	/**
	 * Decision whether screenshot should be saved.
	 * 
	 * @param prev last screenshot
	 * @param current current screenshot
	 * @return decision whether screenshot should be saved
	 */
	Vote vote(BufferedImage prev, BufferedImage current);
}
