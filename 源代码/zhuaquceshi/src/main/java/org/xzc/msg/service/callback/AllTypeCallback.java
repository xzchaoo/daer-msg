package org.xzc.msg.service.callback;

import java.util.Date;

import org.xzc.msg.domain.Message;

public class AllTypeCallback implements IServiceCallback {

	public Message beforeAddCallback(Message msg) {
		//添加两个属性
		Date now = new Date();
		return msg;
	}

	public Message beforeGetCallback(Message msg) {
		return msg;
	}

	public Message beforeUpdateCallback(Message msg) {
		Date now = new Date();
		return msg;
	}

}
