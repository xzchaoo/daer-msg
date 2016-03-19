package org.xzc.msg.utils;

public class Assert {
	public static void notNull(Object obj) {
		notNull( obj, "obj不能为null." );
	}

	public static void notNull(Object obj, String message) {
		if (obj == null)
			throw new IllegalArgumentException( message );
	}

}
