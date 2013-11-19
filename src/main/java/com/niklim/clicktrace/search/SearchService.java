package com.niklim.clicktrace.search;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionManager;

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
	 * @param text
	 *            to be found
	 * @param allSessions
	 *            true when we search in all sessions, false when we search in
	 *            active session only
	 * @param matchCase
	 *            should we match letter case
	 * @return pairs of {#link ScreenShot} and line containing searched text
	 */
	public List<SimpleImmutableEntry<ScreenShot, String>> search(String text, boolean allSessions, boolean matchCase) {
		List<Session> sessions;
		if (allSessions) {
			sessions = sessionManager.loadAll();
		} else {
			sessions = Lists.newArrayList(activeSession.getSession());
		}

		if (!matchCase) {
			text = text.toUpperCase();
		}

		List<SimpleImmutableEntry<ScreenShot, String>> results = Lists.newArrayList();
		for (Session session : sessions) {
			for (ScreenShot shot : session.getShots()) {
				SimpleImmutableEntry<ScreenShot, String> result = findText(shot, text, matchCase);
				if (result != null) {
					results.add(result);
				}
			}
		}
		return results;
	}

	private SimpleImmutableEntry<ScreenShot, String> findText(ScreenShot shot, String text, boolean matchCase) {
		String description = Strings.nullToEmpty(shot.getDescription());
		String label = Strings.nullToEmpty(shot.getLabel());
		String filename = Strings.nullToEmpty(shot.getFilename());

		if (!matchCase) {
			description = description.toUpperCase();
			label = label.toUpperCase();
		}

		if (description.contains(text)) {
			String descFragment = findLine(Strings.nullToEmpty(shot.getDescription()), text, matchCase);
			return new SimpleImmutableEntry<ScreenShot, String>(shot, descFragment);
		} else if (label.contains(text)) {
			return new SimpleImmutableEntry<ScreenShot, String>(shot, shot.getLabel());
		} else if (filename.contains(text)) {
			return new SimpleImmutableEntry<ScreenShot, String>(shot, shot.getFilename());
		} else {
			return null;
		}

	}

	private String findLine(String description, String text, boolean matchCase) {
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
}
