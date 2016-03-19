package org.xzc.msg.exception;

/**
 * 已知的错误 可以由拦截器自动处理
 * @author xzchaoo
 *
 */
public class KnownException extends Exception {

	public KnownException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

	public KnownException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KnownException(String detailMessage, Throwable throwable) {
		super( detailMessage, throwable );
		// TODO Auto-generated constructor stub
	}

	public KnownException(String detailMessage) {
		super( detailMessage );
		// TODO Auto-generated constructor stub
	}

	public KnownException(Throwable throwable) {
		super( throwable );
		// TODO Auto-generated constructor stub
	}

}
