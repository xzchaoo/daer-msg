package org.xzc.msg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.xzc.msg.factory.XZCFactory;
import org.xzc.msg.fetch.TongquFetchService;

public class TestTongquService {
	private TongquFetchService s = XZCFactory.getTongquService();

	@Test
	public void testActDetail() throws Exception {
		JSONObject jo = s.getActDetail( 9877 );
		JSONObject mi = jo.getJSONObject( "main_info" );
		assertEquals( mi.getInt( "actid" ), 9877 );
	}

	@Test
	public void testTypeList() {
		JSONArray ja = s.getTypeList();
		assertNotNull( ja );
	}

	@Test
	public void testListAct() {
		JSONObject jo = s.getListAct( -1, 0, "hotvalue", 1, 10 );
		JSONObject result = jo.getJSONObject( "result" );
		assertEquals( 10, jo.getJSONObject( "result" ).getJSONArray( "acts" ).length() );
	}

	@Test
	public void testSearch() throws Exception {
		JSONObject jo = s.getSearch( "暑假", 2 );
		assertEquals( jo.getInt( "error" ), 0 );
	}

}
