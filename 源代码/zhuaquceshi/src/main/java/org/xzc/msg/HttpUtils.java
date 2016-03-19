package org.xzc.msg;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.xzc.msg.http.MyParams;

public class HttpUtils {
	public static final Charset UTF8 = Charset.forName( "utf-8" );

	public static String getReturnString(CloseableHttpClient hc, String url) throws ClientProtocolException, IOException {
		return getReturnString( hc, RequestBuilder.get( url ).build() );
	}

	public static String getReturnString(CloseableHttpClient hc, HttpUriRequest hur) throws ClientProtocolException,
			IOException {
		HttpResponse res = null;
		try {
			res = hc.execute( hur );
			return EntityUtils.toString( res.getEntity(), UTF8 );
		} finally {
			HttpClientUtils.closeQuietly( res );
		}
	}

	public static CloseableHttpResponse post(CloseableHttpClient hc,String url, MyParams params, MyParams datas)
			throws ClientProtocolException, IOException {
		RequestBuilder rb = RequestBuilder.post( url );
		if (params != null) {
			for (NameValuePair nvp : params.getList())
				rb.addParameter( nvp );
		}
		if (datas != null) {
			rb.setEntity( datas.toEntity() );
		}
		return hc.execute( rb.build() );
	}

	public static String postReturnString(CloseableHttpClient hc,String url, MyParams params, MyParams datas) throws ClientProtocolException, IOException {
		CloseableHttpResponse res = null;
		try {
			res = post( hc,url, params, datas);
			return EntityUtils.toString( res.getEntity(), UTF8 );
		} finally {
			HttpClientUtils.closeQuietly( res );
		}
	}

}
