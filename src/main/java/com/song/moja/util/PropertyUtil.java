package com.song.moja.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * <p>
 * 文件名称:PropertyUtil.java
 * <p>
 * <p>
 * 文件描述:加载property配置文件
 * <p>
 * 版权所有:湖南省蓝蜻蜓网络科技有限公司版权所有(C)2015
 * </p>
 * <p>
 * 内容摘要:简要描述本文件的内容，包括主要模块、函数及能的说明
 * </p>
 * <p>
 * 其他说明:可以通过这个类取得邮件服务
 * </p>
 * <p>
 * 完成日期:2015年6月18日上午10:22:45
 * </p>
 * <p>
 * 
 * @author
 */
public class PropertyUtil {
	private static Logger LOG = Logger.getLogger(PropertyUtil.class);

	private static Map<String, String> map;

	public static void init(String propPath) {
		InputStream in = null;
		Properties prop = null;
		try {
			in = new FileInputStream(new File(propPath));
			prop = new Properties();
			prop.load(in);
		} catch (IOException e) {
			LOG.error("配置文件" + propPath + "不存在!");
			throw new RuntimeException("配置文件" + propPath + "不存在!" + e.getMessage(), e);
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				LOG.error("读取配置文件" + propPath + "关闭in输入流错误!", e);
				throw new RuntimeException("读取配置文件" + propPath + "关闭in输入流错误!" + e.getMessage(), e);
			}
		}
		map = new HashMap<String, String>((Map) prop);
	}

	public static String get(String key) {

		return StringUtils.isEmpty(key) ? null : StringUtils.trim(map.get(key));
	}

	public static String toStr() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
		}
		return sb.toString();
	}

	public static void send(SocketChannel socketChannel) {
		if (null == socketChannel) {
			return;
		}
		try {
			socketChannel.write(ByteBuffer.wrap(toStr().getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		PropertyUtil.init("phplogsys.properties");
		//
	}

}
