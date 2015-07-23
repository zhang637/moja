package com.song.moja.db;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.protobuf.MessageLite;
import com.mongodb.DBObject;
import com.song.moja.process.DeliverMessage;
import com.song.moja.process.ObjConvertUtil;

/**
 * 
 * @author songxin 这个类是描述消息最终保存到哪里的1.写HDFS 2.写MongDB
 */
public class SaveAdaptor<T> {
	private static Logger LOG = Logger.getLogger(SaveAdaptor.class);

	public static <T> boolean save2MongDB(T t) {
		if (null == t) {
			return false;
		}
		DBObject obj = ObjConvertUtil.t2DBObj(t);
		String tableName = DeliverMessage.getTableName(obj);
		boolean result = MongoSaveUtil.insert(obj, tableName);
		return result;
	}

	// 保存一个List
	public static <T> boolean save2MongDB(List<T> tempList) {
		if (null == tempList || tempList.size() <= 0) {
			return false;
		}
		List<DBObject> list = new ArrayList<DBObject>(tempList.size());
		String tableName = DeliverMessage.getTableName(list);
		T t2 = null;
		//这种的话就是protobuf的日志类型
		if(t2 instanceof MessageLite){
			MessageLite ml = (MessageLite)t2;
			ObjConvertUtil.PB2DBObj(ml);
		}
		for (T t : tempList) {
			String transferType = "";
			
			// 将t转换成DBObject
			DBObject obj = ObjConvertUtil.t2DBObj(t);
			list.add(obj);
		}
		boolean result = MongoSaveUtil.insertList(list, tableName);
		return result;
	}
}
