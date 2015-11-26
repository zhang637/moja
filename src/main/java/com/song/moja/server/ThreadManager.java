package com.song.moja.server;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.song.moja.db.MongoConfig;
import com.song.moja.db.MongoUtil;
import com.song.moja.log.LogConfig;
import com.song.moja.monitor.MonitorHandler;
import com.song.moja.mq.MQConfig;
import com.song.moja.mq.MQConsumer;
import com.song.moja.persistent.PersistThread;
import com.song.moja.util.Utils;

public class ThreadManager<T> implements Closeable {
	final ServerConfig config;
	final File logDir;
	final boolean needRecovery;

	// 这个锁是在创建日志文件，删除日志文件时候使用,同步加锁，暂未实现
	private final Object logCreationLock = new Object();
	private BlockingQueue<T> mq;
	// 响应查看监控队列的线程
	private MonitorHandler monitorHandler;
	// 现在将这些线程全部放到servicePool中执行
	private ExecutorService servicePool;

	final int persistBatchSize;

	final List<T> tempList;

	final LogConfig logConfig;

	final MQConfig mqConfig;

	final int mqConsumeNum;

	public ThreadManager(ServerConfig config, boolean needRecovery) throws Exception {
		this.config = config;
		this.logDir = Utils.getCanonicalFile(new File(config.getLogDir()));

		mq = new LinkedBlockingDeque<T>(config.getMQInitSize());

		this.persistBatchSize = config.getPersistBatchSize();
		this.tempList = new ArrayList<T>(persistBatchSize);
		this.logConfig = new LogConfig(config.props);
		this.mqConfig = new MQConfig(config.props);
		this.mqConsumeNum = mqConfig.getThreadNum();
		servicePool = Executors.newCachedThreadPool();
		this.needRecovery = needRecovery;
		if (needRecovery) {
//			new CleanLogThread<MessageTite>(mq, logConfig).start();
		}

		MongoUtil.init(new MongoConfig(config.props));
	}

	public void start() throws Exception {
		// 启动消费MQ的线程
		for (int i = 0; i < mqConsumeNum; i++) {
			servicePool.execute(new MQConsumer<T>(mq, mqConfig));
		}

		// 启动定时扫描线程
		// servicePool.execute(new LoadLogHandler<T>(mqConfig, mq));
	}

	public void close() {
		flushAllLogs();

		try {
			servicePool.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void flushAllLogs() {
		while (mq.peek() != null) {
			int result = mq.drainTo(tempList, persistBatchSize);
			servicePool.execute(new PersistThread<T>(mq, tempList, logConfig));
			tempList.clear();
		}
	}

	public BlockingQueue<T> getMq() {
		return mq;
	}

	public void setMq(BlockingQueue<T> mq) {
		this.mq = mq;
	}

	public MonitorHandler getMonitorHandler() {
		return monitorHandler;
	}

	public void setMonitorHandler(MonitorHandler monitorHandler) {
		this.monitorHandler = monitorHandler;
	}

	public ExecutorService getServicePool() {
		return servicePool;
	}

	public void setServicePool(ExecutorService servicePool) {
		this.servicePool = servicePool;
	}

	public ServerConfig getConfig() {
		return config;
	}

	public File getLogDir() {
		return logDir;
	}

	public boolean isNeedRecovery() {
		return needRecovery;
	}

	public Object getLogCreationLock() {
		return logCreationLock;
	}

	public int getPersistBatchSize() {
		return persistBatchSize;
	}

	public List<T> getTempList() {
		return tempList;
	}

	public LogConfig getLogConfig() {
		return logConfig;
	}

	public MQConfig getMqConfig() {
		return mqConfig;
	}

	public int getMqConsumeNum() {
		return mqConsumeNum;
	}

}
