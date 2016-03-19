package org.xzc.action.test.UserAction;

import org.bson.Document;
import org.junit.Test;
import org.xzc.action.test.MyTestBase;
import org.xzc.msg.action.UserAction;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;
import org.xzc.msg.domain.User;

public class TestState extends MyTestBase<UserAction> {
	public static final String URL = "/api2/user/state";

	public TestState() {
		super( URL );
	}

	/**
	 * 没登陆就state 得到isLogin=false
	 * @throws Exception
	 */
	@Test
	public void test_state_1() throws Exception {
		MyJSON mj = execute();
		//但是code依旧是成功
		assertEquals( mj.code(), Code.SUCCESS );
	}

	@Test
	public void test_state_2() throws Exception {
		mockLogin();
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );
		org.junit.Assert.assertTrue( mj.getResult() instanceof Document );
		Document d = (Document) mj.getResult();
		assertEquals( d.getBoolean( "isLogin" ), true );
		assertEquals( ( (User) d.get( "user" ) ).getName(), user.getName() );
	}
}
