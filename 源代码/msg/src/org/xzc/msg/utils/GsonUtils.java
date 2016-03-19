package org.xzc.msg.utils;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.domain.Page;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
	private static final Gson gson;
	static {
		GsonBuilder gb = new GsonBuilder();
		gb.setDateFormat( "yyyy-MM-dd HH:mm:ss" );
		gson = gb.create();
	}

	public static <T> T fromJson(JSONObject json, Class<T> clazz) {
		return gson.fromJson( json.toString(), clazz );
	}

	public static <T> T fromJson(String json, Type type) {
		return gson.fromJson( json, type );
	}

	public static <T> T fromJson(JSONArray json, Type type) {
		return gson.fromJson( json.toString(), type );
	}

	public static Page<MessageForList> fromJson(JSONObject json, Type type) {
		return gson.fromJson( json.toString(), type );
	}
}
