package com.niklim.clicktrace.model;

import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.niklim.clicktrace.model.helper.ScreenShotLoader;
import com.niklim.clicktrace.model.helper.SessionDeleter;
import com.niklim.clicktrace.model.helper.SessionMetadataHelper;
import com.niklim.clicktrace.model.helper.SessionSaver;

public class Session {
	private String name;
	private List<ScreenShot> shots;
	private SessionMetadata metadata;

	private SessionSaver saver;
	private SessionDeleter deleter;
	private ScreenShotLoader screenShotsLoader;
	private SessionMetadataHelper sessionMetadataHelper;

	public Session(SessionSaver saver, SessionDeleter deleter, ScreenShotLoader screenShotsLoader,
			SessionMetadataHelper sessionMetadataHelper) {
		this.saver = saver;
		this.deleter = deleter;
		this.screenShotsLoader = screenShotsLoader;
		this.sessionMetadataHelper = sessionMetadataHelper;
	}

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

	public SessionMetadata loadMetadata() {
		metadata = sessionMetadataHelper.loadMetadata(this);
		return metadata;
	}

	public void save() {
		saver.save(this);
	}

	public void delete() {
		deleter.delete(this);
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