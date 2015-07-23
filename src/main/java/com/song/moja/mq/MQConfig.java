package com.song.moja.mq;
import java.util.Properties;
import com.song.moja.server.Config;
import com.song.moja.util.Utils;

/**
 * 关于MQ的一些配置获取
 * @author 3gods.com
 * ok
 */
public class MQConfig extends Config{
	
	public MQConfig(Properties props) {
		super(props);
	}

	public long getSleepTime(){
		return Utils.getLong(props,"mq.sleep.time",50L);
	}
	
	//mq初始化数�?
	public int getInitSize(){
		return Utils.getInt(props,"mq.init.size",5000);
	}
	
	//mq�?��的数�?
	public int getMaxSize(){
		return  Utils.getInt(props,"mq.max.size",100000);
	}

	//mq批量处理数量
	public int getBatchSize(){
		return Utils.getInt(props,"mq.batch.size",1000);
	}
	
	public int getSafeNum(){
		return Utils.getInt(props,"mq.safe.num",25000);
	}
	
	public int getWarnNum(){
		return Utils.getInt(props,"mq.warn.num",75000);
	}

	public int getLogMaxSize() {
		return Utils.getInt(props,"log.size",16);
	}

	public int getThreadNum() {
		return Utils.getInt(props,"mq.thread.num",10);
	}

	public String getLogDir() {
		return Utils.getString(props, "log.dir", "/logs");
	}
	
	
}
