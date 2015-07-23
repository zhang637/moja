package com.song.moja.log;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 发送邮件的线程，通过log4j中配置的
 * @author 3gods.com
 *
 */
public class MailThread extends Thread{
	
	final String mailMsg;
	// 发送邮件的日志
	final Logger mailLogger;
	public MailThread(String mailMsg){
		this.mailMsg = mailMsg;
		this.mailLogger = Logger.getLogger("MAIL");
	}

	@Override
	public void run() {
		if(!StringUtils.isEmpty(mailMsg)){
			mailLogger.error(mailMsg);
		}
	}
}
