package com.song.moja.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

/**
 * 
 * <p>
 * 文件名称:DateUtils.java
 * <p>
 * <p>
 * 文件描述:日期工具�?
 * <p>
 * 版权�?��:湖南省蓝蜻蜓网络科技有限公司版权�?��(C)2015
 * </p>
 * <p>
 * 内容摘要:�?��描述本文件的内容，包括主要模块�?函数及能的说�?
 * </p>
 * <p>
 * 其他说明:其它内容的说�?
 * </p>
 * <p>
 * 完成日期:2015�?�?4日下�?:58:18
 * </p>
 * <p>
 * 
 * @author wangwanqiang
 */
public class DateUtils {

	private static Logger LOG = Logger.getLogger(DateUtils.class);

	static final String formatPattern = "yyyy-MM-dd";

	static final String formatPattern_Short = "yyyyMMdd";

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		return format.format(new Date());
	}

	/**
	 * 获取制定毫秒数之前的日期
	 * 
	 * @param timeDiff
	 * @return
	 */
	public static String getDesignatedDate(long timeDiff) {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		long nowTime = System.currentTimeMillis();
		long designTime = nowTime - timeDiff;
		return format.format(designTime);
	}

	/**
	 * 
	 * 获取前几天的日期
	 */
	public static String getPrefixDate(String count) {
		Calendar cal = Calendar.getInstance();
		int day = 0 - Integer.parseInt(count);
		cal.add(Calendar.DATE, day); // int amount 代表天数
		Date datNew = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		return format.format(datNew);
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		return format.format(date);
	}

	/**
	 * 字符串转换日�?
	 * 
	 * @param str
	 * @return
	 */
	public static Date stringToDate(String str) {
		// str = " 2008-07-10 19:20:00 " 格式
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		if (!str.equals("") && str != null) {
			try {
				return format.parse(str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	// java中�?样计算两个时间如：�?21:57”和�?8:20”相差的分钟数�?小时�?java计算两个时间差小�?分钟 �?.
	public void timeSubtract() {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = null;
		Date end = null;
		try {
			begin = dfs.parse("2004-01-02 11:30:24");
			end = dfs.parse("2004-03-26 13:31:40");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成�?

		long day1 = between / (24 * 3600);
		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
		long second1 = between % 60;
	}

	public static String getCurrentMonth() {
		DateFormat df = new SimpleDateFormat("yyyy_MM");
		return df.format(new Date());
	}

	public static String getNowTime() {
		return new Date().toString();
	}

	public static boolean isFullMin() {
		Date d = new Date();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		return (gc.get(gc.MINUTE) == 0);
	}
	
	
}
