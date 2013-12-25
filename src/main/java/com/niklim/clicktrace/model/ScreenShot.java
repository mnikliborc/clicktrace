package com.niklim.clicktrace.model;

import java.awt.image.BufferedImage;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.niklim.clicktrace.model.helper.ImageLoader;
import com.niklim.clicktrace.model.helper.ScreenShotDeleter;

/**
 * Represents screenshot data. Allows loading and deleting it using Visitor like
 * pattern.
 */
public class ScreenShot {
	private String filename;
	private String label;
	private String description;
	private BufferedImage image;
	private Session session;

	private List<Click> clicks;

	private ImageLoader imageLoader;
	private ScreenShotDeleter deleter;

	/**
	 * Creates screenshot with Visitor objects.
	 * 
	 * @param imageLoader
	 * @param deleter
	 */
	public ScreenShot(ImageLoader imageLoader, ScreenShotDeleter deleter) {
		this.imageLoader = imageLoader;
		this.deleter = deleter;
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
		if (this == o) {
			return true;
		}

		if (!(o instanceof ScreenShot)) {
			return false;
		}
		ScreenShot other = (ScreenShot) o;

		if (!Strings.nullToEmpty(filename).equals(Strings.nullToEmpty(other.getFilename()))) {
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

	/**
	 * Deletes the screenshot from the disk.
	 */
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
