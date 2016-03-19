package org.xzc.msg.domain;

import java.util.Date;

public class UserMessage {
	public int id;
	public Date lastUpdateTime;
	public int msgId;
	public int state;
	public int userId;
	
	public int getId() {
		return id;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public int getMsgId() {
		return msgId;
	}

	public int getState() {
		return state;
	}

	public int getUserId() {
		return userId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
