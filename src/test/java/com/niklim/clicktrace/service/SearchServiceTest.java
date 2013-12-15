package com.niklim.clicktrace.service;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {
	@InjectMocks
	private SearchService searchService = new SearchService();

	@Mock
	private SessionManager sessionManager;

	@Mock
	private ActiveSession activeSession;

	@Test
	public void testAllSessions() {
		// given
		String text = "text";
		boolean matchCase = true;
		boolean allSessions = true;

		// when
		searchService.search(text, allSessions, matchCase);

		// then
		Mockito.verifyZeroInteractions(activeSession);
		Mockito.verify(sessionManager).loadAll();
	}

	@Test
	public void testOnlyActiveSession() {
		// given
		String text = "text";
		boolean matchCase = true;
		boolean allSessions = false;

		Session session = Mockito.mock(Session.class);
		Mockito.when(activeSession.getSession()).thenReturn(session);

		// when
		searchService.search(text, allSessions, matchCase);

		// then
		Mockito.verifyZeroInteractions(sessionManager);
		Mockito.verify(activeSession).getSession();
	}

	@Test
	public void shouldFindIgnoreCase() {
		// given
		String text = "TEXT";
		boolean matchCase = false;

		ScreenShot shot = new ScreenShot(null, null);
		shot.setDescription("kaszanka\ntext\nkaszanka");

		// when
		SearchService.ShotSearchResult result = searchService.findText(shot, text, matchCase);

		// then
		assertThat(result.shot).isSameAs(shot);
	}

	@Test
	public void shouldNotFindMatchCase() {
		// given
		String text = "Text";
		boolean matchCase = true;

		ScreenShot shot = new ScreenShot(null, null);
		shot.setDescription("kaszanka\ntext\nkaszanka");

		// when
		SearchService.ShotSearchResult result = searchService.findText(shot, text, matchCase);

		// then
		assertThat(result).isNull();
	}

	@Test
	public void shouldFindMatchCase() {
		// given
		String text = "Text";
		boolean matchCase = true;

		ScreenShot shot = new ScreenShot(null, null);
		shot.setDescription("kaszanka\nText\nkaszanka");

		// when
		SearchService.ShotSearchResult result = searchService.findText(shot, text, matchCase);

		// then
		assertThat(result.shot).isSameAs(shot);
	}
}
