package com.niklim.clicktrace.model.session;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.niklim.clicktrace.model.session.helper.ImageLoader;
import com.niklim.clicktrace.model.session.helper.ScreenShotDeleter;
import com.niklim.clicktrace.model.session.helper.SessionHelperFactory;

public class ScreenShot {
	private String filename;
	private String label;
	private String description;
	private BufferedImage image;
	private Session session;

	private List<Click> clicks;

	private ImageLoader imageLoader = SessionHelperFactory.getImageLoader();
	private ScreenShotDeleter deleter = new ScreenShotDeleter();

	public static class Click {
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
				String[] vals = click.substring(1, click.length() - 1).split(
						"-");
				int x = Integer.parseInt(vals[0]);
				int y = Integer.parseInt(vals[1]);
				int button = Integer.parseInt(vals[2]);
				clicks.add(new Click(x, y, button));
			}
			return clicks;
		}

		public static String getString(List<Click> clicks) {
			List<String> strings = Lists
					.newArrayListWithCapacity(clicks.size());
			for (Click c : clicks) {
				strings.add(c.toString());
			}
			return StringUtils.join(strings, ";");
		}

	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String name) {
		this.filename = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BufferedImage getImage() {
		if (image == null) {
			loadImage();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage loadImage() {
		image = imageLoader.load(this);
		return image;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ScreenShot)) {
			return false;
		}
		ScreenShot other = (ScreenShot) o;

		if (!Strings.nullToEmpty(filename).equals(
				Strings.nullToEmpty(other.getFilename()))) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		HashFunction hf = Hashing.md5();
		HashCode hc = hf.newHasher().putString(filename, Charsets.UTF_8).hash();
		return hc.asInt();
	}

	@Override
	public String toString() {
		return Strings.isNullOrEmpty(label) ? filename : label;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void delete() {
		deleter.delete(this);
	}

	public List<Click> getClicks() {
		return clicks;
	}

	public void setClicks(List<Click> clicks) {
		this.clicks = clicks;
	}

}
