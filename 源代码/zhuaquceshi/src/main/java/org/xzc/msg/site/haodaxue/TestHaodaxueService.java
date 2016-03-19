package org.xzc.msg.site.haodaxue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;
import org.xzc.msg.http.MyClient;

public class TestHaodaxueService {
	private MyClient mc = new MyClient();
	//这里是加密后的密码
	private HaodaxueService s = new HaodaxueService( "ceshi_20150709@163.com", "ZaQoskjcFmfPEBv8OuKRpg==", mc );

	@Test
	public void testLogin() {
		assertTrue( s.login() );
	}

	@Test
	public void testCourseSubject() throws Exception {
		JSONObject jo = s.getCourseSubject();
		assertEquals( jo.getString( "code" ), "000000" );
		assertEquals( "哲学",
				jo.getJSONObject( "result" ).getJSONArray( "subjects" ).getJSONObject( 0 ).getString( "name" ) );
	}

	@Test
	public void testCourseList() throws Exception {
		JSONObject jo = s.getCourseList( "08", "", 0, 0 );
		assertEquals( jo.getString( "code" ), "000000" );
	}

	@Test
	public void testCourseDetail() throws Exception {
		JSONObject jo = s.getCourseDetail( "159" );
		assertEquals( jo.getString( "code" ), "000000" );
		assertEquals( jo.getJSONObject( "result" ).getJSONObject( "course" ).getString( "courseName" ), "数据库原理与技术" );
	}

	@Test
	public void testCourseLearn() throws Exception {
		JSONObject jo = s.getCourseLearn( "159" );
		assertEquals( "000003", jo.getString( "code" ) );
		assertTrue( s.login() );
		jo = s.getCourseLearn( "159" );
		assertEquals( "000000", jo.getString( "code" ) );
		System.out.println( jo );
	}

}
