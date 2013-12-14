package com.niklim.clicktrace.controller.operation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Base class for reusable Clicktrace operations.
 */
public abstract class AbstractOperation {
	public abstract void perform();

	/**
	 * Creates {@link ActionListener} triggering this.perform() on action event.
	 * 
	 * @return
	 */
	public ActionListener action() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				perform();
			}
		};
	}

	/**
	 * Creates {@link MouseListener} triggering this.perform() on mouse click.
	 * 
	 * @return
	 */
	public MouseListener mouse() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				perform();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		};
	}
}
