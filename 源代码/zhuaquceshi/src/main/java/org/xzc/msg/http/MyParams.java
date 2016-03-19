package org.xzc.msg.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class MyParams {
	private List<NameValuePair> params = new ArrayList<NameValuePair>();

	public MyParams add(String name, Object value) {
		params.add( new BasicNameValuePair( name, value == null ? "" : value.toString() ) );
		return this;
	}

	public List<NameValuePair> getList() {
		return params;
	}

	public UrlEncodedFormEntity toEntity() {
		try {
			return new UrlEncodedFormEntity( params );
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static MyParams create(String name, Object value) {
		MyParams mp = new MyParams();
		return mp.add( name, value );
	}

}
