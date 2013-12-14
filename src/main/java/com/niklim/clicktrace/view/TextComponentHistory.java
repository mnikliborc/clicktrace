package com.niklim.clicktrace.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.JTextComponent;

/**
 * Class providing support for undo/redo operation on {#link JTextComponent}.
 */
public class TextComponentHistory {
	LinkedList<String> stack = new LinkedList<String>();
	JTextComponent textcomponent;
	int index = 0;

	public static class DefaultKeyAdapter extends KeyAdapter {
		private TextComponentHistory history;

		public DefaultKeyAdapter(TextComponentHistory history) {
			this.history = history;
		}

		@Override
		public void keyReleased(KeyEvent event) {
			if (Character.isWhitespace(event.getKeyChar())) {
				history.store();
			} else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Z) {
				history.restorePrevious();
			} else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Y) {
				history.restoreForward();
			}
		}
	}

	public TextComponentHistory(JTextComponent textcomponent) {
		this.textcomponent = textcomponent;
	}

	public void restoreForward() {
		store();
		textcomponent.setText(forward());
		waitForEventPropagation();
	}

	public void restorePrevious() {
		store();
		textcomponent.setText(previous());
		waitForEventPropagation();
	}

	private void waitForEventPropagation() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void reset(String description) {
		stack.clear();
		stack.add(description);
		index = 0;
	}

	public void store() {
		String text = textcomponent.getText().trim();
		if (!text.equals(stack.get(index))) {
			List<String> toRemove = new ArrayList<String>(stack.subList(0, index));
			stack.removeAll(toRemove);
			stack.push(text);
			index = 0;
		}
	}

	private String previous() {
		index++;
		if (index >= stack.size()) {
			index = stack.size() - 1;
		}
		return stack.get(index);
	}

	private String forward() {
		index--;
		if (index < 0) {
			index = 0;
		}
		return stack.get(index);
	}
}