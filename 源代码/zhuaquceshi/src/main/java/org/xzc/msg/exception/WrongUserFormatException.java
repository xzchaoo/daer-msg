package org.xzc.msg.exception;

import org.xzc.msg.action.code.Code;

/**
 * 用户的信息错误的时候发出的异常
 * @author xzchaoo
 *
 */
public class WrongUserFormatException extends KnownException {

	public WrongUserFormatException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WrongUserFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super( message, cause, enableSuppression, writableStackTrace );
		// TODO Auto-generated constructor stub
	}

	public WrongUserFormatException(String message, Throwable cause) {
		super( message, cause );
		// TODO Auto-generated constructor stub
	}

	public WrongUserFormatException(String message) {
		super( message );
		// TODO Auto-generated constructor stub
	}

	public WrongUserFormatException(Throwable cause) {
		super( cause );
		// TODO Auto-generated constructor stub
	}

}
