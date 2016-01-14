package com.song.moja.mq;

import java.io.Serializable;

import org.bson.types.ObjectId;

/**
 * 对客户端消息进行包装，添加额外属性，后续多样处理
 * 
 * @author 3gods.com ok
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	// id,全局唯一，
	private String id;
	// 消息内容
	private Object obj;
	// 消息持久化优先级，比如加入队列失败的，和消费失败的持久化优先级较高，加入队列成功 的优先级低
	// 可自己实现
	private int persistPriority;
	// 构造器

	public Message(Object obj) {
		this.obj = obj;
		id = String.valueOf(ObjectId.get());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public int getPersistPriority() {
		return persistPriority;
	}

	public void setPersistPriority(int persistPriority) {
		this.persistPriority = persistPriority;
	}

}
