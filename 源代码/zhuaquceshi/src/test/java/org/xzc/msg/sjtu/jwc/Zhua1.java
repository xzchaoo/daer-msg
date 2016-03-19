package org.xzc.msg.sjtu.jwc;

import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.xzc.msg.domain.SJTUJWCMessage;

public class Zhua1 {
	@Test
	public void teset_zhua_1() throws Exception {
		SJTUJWCMessageFetchCreator ns = new SJTUJWCMessageFetchCreator();
		ns.init();

		List<SJTUJWCMessage> list = ns.list();
		SJTUJWCMessage n = list.get( 0 );
		String content = ns.getContent( n.getLink() );
		System.out.println( content );

		//JSONObject jo = new JSONObject( list.get( 0 ) );
		//System.out.println( jo );
		//System.out.println( Utils.jsonToBean( jo, Notice.class ) );
		//System.out.println( Utils.jsonToBean( jo, Notice.class ).toJSON() );
	}

	public void testName() throws Exception {
		String url = "http://127.0.0.1:8080/ssh2/cc.jsp";
		//最有用的应该是socketTimeout 防止超时
		RequestConfig rc = RequestConfig.custom().setConnectTimeout( 1000 ).setSocketTimeout( 1000 )
				.setConnectionRequestTimeout( 1000 ).build();
		CloseableHttpClient hc = HttpClients.custom().setDefaultRequestConfig( rc ).build();
		String content = EntityUtils.toString( hc.execute( RequestBuilder.get( url ).build() ).getEntity() );
		System.out.println( content );
	}

	@Test
	public void testGetContent() {
	}
}
