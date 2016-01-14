package com.song.moja.log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.google.protobuf.MessageLite;

public class CleanLogThread<T extends MessageLite> extends Thread implements Closeable {
	private static Logger LOG = Logger.getLogger(CleanLogThread.class);

	// final BlockingQueue<T extends MessageLite> mq;
	final BlockingQueue<MessageLite> mq;

	final LogConfig logConfig;

	final String logDir;
	private final CountDownLatch shutdownLatch = new CountDownLatch(1);

	public CleanLogThread(BlockingQueue<MessageLite> mq, LogConfig logConfig) {
		this.mq = mq;
		this.logConfig = logConfig;
		this.logDir = logConfig.getDir();
	}

	@Override
	public void run() {
		// 1.文件排序
		File[] files = new File(logDir).listFiles();

		List<File> fileList = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains("   ")) {

				fileList.add(files[i]);
			}
		}
		// sort一下 将文件按照时间排个序
		Collections.sort(fileList, new Comparator<File>() {
			public int compare(File o1, File o2) {
				long diff = o1.lastModified() - o2.lastModified();
				if (diff > 0) {
					return 1;
				} else if (diff == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		File freshestFile = fileList.get(0);
		// 2.读最新的文件，一直读，读到报异常，就表名后面的日志都不正常了
		List<MessageLite> msgList = new ArrayList<MessageLite>(1024);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(freshestFile);
		} catch (FileNotFoundException e) {
			LOG.error("文件不存在" + freshestFile.getAbsolutePath());
		}

		int count = 0;
		try {
			MessageLite messageLite = null;
			while ((messageLite = messageLite.getParserForType().parseDelimitedFrom(inputStream)) != null) {
				msgList.add(messageLite);
				count++;
			}
		} catch (IOException e) {
			LOG.error("FileUtils调用convertFile2Msg，读取文件" + freshestFile.getAbsolutePath() + "出错!!!");
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOG.error("FileUtils调用convertFile2Msg，关闭br出错!!!");
			}
		}

		mq.addAll(msgList);
		// PB对象可以这样搞，但是JSON的文本怎么搞?
		// 3.删除文件，将读出的日志载入到mq

	}

	public void close() throws IOException {
		shutdownLatch.countDown();
	}

}
