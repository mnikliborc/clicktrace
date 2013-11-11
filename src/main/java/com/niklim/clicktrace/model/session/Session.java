package com.niklim.clicktrace.model.session;

import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.niklim.clicktrace.model.session.helper.ScreenShotLoader;
import com.niklim.clicktrace.model.session.helper.SessionDeleter;
import com.niklim.clicktrace.model.session.helper.SessionHelperFactory;
import com.niklim.clicktrace.model.session.helper.SessionMetadataHelper;
import com.niklim.clicktrace.model.session.helper.SessionSaver;

public class Session {
	private String dirName;
	private String label;
	private List<ScreenShot> shots;
	private SessionMetadata metadata;

	private SessionSaver saver = SessionHelperFactory.getSessionSaver();
	private SessionDeleter deleter = SessionHelperFactory.getSessionDeleter();
	private ScreenShotLoader screenShotsLoader = SessionHelperFactory.getScreenShotLoader();
	private SessionMetadataHelper sessionMetadataHelper = SessionHelperFactory.getSessionMetadataHelper();

	public String getDirname() {
		return dirName;
	}

	public void setDirname(String name) {
		this.dirName = name;
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

		if (!Strings.nullToEmpty(dirName).equals(Strings.nullToEmpty(other.getDirname()))) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		HashFunction hf = Hashing.md5();
		HashCode hc = hf.newHasher().putString(dirName, Charsets.UTF_8).hash();
		return hc.asInt();
	}

	@Override
	public String toString() {
		return Strings.isNullOrEmpty(label) ? dirName : label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
