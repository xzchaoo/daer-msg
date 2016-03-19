package org.xzc.msg.http;

import java.nio.charset.Charset;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * 对HttpClient的常用功能的封装
 * @author xzchaoo
 *
 */
@Component
public class MyClient {
	public static final Charset UTF8 = Charset.forName( "utf-8" );

	/**
	 * 返回get的builder
	 * @param url
	 * @param params
	 * @return
	 */
	public static final RequestBuilder getBuilder(String url, MyParams params) {
		RequestBuilder rb = RequestBuilder.get( url );
		if (params != null) {
			addParams( rb, params );
		}
		return rb;
	}

	/**
	 * return getBuilder( url, null );
	 * @param url
	 * @return
	 */
	public static final RequestBuilder getBuilder(String url) {
		return getBuilder( url, null );
	}

	/**
	 * 返回post的builder
	 * @param url
	 * @param params
	 * @param datas
	 * @return
	 */
	public static final RequestBuilder postBuilder(String url, MyParams params, MyParams datas) {
		RequestBuilder rb = RequestBuilder.post( url );
		if (params != null) {
			addParams( rb, params );
		}
		if (datas != null) {
			try {
				rb.setEntity( new UrlEncodedFormEntity( datas.getList() ) );
			} catch (Exception e) {
				throw new RuntimeException( e );
			}
		}
		return rb;
	}

	/**
	 * 辅助方法 往rb里加params
	 * @param rb
	 * @param params
	 */
	public static void addParams(RequestBuilder rb, MyParams params) {
		for (NameValuePair nvp : params.getList())
			rb.addParameter( nvp );
	}

	private BasicCookieStore bcs;

	private CloseableHttpClient hc;

	public MyClient() {
		this.bcs = new BasicCookieStore();
		//TODO set rc
		this.hc = HttpClients.custom().setDefaultCookieStore( bcs ).build();
	}

	public MyClient(CloseableHttpClient hc) {
		if (hc == null) {
			throw new IllegalArgumentException( "hc不可以为null." );
		}
		this.hc = hc;
	}

	/**
	 * 执行hur返回CloseableHttpResponse
	 * @param hur
	 * @return
	 */
	public CloseableHttpResponse execute(HttpUriRequest hur) {
		CloseableHttpResponse res = null;
		try {
			res = hc.execute( hur );
			return res;
		} catch (Exception e) {
			HttpClientUtils.closeQuietly( res );
			throw new RuntimeException( e );
		}
	}

	/**
	 * 将其执行结果作为String返回
	 * @param hur
	 * @return
	 */
	public String asString(HttpUriRequest hur) {
		CloseableHttpResponse res = execute( hur );
		try {
			//Header h = res.getEntity().getContentEncoding();
			//if (h != null && h.getValue() != null)
			//	return EntityUtils.toString( res.getEntity(), h.getValue() );
			//return EntityUtils.toString( res.getEntity(), UTF8 );
			return EntityUtils.toString( res.getEntity(), UTF8 );
		} catch (Exception e) {
			throw new RuntimeException( e );
		} finally {
			HttpClientUtils.closeQuietly( res );
		}
	}

	public String asString(HttpUriRequest hur, String encoding) {
		CloseableHttpResponse res = execute( hur );
		try {
			//Header h = res.getEntity().getContentEncoding();
			//if (h != null && h.getValue() != null)
			//	return EntityUtils.toString( res.getEntity(), h.getValue() );
			//return EntityUtils.toString( res.getEntity(), UTF8 );
			return EntityUtils.toString( res.getEntity(), encoding );
		} catch (Exception e) {
			throw new RuntimeException( e );
		} finally {
			HttpClientUtils.closeQuietly( res );
		}
	}

	public void close() {
		HttpClientUtils.closeQuietly( hc );
	}

	public CloseableHttpResponse get(String url) {
		return execute( getBuilder( url ).build() );
	}

	/**
	 * 如果是通过默认构造函数创建的 则可以获得bcs
	 * @return
	 */
	public CookieStore getCookieStore() {
		return bcs;
	}

	public String getString(String url) {
		return asString( getBuilder( url ).build() );
	}

	public String getString(String url, String encoding) {
		return asString( getBuilder( url ).build(), encoding );
	}

	public String getString(String url, MyParams params) {
		return asString( getBuilder( url, params ).build() );
	}

	public CloseableHttpResponse post(String url, MyParams params, MyParams datas) {
		return execute( postBuilder( url, params, datas ).build() );
	}

	public String postString(String url, MyParams params, MyParams datas) {
		return asString( postBuilder( url, params, datas ).build() );
	}

}
