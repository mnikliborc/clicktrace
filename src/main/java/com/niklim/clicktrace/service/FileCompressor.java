package com.niklim.clicktrace.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niklim.clicktrace.TimeMeter;

/**
 * In parallel it compresses an image using given formats (jpg, png, etc.).
 * Returns the image in the format resulting in the smallest file size.
 */
public class FileCompressor {
	private static final Logger log = LoggerFactory.getLogger(FileCompressor.class);
	ExecutorService executor;

	String[] imageFormats;

	public FileCompressor(String... imageFormats) {
		executor = Executors.newFixedThreadPool(imageFormats.length);
		this.imageFormats = imageFormats;
	}

	public CompressionResult getBestCompressed(BufferedImage image) {
		TimeMeter tm = TimeMeter.start("FileCompressor.getBestCompressed", log);

		List<Future<CompressionResult>> futures = compress(image);

		CompressionResult minSize = null;
		for (Future<CompressionResult> f : futures) {
			try {
				CompressionResult result = f.get();
				if (minSize == null || result.streamSize < minSize.streamSize) {
					minSize = result;
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}

		tm.stop();
		return minSize;
	}

	private List<Future<CompressionResult>> compress(BufferedImage image) {
		List<Future<CompressionResult>> futures = new ArrayList<Future<CompressionResult>>(imageFormats.length);
		for (String format : imageFormats) {
			futures.add(executor.submit(new Compressing(image, format)));
		}

		return futures;
	}

	public static class CompressionResult {
		public CompressionResult(ByteArrayOutputStream stream, String format) {
			this.stream = stream;
			this.streamSize = stream.size();
			this.format = format;
		}

		public final ByteArrayOutputStream stream;
		public final int streamSize;
		public final String format;
	}

	private static class Compressing implements Callable<CompressionResult> {
		private final BufferedImage image;
		private final String format;

		public Compressing(BufferedImage image, String format) {
			this.image = image;
			this.format = format;
		}

		@Override
		public CompressionResult call() throws Exception {
			try {
				BufferedImage buffi = image;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageOutputStream ios = ImageIO.createImageOutputStream(baos);

				Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
				ImageWriter writer = writers.next();

				ImageWriteParam param = writer.getDefaultWriteParam();

				writer.setOutput(ios);
				writer.write(null, new IIOImage(buffi, null, null), param);
				writer.dispose();

				return new CompressionResult(baos, format);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}
}
