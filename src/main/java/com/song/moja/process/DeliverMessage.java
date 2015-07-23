package com.song.moja.process;

import java.util.Date;
import java.util.List;




import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;

import com.mongodb.DBObject;

/**
 * 
 * @author 3gods.com
 * 这里如何存表需要自己实现
 */
public class DeliverMessage {

	//获取这个日志需要保存到那个表中，但是我也不知道保存到哪里啊。
	//可自己根据日志类型，业务类型，操作时间等保存到不同的表
	//这里简要的按照日志来的时候的时间存在表中
	public static String getTableName(DBObject obj) {
//		String month = DateUtils.getCurrentMonth();
		String month = DateUtil.formatDate(new Date(), "yyyy-MM");
		
		return month;
	}
	
	
	public static String getTableName(List<DBObject> list) {
		String month = DateUtil.formatDate(new Date(), "yyyy-MM");
		
		return month;
	}
	
}
