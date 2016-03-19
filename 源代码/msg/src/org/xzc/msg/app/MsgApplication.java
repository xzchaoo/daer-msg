package org.xzc.msg.app;

import org.xzc.msg.R;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

public class MsgApplication extends Application {

	public static MsgApplication app;
	public static AsyncHttpClient ahc;
	public static PersistentCookieStore pcs;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		ahc = new AsyncHttpClient();
		config();
		pcs = new PersistentCookieStore( this );
		ahc.setCookieStore( pcs );
	}

	public void config() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( this );
		//其实下面这个才是关键
		//否则 重试+睡觉 会让你的系统足足1分钟才会收到onFailure
		int timeout = Integer.parseInt( sp.getString( "timeout", "1000" ) );
		int retry = Integer.parseInt( sp.getString( "retry", "1" ) );
		ahc.setTimeout( timeout );
		ahc.setMaxRetriesAndTimeout( retry, 500 );
	}
}
