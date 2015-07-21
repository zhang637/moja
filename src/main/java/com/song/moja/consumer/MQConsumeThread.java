package com.song.moja.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.song.moja.db.SaveAdaptor;
import com.song.moja.log.LogConfig;
import com.song.moja.mq.MQConfig;
import com.song.moja.persistent.PersistThread;

public class MQConsumeThread<T> implements Runnable {

	final BlockingQueue<T> mq;

	final long sleepTime;
	final int mqBatchSize;

	final MQConfig config;

	boolean flag = false;

	long lastDrainTime = System.currentTimeMillis();

	private List<T> tempList;
	
	private LogConfig logConfig;

	public MQConsumeThread(BlockingQueue<T> mq, MQConfig mqConfig) {
		this.mq = mq;
		this.config = mqConfig;

		sleepTime = config.getSleepTime();
		mqBatchSize = config.getBatchSize();

		tempList = new ArrayList<T>(mqBatchSize);
		logConfig = new LogConfig(mqConfig.props);
	}

	public void run() {
		while (true) {
			if (mq.size() >= mqBatchSize) {
				int drainNum = mq.drainTo(tempList, mqBatchSize);
				lastDrainTime = System.currentTimeMillis();
				flag = true;
				// 调用方法保存到目的地，这个地方应该是根据配置选用不同的类来保存？
				boolean result = SaveAdaptor.save2MongDB(tempList);
				//如果保存失败，可进行持久化，也可以再次推入到mq中
				if(!result){
//					mq.addAll(tempList);
					new PersistThread<T>(mq, tempList, logConfig).start();
				}
				
			} else {
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
				if(!result){
//					mq.addAll(tempList);
					new PersistThread<T>(mq, tempList, logConfig).start();
				}
			}
		}
	}
}
