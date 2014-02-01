package com.niklim.clicktrace.model.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.niklim.clicktrace.Files;

public class PropertiesStore {
	Map<String, String> map = new HashMap<String, String>();
	
	File file;
	
	public PropertiesStore(File file) throws IOException {
		this.file = file;
		load();
	}
	
	public PropertiesStore() {
	}
	
	public void setFile(File f) {
		file = f;
	}
	
	public void setProperty(String propName, String value) {
		map.put(propName, value);
	}
	
	public String getProperty(String propName) {
		return map.get(propName);
	}
	
	public void save() throws IOException {
		StringBuilder b = new StringBuilder();
		for (Entry<String, String> entry : map.entrySet()) {
			b.append(entry.getKey()).append(" = ").append(escape(entry.getValue())).append("\n");
		}
		Files.save(file, b.toString());
	}
	
	private String escape(String value) {
		StringBuffer b = new StringBuffer();
		for (Character c : value.toCharArray()) {
			if (c == '\\') {
				b.append("\\\\");
			} else if (c == '\n') {
				b.append("\\n");
			} else if (c == '\r') {
				b.append("\\r");
			} else if (c == '\t') {
				b.append("\\t");
			} else
				b.append(c);
		}
		return b.toString();
	}
	
	public void load() throws IOException {
		if (file == null) {
			// TODO do somethign
			throw new RuntimeException();
		}
		
		String content = Files.load(file);
		map = new HashMap<String, String>();
		for (String line : content.split("\n")) {
			if (line.trim().isEmpty()) {
				continue;
			}
			
			int splitIndex = line.indexOf("=");
			String propName = line.substring(0, splitIndex).trim();
			String value = unescape(line.substring(splitIndex + 1).trim());
			map.put(propName, value);
		}
	}
	
	private String unescape(String value) {
		if (value.length() < 2) {
			return value;
		}
		
		char[] chars = value.toCharArray();
		StringBuffer b = new StringBuffer();
		int i;
		for (i = 0; i < chars.length - 1; i++) {
			if (chars[i] == '\\' && chars[i + 1] == '\\') {
				b.append('\\');
				i++;
			} else if (chars[i] == '\\' && chars[i + 1] == 'n') {
				b.append('\n');
				i++;
			} else if (chars[i] == '\\' && chars[i + 1] == 'r') {
				b.append('\r');
				i++;
			} else if (chars[i] == '\\' && chars[i + 1] == 't') {
				b.append('\t');
				i++;
			} else {
				b.append(chars[i]);
			}
		}
		if (i < chars.length) {
			b.append(chars[i]);
		}
		return b.toString();
	}
	
	public void clearProperty(String propName) {
		map.remove(propName);
	}
}
