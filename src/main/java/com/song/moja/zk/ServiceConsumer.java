package com.song.moja.zk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.song.moja.util.SocketUtil;

import io.netty.util.internal.ThreadLocalRandom;

public class ServiceConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumer.class);

	// 用于等待 SyncConnected 事件触发后继续执行当前线程
	private CountDownLatch latch = new CountDownLatch(1);

	// 定义 volatile 成员变量，用于保存最新的 RMI 地址（考虑到该变量或许会被其它线程所修改，一旦修改后，该变量的改变会影响到其他的线程?
	private volatile List<String> urlList = new ArrayList<String>();

	// 构造器
	public ServiceConsumer() {
		ZooKeeper zk = connectServer(); // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象
		if (zk != null) {
			watchNode(zk); // 观察 /registry 节点的所有子节点便跟urlList 成员变量
		}
	}

	public String lookupURL() {
		int size = urlList.size();
		String url = null;
		if (size > 0) {
			if (size == 1) {
				url = urlList.get(0); // urlList 中只有一个元素，则直接获取该元素
				LOGGER.debug("using only url: {}", url);
			} else {
				url = urlList.get(ThreadLocalRandom.current().nextInt(size)); // �?
				LOGGER.debug("using random url: {}", url);
			}
		}
		return url;
	}

	// 连接 ZooKeeper 服务
	private ZooKeeper connectServer() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(ZkConstant.ZK_CONNECTION_STRING, ZkConstant.ZK_SESSION_TIMEOUT, new Watcher() {
				public void process(WatchedEvent event) {
					if (event.getState() == Event.KeeperState.SyncConnected) {
						latch.countDown(); // 唤醒当前正在执行的线程
					}
				}
			});
			latch.await(); // 使当前线程处于等待状态
		} catch (IOException e1) {
			LOGGER.error("", e1);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
		}
		return zk;
	}

	// 观察 /registry 节点下所有子节点是否有变化
	private void watchNode(final ZooKeeper zk) {
		try {
			List<String> nodeList = zk.getChildren(ZkConstant.ZK_REGISTRY_PATH, new Watcher() {
				public void process(WatchedEvent event) {
					if (event.getType() == Event.EventType.NodeChildrenChanged) {
						watchNode(zk); // 若子节点有变化，则重新调用该方法（为了获取最新子节点中的数据�?
					}
				}
			});
			List<String> dataList = new ArrayList<String>(); // 用于存放 /registry
			for (String node : nodeList) {
				// 获取/registry的子节点中的数据
				byte[] data = zk.getData(ZkConstant.ZK_REGISTRY_PATH + "/" + node, false, null);
				dataList.add(new String(data));
			}
			LOGGER.debug("node data: {}", dataList);
			urlList = dataList; // 更新RMI 地址
		} catch (KeeperException e1) {
			LOGGER.error("", e1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String sendLog(String string) {
		String result = null;

		// 首先是去zookeeper上拿节点
		String url = lookupURL();
		// url = String.format("rmi://%s:%d/%s", host, port,
		// remote.getClass().getName());

		String[] strs = url.split(",");
		String ip = strs[0];
		int port = Integer.valueOf(strs[1]);
		// 然后建立socket连接，然后发送日
		// check(ip,port)
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			socket = new Socket(ip, port);
			out = socket.getOutputStream();
			in = socket.getInputStream();
			out.write(string.getBytes());
			out.flush();
			String readStrFromStream = SocketUtil.readStrFromStream(socket.getInputStream());
			System.out.println(readStrFromStream);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}

				if (socket != null) {
					socket.close();
					System.out.println("客户端关闭socket!!!");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}