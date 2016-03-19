package org.xzc.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Utils {

	public static void checkNull(Object obj, String name) {
		if (obj == null)
			throw new IllegalArgumentException( name + "不能为null." );
	}

	public static void dump(Object bean) {
		try {
			Map<String, String> map = BeanUtils.describe( bean );
			List<String> keys = new ArrayList<String>( map.keySet() );
			Collections.sort( keys );
			for (String k : keys) {
				System.out.println( k + " " + map.get( k ) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void jsonToBean(JSONObject json, Object bean) {
		try {
			BeanUtils.populate( bean, jsonToMap( json ) );
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
	}

	public static <T> T jsonToBean(JSONObject json, Class<T> clazz) {
		try {
			T t = clazz.newInstance();
			jsonToBean( json, t );
			return t;
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
	}

	public static List<Object> jsonToList(JSONArray json) {
		List<Object> ret = new ArrayList<Object>();
		for (int i = 0; i < json.length(); ++i) {
			Object value = json.get( i );
			if (value instanceof JSONObject) {
				value = jsonToMap( (JSONObject) value );
			} else if (value instanceof JSONArray) {
				value = jsonToList( (JSONArray) value );
			}
			ret.add( value );
		}
		return ret;
	}

	public static Map<String, Object> jsonToMap(JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Iterator<String> iter = json.keys(); iter.hasNext();) {
			String key = iter.next();
			Object value = json.get( key );
			if (value instanceof JSONObject) {
				value = jsonToMap( (JSONObject) value );
			} else if (value instanceof JSONArray) {
				value = jsonToList( (JSONArray) value );
			}
			ret.put( key, value );
		}
		return ret;
	}
}
