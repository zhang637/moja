package com.song.moja.persistent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.song.moja.log.LogConfig;
import com.song.moja.serialize.FastJsonSerializer;
import com.song.moja.util.Utils;

/**
 * 持久化的线程
 * 
 * @author 3gods.com
 *
 */
public class PersistThread<T> extends Thread {
	private static Logger LOG = Logger.getLogger(PersistThread.class);

	final BlockingQueue<T> mq;
	final List<T> tempList;
	final LogConfig config;
	private final AtomicInteger logIndex = new AtomicInteger(0);
	final int logMaxSize;

	public PersistThread(BlockingQueue<T> mq, List<T> tempList, LogConfig logConfig) {
		this.mq = mq;
		this.tempList = tempList;
		this.config = logConfig;
		this.logMaxSize = config.getMaxSize();
	}

	@Override
	public void run() {
		File file = null;
		OutputStream out = null;
		try {
			file = Utils.getCanonicalFile(new File(config.getDir()));
			out = new FileOutputStream(file, true);

			for (int i = 0; i < tempList.size(); i++) {
				out = isFile2Big(file, logMaxSize, out);
				T log = tempList.get(i);
				byte[] bys = FastJsonSerializer.serialize(log);
				// 写入文件
				out.write(bys);
				tempList.remove(i);
			}
			tempList.clear();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("将mq中日志写入到文件出错，正在将剩下的日志，大小为:" + tempList.size() + "写回到mq" + e.getMessage(), e);
			mq.addAll(tempList);
		}
	}

	private OutputStream isFile2Big(File file, int logMaxSize, OutputStream out) {
		long fileLength = file.length();
		long maxFileSize = 1024L * 1024 * 1024 * logMaxSize;

		if (fileLength >= maxFileSize) {
			String filePath = file.getAbsolutePath() + logIndex.getAndIncrement();
			file = new File(filePath);
			LOG.error("持久化日志文件" + file.getAbsolutePath() + "过大，生成新的日志文件" + filePath);
			OutputStream output = null;
			try {
				output = new FileOutputStream(file, true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return output;
		} else {
			return out;
		}
	}
}