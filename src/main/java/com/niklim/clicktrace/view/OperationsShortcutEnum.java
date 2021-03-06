package com.niklim.clicktrace.view;

import java.awt.event.KeyEvent;

import com.niklim.clicktrace.controller.hook.GlobalKeyboardListenerImpl;

public enum OperationsShortcutEnum {
	// @formatter:off
	SCROLL_UP("", KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK),
	SCROLL_DOWN("", KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK),
	
	SHOT_NEXT("[Ctrl+Right Arrow]", KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK),
	SHOT_PREV("[Ctrl+Left Arrow]", KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK),
	
	/**
	 * shortcuts for Mac are use only for tooltips, 
	 * action registering must be done by hand in {@link GlobalKeyboardListenerImpl}
	 */
	SHOT_NEXT_MAC("[Cmd+Right Arrow]", 0, 0),
	SHOT_PREV_MAC("[Cmd+Left Arrow]", 0, 0),
	
	SHOT_FIRST("[Shift+Left Arrow]", KeyEvent.VK_LEFT, KeyEvent.SHIFT_DOWN_MASK),
	SHOT_LAST("[Shift+Right Arrow]", KeyEvent.VK_RIGHT, KeyEvent.SHIFT_DOWN_MASK),
	SHOT_REFRESH("[F5]", KeyEvent.VK_F5, 0),
	SHOT_EDIT("[Ctrl+E]", KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK),
	SHOT_DESCRIPTION("[Ctrl+D]", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK),
	SHOT_LABEL("[Ctrl+L]", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK),
	SHOT_DELETE("[Ctrl+Del]", KeyEvent.VK_DELETE, KeyEvent.CTRL_DOWN_MASK),
	SHOT_SELECT("[Ctrl+A]", KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK),
	
	SHOT_MOVE_NEXT("[Ctrl+W]", KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK),
	SHOT_MOVE_PREV("[Ctrl+Q]", KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK),
	
	SESSION_REFRESH("[Shift+F5]", KeyEvent.VK_F5, KeyEvent.SHIFT_DOWN_MASK),
	SESSION_NEW("[Shift+N]", KeyEvent.VK_N, KeyEvent.SHIFT_DOWN_MASK),
	SESSION_OPEN("[Shift+O]", KeyEvent.VK_O, KeyEvent.SHIFT_DOWN_MASK),
	SESSION_DESCRIPTION("[Shift+D]", KeyEvent.VK_D, KeyEvent.SHIFT_DOWN_MASK),
	SESSION_DELETE("[Shift+Del]", KeyEvent.VK_DELETE, KeyEvent.SHIFT_DOWN_MASK),
	SESSION_DELETE_SELECTED("[Shift+Ctrl+Del]", KeyEvent.VK_DELETE, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK),
	SESSION_SELECT_ALL_SHOTS("[Shift+A]", KeyEvent.VK_A, KeyEvent.SHIFT_DOWN_MASK),
	SESSION_DESELECT_ALL_SHOTS("[Shift+Ctrl+A]", KeyEvent.VK_A, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK),
	SESSION_REORDER("[Shift+E]", KeyEvent.VK_E, KeyEvent.SHIFT_DOWN_MASK),
	
	FIND("[Ctrl+F]", KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), 
	START_RECORDING("[Shift+Ctrl+R]", KeyEvent.VK_R, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK),
	STOP_RECORDING("[Shift+Ctrl+S]", KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK),
	JIRA_EXPORT("[Ctrl+J]", KeyEvent.VK_J, KeyEvent.CTRL_DOWN_MASK), 
	HTML_EXPORT("[Ctrl+H]", KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK);
	// @formatter:on

	public final String text;
	public final int code;
	public final int modifier;

	OperationsShortcutEnum(String shortcut, int shortCutKeyCode, int shortCutKeyModifier) {
		this.text = shortcut;
		this.code = shortCutKeyCode;
		this.modifier = shortCutKeyModifier;
	}
}
