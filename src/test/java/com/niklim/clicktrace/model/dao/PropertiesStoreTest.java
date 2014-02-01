package com.niklim.clicktrace.model.dao;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class PropertiesStoreTest {
	@Test
	public void test() throws IOException {
		PropertiesStore props = new PropertiesStore();
		props.setFile(new File("session.properties"));
		props.setProperty("propA", "valA");
		props.setProperty("propB", "valB");
		props.save();
		
		verify();
	}
	
	private void verify() throws IOException {
		PropertiesStore props = new PropertiesStore(new File("session.properties"));
		assertThat(props.getProperty("propA")).isEqualTo("valA");
		assertThat(props.getProperty("propB")).isEqualTo("valB");
	}
}
