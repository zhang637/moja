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
