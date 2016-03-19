package org.xzc.msg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

public class MyHttpUtils {
	//public static final BasicCookieStore bcs = new BasicCookieStore();

	public static String format(String string) {
		return "<!DOCTYPE html><html><head><meta charset=\"utf-8\"></head><body>" + string + "</body></html>";
	}
	public static AsyncHttpClient createAHC(Context ctx) {
		AsyncHttpClient ahc = new AsyncHttpClient();
		PersistentCookieStore pcs = new PersistentCookieStore( ctx );
		ahc.setCookieStore( pcs );
		return ahc;
	}

	public static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	public static String formatToServer(Date d) {
		if(d==null)
			return "";
		return SDF.format( d );
	}
}
