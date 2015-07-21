package com.song.moja.process;

import java.util.List;

import com.mongodb.DBObject;
import com.song.moja.util.DateUtils;

/**
 * 
 * <p>
 * 文件名称:DeliverMsg.java
 * <p>
 * <p>
 * 文件描述:本类描述
 * <p>
 * 版权所有:湖南省蓝蜻蜓网络科技有限公司版权所有(C)2015
 * </p>
 * <p>
 * 内容摘要:这个类是分发日志的，因为希望Mongodb可以做到批量存储，所以将要存到一个表中的数据集中存放
 * </p>
 * <p>
 * 其他说明:其它内容的说明
 * </p>
 * <p>
 * 完成日期:2015年6月10日上午9:22:32
 * </p>
 * <p>
 * 
 * @author
 */
public class DeliverMessage {

	//获取这个日志需要保存到那个表中，但是我也不知道保存到哪里啊。
	//可自己根据日志类型，业务类型，操作时间等保存到不同的表
	//这里简要的按照日志来的时候的时间存在表中
	public static String getTableName(DBObject obj) {
		String month = DateUtils.getCurrentMonth();
		
		return month;
	}
	
	
	public static String getTableName(List<DBObject> list) {
		String month = DateUtils.getCurrentMonth();
		
		return month;
	}
	
}
