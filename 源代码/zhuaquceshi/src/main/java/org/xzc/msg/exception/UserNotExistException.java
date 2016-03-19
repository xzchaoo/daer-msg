package org.xzc.msg.exception;

public class UserNotExistException extends KnownException{

	public UserNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super( message, cause, enableSuppression, writableStackTrace );
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String detailMessage, Throwable throwable) {
		super( detailMessage, throwable );
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String detailMessage) {
		super( detailMessage );
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(Throwable throwable) {
		super( throwable );
		// TODO Auto-generated constructor stub
	}

}