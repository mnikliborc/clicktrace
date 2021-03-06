package com.niklim.clicktrace.service;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;

@Singleton
public class SearchService {

	@Inject
	private SessionManager sessionManager;

	@Inject
	private ActiveSession activeSession;

	/**
	 * Method searches for {#link ScreenShot}s which have label, filename or
	 * description containing given text.
	 * 
	 * @param text text to be found
	 * @param allSessions true when search should be performed in all sessions,
	 *            false when we search in active session only
	 * @param matchCase true when we should match letter case
	 * @return pairs of {#link ScreenShot} and line containing found text
	 */
	public List<SearchResult> search(String text, boolean allSessions, boolean matchCase) {
		List<Session> sessions;
		if (allSessions) {
			sessions = sessionManager.loadAll();
		} else {
			sessions = Lists.newArrayList(activeSession.getSession());
		}

		if (!matchCase) {
			text = text.toUpperCase();
		}

		List<SearchResult> results = Lists.newArrayList();
		for (Session session : sessions) {
			for (ScreenShot shot : session.getShots()) {
				ShotSearchResult result = findText(shot, text, matchCase);
				if (result != null) {
					results.add(result);
				}
			}

			SessionSearchResult result = findText(session, text, matchCase);
			if (result != null) {
				results.add(result);
			}
		}
		return results;
	}

	ShotSearchResult findText(ScreenShot shot, String text, boolean matchCase) {
		String description = Strings.nullToEmpty(shot.getDescription());
		String label = Strings.nullToEmpty(shot.getLabel());
		String filename = Strings.nullToEmpty(shot.getFilename());

		if (!matchCase) {
			description = description.toUpperCase();
			label = label.toUpperCase();
		}

		if (description.contains(text)) {
			String descFragment = findLine(Strings.nullToEmpty(shot.getDescription()), text, matchCase);
			return new ShotSearchResult(shot, descFragment);
		} else if (label.contains(text)) {
			return new ShotSearchResult(shot, shot.getLabel());
		} else if (filename.contains(text)) {
			return new ShotSearchResult(shot, shot.getFilename());
		} else {
			return null;
		}

	}
	
	SessionSearchResult findText(Session session, String text, boolean matchCase) {
		String description = Strings.nullToEmpty(session.getDescription());

		if (!matchCase) {
			description = description.toUpperCase();
		}

		if (description.contains(text)) {
			String descFragment = findLine(Strings.nullToEmpty(session.getDescription()), text, matchCase);
			return new SessionSearchResult(session, descFragment);
		} else {
			return null;
		}

	}

	String findLine(String description, String text, boolean matchCase) {
		for (String line : description.split("\n")) {
			String l = line;
			if (!matchCase) {
				l = line.toUpperCase();
			}
			if (l.contains(text)) {
				return line;
			}
		}
		return null;
	}

	public static abstract class SearchResult {
		public final String highlight;

		public SearchResult(String highlight) {
			this.highlight = highlight;
		}
	}

	public static class ShotSearchResult extends SearchResult {
		public final ScreenShot shot;

		public ShotSearchResult(ScreenShot shot, String highlight) {
			super(highlight);
			this.shot = shot;
		}

		@Override
		public String toString() {
			return shot.toString();
		}
	}

	public static class SessionSearchResult extends SearchResult {
		public final Session session;

		public SessionSearchResult(Session session, String highlight) {
			super(highlight);
			this.session = session;
		}

		@Override
		public String toString() {
			return session.toString() + " - session";
		}
	}
}
