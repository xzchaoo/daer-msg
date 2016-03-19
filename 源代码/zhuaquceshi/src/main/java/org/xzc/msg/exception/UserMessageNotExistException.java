package org.xzc.msg.exception;

public class UserMessageNotExistException extends Exception {
	private int umId;

	public UserMessageNotExistException(int umId) {
		super( "umId=" + umId + "的用户消息不存在." );
		this.umId = umId;
	}

	public int getUmId() {
		return umId;
	}

}
