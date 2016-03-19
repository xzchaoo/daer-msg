package org.xzc.action.test.UserAction;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;

public class TestLogout extends TestUserActionBase {
	public static final String URL = "/api2/user/logout";

	public TestLogout() {
		super( URL );
	}

	/**
	 * 在没有登陆的情况下 注销
	 * @throws Exception
	 */
	@Test
	public void test_logout_1() throws Exception {
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.NOT_LOGIN );
	}

	/**
	 * 在已经登陆的情况下 注销
	 * @throws Exception
	 */
	@Test
	public void test_logout_2() throws Exception {
		String name = "zhangsan";
		String password = "zhangsan";
		userService._create( name, password );
		_name( name )._password( password );
		executeAction( "/api2/user/login" );
		MyJSON mj = getAction().getMyJSON();
		assertEquals( mj.code(), Code.SUCCESS );

		request.removeAllParameters();
		mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );
	}

}
