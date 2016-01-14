package com.song.moja.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.song.moja.db.DataProcessor;
import com.song.moja.log.LogConfig;
import com.song.moja.persistent.PersistThread;

/**
 * 消费MQ的线程，从MQ里面批量取
 * 
 * @author 3gods.com ok
 * @param <T>
 */
public class MQConsumer<T> implements Runnable {
	final BlockingQueue<T> mq;
	final long sleepTime;
	final int mqBatchSize;
	final long timeInterval;
	final MQConfig config;
	private boolean flag = false;
	long lastDrainTime = System.currentTimeMillis();
	private List<T> tempList;
	private LogConfig logConfig;

	public MQConsumer(BlockingQueue<T> mq, MQConfig mqConfig) {
		this.mq = mq;
		this.config = mqConfig;
		sleepTime = config.getSleepTime();
		timeInterval = config.getTimeInterval();
		mqBatchSize = config.getBatchSize();
		tempList = new ArrayList<T>(mqBatchSize);
		logConfig = new LogConfig(mqConfig.props);
	}

	public void run() {
		for (;;) {
			tempList.clear();
			boolean timeCondition = (System.currentTimeMillis() - lastDrainTime > timeInterval);
			if (mq.size() >= mqBatchSize || timeCondition || flag) {
				lastDrainTime = System.currentTimeMillis();
				mq.drainTo(tempList, mqBatchSize);
				// 调用方法保存到目的地，这个地方应该是根据配置选用不同的类来保存
				boolean result = DataProcessor.process(tempList);
				if (!result) {
					new PersistThread<T>(mq, tempList, logConfig).start();
				}
			} else { // 感觉这个地方也不是太好，如果在休眠的这段时间刚好有很多数据过来
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
