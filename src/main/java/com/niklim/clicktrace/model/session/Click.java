package com.niklim.clicktrace.model.session;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class Click {
	private int x;
	private int y;
	private int button;

	public Click(int x, int y, int button) {
		this.x = x;
		this.y = y;
		this.button = button;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getButton() {
		return button;
	}

	@Override
	public String toString() {
		return "(" + x + "-" + y + "-" + button + ")";
	}

	public static List<Click> getList(String string) {
		List<Click> clicks = new ArrayList<Click>();
		if (Strings.isNullOrEmpty(string)) {
			return clicks;
		}
		for (String click : string.split(";")) {
			String[] vals = click.substring(1, click.length() - 1).split("-");
			int x = Integer.parseInt(vals[0]);
			int y = Integer.parseInt(vals[1]);
			int button = Integer.parseInt(vals[2]);
			clicks.add(new Click(x, y, button));
		}
		return clicks;
	}

	public static String getString(List<Click> clicks) {
		List<String> strings = Lists.newArrayListWithCapacity(clicks.size());
		for (Click c : clicks) {
			strings.add(c.toString());
		}
		return StringUtils.join(strings, ";");
	}

}