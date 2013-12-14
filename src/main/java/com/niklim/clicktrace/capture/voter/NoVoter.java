package com.niklim.clicktrace.capture.voter;

import java.awt.image.BufferedImage;

/**
 * {@link ChangeVoter} deciding always to discard screenshot. For test purposes.
 */
public class NoVoter implements ChangeVoter {
	@Override
	public Vote vote(BufferedImage prev, BufferedImage current) {
		return Vote.DISCARD;
	}
}
