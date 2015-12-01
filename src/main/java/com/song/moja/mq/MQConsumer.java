package com.song.moja.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.song.moja.db.SaveAdaptor;
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
	final MQConfig config;
	boolean flag = false;
	long lastDrainTime = System.currentTimeMillis();
	private List<T> tempList;
	private LogConfig logConfig;

	public MQConsumer(BlockingQueue<T> mq, MQConfig mqConfig) {
		this.mq = mq;
		this.config = mqConfig;
		sleepTime = config.getSleepTime();
		mqBatchSize = config.getBatchSize();
		tempList = new ArrayList<T>(mqBatchSize);
		logConfig = new LogConfig(mqConfig.props);
	}

	public void run() {
		for (;;) {
			if (mq.size() >= mqBatchSize) {
				int drainNum = mq.drainTo(tempList, mqBatchSize);
				lastDrainTime = System.currentTimeMillis();
				flag = true;
				// 调用方法保存到目的地，这个地方应该是根据配置选用不同的类来保存
				boolean result = SaveAdaptor.save2MongDB(tempList);
				if (!result) {
					// 如果保存失败，也可以再次推入到mq中，3次没有成功，持久化到本地
					// mq.addAll(tempList);
					new PersistThread<T>(mq, tempList, logConfig).start();
				}
			} else { // 感觉这个地方也不是太好，如果在休眠的这段时间刚好有很多数据过来
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			boolean timeCondition = (System.currentTimeMillis() - lastDrainTime > 3000);
			if (timeCondition && flag) {
				mq.drainTo(tempList, mqBatchSize);
				boolean result = SaveAdaptor.save2MongDB(tempList);
				if (!result) {
					// mq.addAll(tempList);
					new PersistThread<T>(mq, tempList, logConfig).start();
				}
			}
		}
	}
}
