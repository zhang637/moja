package com.song.moja.db;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class MongoSaveUtil {

	private static Logger LOG = Logger.getLogger(MongoSaveUtil.class);

	/**
	 * @throws Exception
	 * 
	 * 			@author： @date：2015年6月19日 @Description：向mongodb中插入一个list，
	 *             返回消费失败的List @param tableName @param dbList @return:
	 *             返回结果描述 @return List<DBObject>: 返回值类型 @throws
	 */
	public static boolean insertList(List<DBObject> dbList, String tableName) {
		boolean result = false;
		// 验证参数
		if (StringUtils.isEmpty(tableName) || null == dbList || dbList.size() == 0) {
			return false;
		}

		DB db = null;
		DBCollection collection = null;
		WriteResult wr = null;
		try {
			db = MongoUtil.getDB();
			db.requestStart();
			collection = db.getCollection(tableName);
			wr = collection.insert(dbList, WriteConcern.SAFE);
			if (!StringUtils.isEmpty(wr.getError())) {
				// 如果消费失败
				result = false;
			} else {
				result = true;
			}

			db.requestDone();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			return result;
		}
	}

	/**
	 * 
	 * @author： @date：2015年6月10日 @Description：向mongodb中插入一个DBObject @param
	 * obj @param tableName @return: 返回结果描述 @return boolean: 返回值类型 @throws
	 */
	public static boolean insert(DBObject obj, String tableName) {
		boolean result = true;
		try {
			DB db = MongoUtil.getDB();

			db.requestStart();
			DBCollection collection = db.getCollection(tableName);
			WriteResult ret = collection.insert(obj, WriteConcern.SAFE);
			if (!StringUtils.isEmpty(ret.getError())) {
				result = false;
			}
			db.requestDone();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

}
