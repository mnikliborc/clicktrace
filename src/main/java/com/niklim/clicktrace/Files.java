package com.niklim.clicktrace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.google.common.io.ByteSource;

/**
 * Replaces java.nio.file.Files class in order to port code from Java 7 to Java
 * 6.
 */
public class Files {
	public static boolean exists(String path) {
		File file = new File(path);
		return file.exists();
	}
	
	public static void move(String fromPath, String toPath) throws IOException {
		com.google.common.io.Files.move(new File(fromPath), new File(toPath));
	}
	
	public static void copy(String fromPath, String toPath) throws IOException {
		com.google.common.io.Files.copy(new File(fromPath), new File(toPath));
	}
	
	public static void createDirectory(String dirPath) throws IOException {
		File file = new File(dirPath);
		if (!file.mkdir()) {
			throw new IOException("Cannot create directory " + dirPath);
		}
	}
	
	public static void copy(final InputStream resource, String toPath) throws IOException {
		ByteSource source = new ByteSource() {
			@Override
			public InputStream openStream() throws IOException {
				return resource;
			}
		};
		File toFile = new File(toPath);
		FileOutputStream output = new FileOutputStream(toFile);
		source.copyTo(output);
		output.close();
	}
	
	public static void delete(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.delete()) {
			throw new IOException("Cannot delete file " + filePath);
		}
	}
	
	public static String load(File file) throws IOException {
		return com.google.common.io.Files.toString(file, Charset.defaultCharset());
	}
	
	public static void save(File file, String content) throws IOException {
		com.google.common.io.Files.write(content.getBytes(), file);
	}
}
