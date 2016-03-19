package org.xzc.msg;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTongquAPI {
	private static final String INDEX = "http://www.tongqu.me/index.php/";

	private CloseableHttpClient hc;
	private BasicCookieStore bcs;

	@Before
	public void before() {
		bcs = new BasicCookieStore();
		hc = HttpClients.custom().setDefaultCookieStore( bcs ).build();
	}

	@After
	public void after() {
		HttpClientUtils.closeQuietly( hc );
	}

	/**
	 * 获得单个活动的详情 获得的json有[main_info, msg, body, error, version]属性 其中main_info描述了一些诸如名称 起始时间 结束时间等信息 body就是这个活动共对应的网页的内容
	 * msg error version 暂时没多大用处
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_url_act_detail() throws Exception {
		// url_act_detail
		String url = INDEX + "/api/act/detail?id=" + 9877;
		String res = Request.Get( url ).execute().returnContent().asString();
		JSONObject jo = new JSONObject( res );
		assertEquals( jo.getJSONObject( "main_info" ).getString( "name" ), "小学期美妆班开始报名啦" );
	}

	private static final String URL_IS_LOGIN = INDEX + "/api/user/is_login";

	/**
	 * 判断是否登陆
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_url_is_login() throws Exception {
		//不知道为什么直接使用fluent会有问题
		JSONObject jo = new JSONObject( HttpUtils.getReturnString( hc, URL_IS_LOGIN ) );
		System.out.println( jo );
	}

	public static final String URL_LOGIN = INDEX + "/api/user/login";

	/**
	 * <string name="url_login">/api/user/login</string>
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_url_login() throws Exception {
		// 要以post方式
		List<NameValuePair> datas = new ArrayList<NameValuePair>();
		datas.add( new BasicNameValuePair( "user_name", "8147532" ) );
		datas.add( new BasicNameValuePair( "password", "708620451" ) );
		JSONObject jo = new JSONObject( EntityUtils.toString( hc.execute(
				RequestBuilder.post( URL_LOGIN ).setEntity( new UrlEncodedFormEntity( datas ) ).build() ).getEntity() ) );

		assertEquals( jo.getInt( "error" ), 1 );

		jo = new JSONObject( HttpUtils.getReturnString( hc, URL_IS_LOGIN ) );
		assertEquals( jo.getString( "msg" ), "尚未登录" );
		assertEquals( jo.getInt( "error" ), 1 );

		datas.remove( 1 );
		datas.add( new BasicNameValuePair( "password", "70862045" ) );
		jo = new JSONObject( EntityUtils.toString( hc.execute(
				RequestBuilder.post( URL_LOGIN ).setEntity( new UrlEncodedFormEntity( datas ) ).build() ).getEntity() ) );
		assertEquals( jo.getInt( "error" ), 0 );

		jo = new JSONObject( HttpUtils.getReturnString( hc, URL_IS_LOGIN ) );
		assertEquals( jo.getString( "msg" ), "已登录" );
		assertEquals( jo.getInt( "error" ), 0 );
	}
}
