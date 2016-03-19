package org.xzc.msg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	public static final SimpleDateFormat SDF_YYN = new SimpleDateFormat( "yyyy-MM-dd" );

	public static String format(Date d) {
		return SDF.format( d );
	}
	public static String formatYYR(Date d){
		return SDF_YYN.format( d );
	}
}
