package com.song.moja.log;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.song.moja.mq.MQConfig;
import com.song.moja.util.DateUtils;
import com.song.moja.util.FileUtils;
import com.song.moja.util.PropertyUtil;

public class LoadLogHandler<T> extends Thread implements Closeable {

	private static Logger LOG = Logger.getLogger(LoadLogHandler.class);

	private BlockingQueue<T> mq;

	private int mqSafeNum;

	private long sleepTime;

	private MQConfig config;

	private final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private String dir;

	public LoadLogHandler(BlockingQueue<T> mq) {
		this.mq = mq;
		mqSafeNum = Integer.valueOf(PropertyUtil.get("mq.safe.num"));

		sleepTime = Integer.valueOf(PropertyUtil.get("ld.sleep"));
	}

	public LoadLogHandler(MQConfig mqConfig, BlockingQueue<T> mq) {
		this.config = mqConfig;
		this.mq = mq;

		mqSafeNum = config.getSafeNum();
		sleepTime = config.getSleepTime();
		dir = config.getLogDir();

	}

	public void run() {
		LOG.info("LoadLogHandler启动成功!");

		File fileDir = new File(FileUtils.getMsgInputDir());

		if (!fileDir.exists()) {
			LOG.error("存放消费错误日志的目录没有配置！！！" + dir);
			fileDir.mkdirs();
		}

		while (true) {
			// 获得错误日志的类别子目录
			File[] files = fileDir.listFiles();
			// 如果不存在持久化文件
			if (null == files || files.length == 0) {
				try {
					//睡眠
					Thread.sleep(sleepTime * 12 * 3 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 如果存在，并且mq的大小小于安�?
			} else {
				List<File> fileList = Arrays.asList(files);

				List<T> logs = new ArrayList<T>(1024);
				//这个地方好像有个问题
					for (File fileTemp : fileList) {
						logs = FileUtils.readLogsFromFile(fileTemp);
						
						// 加入mq
						//如果mq大小在安全线以下
						if (mq.size() <= mqSafeNum) {
							if(null!=logs&&logs.size()>0){
								boolean flag = mq.addAll(logs);
								if (flag) {
									logs.clear();
									fileTemp.delete();
									LOG.error("当前时间"+DateUtils.getCurrentDate()+"将文件读成logs载入mq成功,删除本地持久化文件"+fileTemp.getAbsolutePath()+"成功!!!");
								}
							}
						//如果mq大小在安全线以上，休眠短暂时间然后再试？
						} else {
							try {
								if(null!=logs&&logs.size()>0){
									logs.clear();
								}
								Thread.sleep(sleepTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
				}
			}
		}
	}

	public void close() {
		shutdownLatch.countDown();
	}
}
