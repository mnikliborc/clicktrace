package com.niklim.clicktrace.controller.operation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class AbstractOperation {
	public abstract void perform();

	public ActionListener action() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				perform();
			}
		};
	}

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
