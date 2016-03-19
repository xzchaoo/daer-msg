package org.xzc.msg.utils;

import org.xzc.msg.app.MsgApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsUtils {
	public static SharedPreferences getSharedPreferences(String name) {
		return MsgApplication.app.getSharedPreferences( name, Context.MODE_PRIVATE );
	}

	public static SharedPreferences getDefaultPreferences() {
		return PreferenceManager.getDefaultSharedPreferences( MsgApplication.app );
	}

	public static boolean allowAutoLogin() {
		return getDefaultPreferences().getBoolean( "auto_login", false );
	}

	public static boolean allowRememberLastUsername() {
		return getDefaultPreferences().getBoolean( "remember_last_username", false );
	}
	
	
}
