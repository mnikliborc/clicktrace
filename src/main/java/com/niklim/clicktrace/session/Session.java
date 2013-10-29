package com.niklim.clicktrace.session;

import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.niklim.clicktrace.session.helper.ScreenShotLoader;
import com.niklim.clicktrace.session.helper.SessionHelperFactory;
import com.niklim.clicktrace.session.helper.SessionSaver;

public class Session {
	private String name;
	private List<ScreenShot> shots;

	private SessionSaver saver = SessionHelperFactory.getSessionSaver();
	private ScreenShotLoader screenShotsLoader = SessionHelperFactory.getScreenShotLoader();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ScreenShot> getShots() {
		if (shots == null) {
			loadScreenShots();
		}
		return shots;
	}

	public void setShots(List<ScreenShot> shots) {
		this.shots = shots;
	}

	public List<ScreenShot> loadScreenShots() {
		shots = screenShotsLoader.load(this);
		return shots;
	}

	public void save() {
		saver.save(this);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Session)) {
			return false;
		}
		Session other = (Session) o;

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
}
