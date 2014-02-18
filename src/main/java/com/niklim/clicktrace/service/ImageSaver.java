package com.niklim.clicktrace.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.TimeMeter;
import com.niklim.clicktrace.msg.ErrorMsgs;

/**
 * Compresses and saves images in own thread.
 */
public class ImageSaver implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(ImageSaver.class);

	private static Format format = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

	private final FileCompressor compressor = new FileCompressor("jpg", "png");
	private final BlockingQueue<SaveTask> queue = new LinkedBlockingDeque<SaveTask>();

	@Inject
	private FileManager fileManager;

	private static class SaveTask {
		BufferedImage image;
		String sessionName;
		Date date;

		public SaveTask(BufferedImage image, String sessionName, Date date) {
			this.image = image;
			this.sessionName = sessionName;
			this.date = date;
		}
	}

	public ImageSaver() {
		new Thread(this).start();
	}

	/**
	 * Enqueues a save task.
	 * 
	 * @param image
	 * @param sessionName
	 * @throws IOException
	 */
	public void save(BufferedImage image, String sessionName) {
		queue.add(new SaveTask(image, sessionName, new Date()));
	}

	@Override
	public void run() {
		try {
			while (true) {
				SaveTask saving = queue.take();
				try {
					saveImage(saving);
				} catch (IOException e) {
					log.error(ErrorMsgs.SCREENSHOT_SAVE_ERROR, e);
					ErrorNotifier.notify(ErrorMsgs.SCREENSHOT_SAVE_ERROR);
				}
			}
		} catch (InterruptedException e) {
			log.error(ErrorMsgs.SCREENSHOT_SAVE_ERROR, e);
			ErrorNotifier.notify(ErrorMsgs.SCREENSHOT_SAVE_FATAL_ERROR);
		}
	}

	private void saveImage(SaveTask saveTask) throws IOException {
		TimeMeter tm = TimeMeter.start("ImageSaver.saveImage", log);
		FileCompressor.CompressionResult compressionResult = compressor.getBestCompressed(saveTask.image);

		File file = findFileToSave(saveTask.sessionName, compressionResult, saveTask.date);
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(compressionResult.stream.toByteArray());
		fop.flush();
		fop.close();

		tm.stop();
	}

	/**
	 * Creates filepath based on timestamp (sec precision). If file already
	 * exists, then appends 'x' to the timestamp and checks again if exists, and
	 * so on.
	 * 
	 * @param sessionName
	 * @param compressionResult
	 * @return
	 */
	private File findFileToSave(String sessionName, FileCompressor.CompressionResult compressionResult, Date date) {
		String timeStamp = format.format(date);

		File file;
		String postfix = "";
		do {
			String filename = timeStamp + postfix + "." + compressionResult.format;
			String filePath = fileManager.createFilePath(sessionName, filename);
			file = new File(filePath);
			postfix += "x";
		} while (file.exists());

		return file;
	}

}
