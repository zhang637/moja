package com.song.moja.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.song.moja.db.MongoUtil;
import com.song.moja.util.DateUtils;
import com.sun.tools.apt.resources.apt;

/**
 * 
 * <p>
 * 文件名称:MQMonitorSys.java
 * <p>
 * <p>
 * 文件描述:本类描述
 * <p>
 * 版权所有:湖南省蓝蜻蜓网络科技有限公司版权所有(C)2015
 * </p>
 * <p>
 * 内容摘要:消息队列需要监控的内容，最后展示到页面上
 * </p>
 * <p>
 * 其他说明:其它内容的说明
 * </p>
 * <p>
 * 完成日期:2015年6月12日下午4:14:42
 * </p>
 * <p>
 * 
 * @author
 */
public class MQMonitorInfo implements Serializable {
	private static Logger LOG = Logger.getLogger(MQMonitorInfo.class);
	private static final long serialVersionUID = 1L;

	private static ConcurrentHashMap<String, String> monitorMap = new ConcurrentHashMap<String, String>();

	static {
		new Thread(new Counter(monitorMap)).start();
	}
	// 消息队列大小
	private int mqSize;

	// 客户端活动连接数量
	// private int clientNum;
	private int activeClientNum;

	private Long connectedTotalNum;
	// 接收到的消息总条数
	private Long recvMsgTotalNum;

	// 加入到队列中失败的总条数
	private Long addMQFailTotalNum;

	// 从消息队列中出来的总条数
	private Long outOfMQTotalNum;

	// 插入到mongodb中失败的总条数
	private Long insertFailTotalNum;

	// 每秒处理消息数量
	private Long consumeNumPerSec;

	public static int getMqSize() {
		return null == monitorMap.get("mqSize") ? 0 : Integer
				.valueOf(monitorMap.get("mqSize"));
	}

	public static void setMqSize(int mqSize) {
		monitorMap.put("mqSize", String.valueOf(mqSize));
	}

	public static int getConnectedTotalNum() {
		return null == monitorMap.get("connectedTotalNum") ? 0 : Integer
				.valueOf(monitorMap.get("connectedTotalNum"));
	}

	public static void setConnectedTotalNum(int connectedTotalNum) {
		monitorMap.put("connectedTotalNum", String.valueOf(connectedTotalNum));
	}

	public static int getActiveClientNum() {
		return null == monitorMap.get("activeClientNum") ? 0 : Integer
				.valueOf(monitorMap.get("activeClientNum"));
	}

	public static void setActiveClientNum(int activeClientNum) {
		monitorMap.put("activeClientNum", String.valueOf(activeClientNum));
	}

	public static Long getRecvMsgTotalNum() {
		return null == monitorMap.get("recvMsgTotalNum") ? 0L : Long
				.valueOf(monitorMap.get("recvMsgTotalNum"));
	}

	public static void setRecvMsgTotalNum(Long recvMsgTotalNum) {
		monitorMap.put("recvMsgTotalNum", String.valueOf(recvMsgTotalNum));
	}

	public static Long getAddMQFailTotalNum() {
		return null == monitorMap.get("addMQFailTotalNum") ? 0L : Long
				.valueOf(monitorMap.get("addMQFailTotalNum"));
	}

	public static void setAddMQFailTotalNum(Long addMQFailTotalNum) {
		monitorMap.put("addMQFailTotalNum", String.valueOf(addMQFailTotalNum));
	}

	public static Long getOutOfMQTotalNum() {
		return null == monitorMap.get("outOfMQTotalNum") ? 0L : Long
				.valueOf(monitorMap.get("outOfMQTotalNum"));
	}

	public static void setOutOfMQTotalNum(Long outOfMQTotalNum) {
		monitorMap.put("outOfMQTotalNum", String.valueOf(outOfMQTotalNum));
	}

	public static Long getInsertFailTotalNum() {
		return null == monitorMap.get("insertFailTotalNum") ? 0L : Long
				.valueOf(monitorMap.get("insertFailTotalNum"));
	}

	public static void setInsertFailTotalNum(Long insertFailTotalNum) {
		monitorMap
				.put("insertFailTotalNum", String.valueOf(insertFailTotalNum));
	}

	public Long getConsumeNumPerSec() {
		return null == monitorMap.get("consumeNumPerSec") ? 0L : Long
				.valueOf(monitorMap.get("consumeNumPerSec"));
	}

	public void setConsumeNumPerSec(Long consumeNumPerSec) {
		monitorMap.put("consumeNumPerSec", String.valueOf(consumeNumPerSec));
	}

	public static void send(OutputStream out) {

		try {
			String str = JSON.toJSONString(monitorMap, true);
			out.write(str.getBytes());
			// out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			LOG.info("当前时间是" + DateUtils.getCurrentDate() + "发送队列监控数据出错!!!");
		}
	}

	public static void send(SocketChannel socketChannel) {
		try {
			DB db = MongoUtil.getDB();
			Set<String> dbNames = db.getCollectionNames();
			Map<String ,String> map = new HashMap<String,String>();
			
			if (dbNames.size() > 0) {
				for (String name : db.getCollectionNames()) {
					
					map.put(name,
							String.valueOf(db.getCollection(name).count()));
				}
			}
			
			monitorMap.put("dbs", map.toString());

			String str = JSON.toJSONString(monitorMap, true);
			socketChannel.write(ByteBuffer.wrap(str.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
			LOG.info("当前时间是" + DateUtils.getCurrentDate() + "发送队列监控数据出错!!!");
		}
	}

	// 重写toString方法，在发送邮件警告信息的时候带出
	public static String toStr() {
		return new StringBuilder().append("活动客户端连接数:")
				.append(MQMonitorInfo.getActiveClientNum())
				.append("socket接收到的总数量:")
				.append(MQMonitorInfo.getRecvMsgTotalNum()).append("消息队列大小为:")
				.append(MQMonitorInfo.getMqSize()).append("添加进mq失败的总数量")
				.append(MQMonitorInfo.getAddMQFailTotalNum())
				.append("从mq中出队的总数量")
				.append(MQMonitorInfo.getOutOfMQTotalNum())
				.append("插入mongodb失败总数量")
				.append(MQMonitorInfo.getInsertFailTotalNum()).toString();
	}

	public static void clear() {
		if(null!=monitorMap&&monitorMap.size()>0){
			monitorMap.clear();
		}
	}
}

class Counter implements Runnable {
	private ConcurrentHashMap<String, String> map;

	public Counter(ConcurrentHashMap<String, String> map) {
		this.map = map;
	}

	public void run() {
		while (true) {
			long lastConsumeNum = MQMonitorInfo.getRecvMsgTotalNum();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long nowConsumeNum = MQMonitorInfo.getRecvMsgTotalNum();
			
			long consumeNumPerSec = nowConsumeNum - lastConsumeNum;
			map.put("consumeNumPerSec", String.valueOf(consumeNumPerSec));
		}
	}
}
