package com.niklim.clicktrace.session;

import java.awt.image.BufferedImage;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.niklim.clicktrace.session.helper.ImageLoader;
import com.niklim.clicktrace.session.helper.SessionHelperFactory;

public class ScreenShot {
	private String name;
	private String label;
	private String description;
	private BufferedImage image;
	private Session session;

	private ImageLoader imageLoader = SessionHelperFactory.getImageLoader();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

		if (!Strings.nullToEmpty(name).equals(Strings.nullToEmpty(other.getName()))) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		HashFunction hf = Hashing.md5();
		HashCode hc = hf.newHasher().putString(name, Charsets.UTF_8).hash();
		return hc.asInt();
	}

	@Override
	public String toString() {
		return name;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void delete() {
		// TODO Auto-generated method stub

	}

}
