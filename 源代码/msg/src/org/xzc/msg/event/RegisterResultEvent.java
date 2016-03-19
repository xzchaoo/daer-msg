package org.xzc.msg.event;

public class RegisterResultEvent {
	public final boolean success;
	public final String msg;

	public RegisterResultEvent(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

}
