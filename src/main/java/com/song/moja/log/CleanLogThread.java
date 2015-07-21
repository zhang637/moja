package com.song.moja.log;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class CleanLogThread<T> extends Thread implements Closeable {
	final BlockingQueue<T> mq;

	final LogConfig logConfig;

	final String logDir;
	private final CountDownLatch shutdownLatch = new CountDownLatch(1);

	public CleanLogThread(BlockingQueue<T> mq, LogConfig logConfig) {
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
		//PB对象可以这样搞，但是JSON的文本怎么搞?
		// 3.删除文件，将读出的日志载入到mq
		
	}

	public void close() throws IOException {
		shutdownLatch.countDown();
	}

}
