package org.xzc.msg.exception;

public class RepeatedUserMessageException extends Exception {

	private int msgId;
	private int umId;
	private int userId;

	public RepeatedUserMessageException(int userId, int msgId, int umId) {
		super( "(userId,msgId)=(" + userId + "," + msgId + ")" + "已经存在了,umId=" + umId + "." );
		this.userId = userId;
		this.msgId = msgId;
		this.umId = umId;
	}

	public int getMsgId() {
		return msgId;
	}

	public int getUmId() {
		return umId;
	}

	public int getUserId() {
		return userId;
	}

}
