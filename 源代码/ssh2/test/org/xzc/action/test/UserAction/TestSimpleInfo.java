package org.xzc.action.test.UserAction;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xzc.action.test.MyTestBase;
import org.xzc.msg.action.UserAction;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;
import org.xzc.msg.domain.UserForSimpleInfo;

public class TestSimpleInfo extends MyTestBase<UserAction> {
	public static final String URL = "/api2/user/simpleInfo";

	public TestSimpleInfo() {
		super( URL );
	}

	/**
	 * 不存在的用户
	 * @throws Exception
	 */
	@Test
	public void test_simpleInfo_1() throws Exception {
		add( "id", 70862045 );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.NO_USER );
	}

	/**
	 * 存在的用户
	 * @throws Exception
	 */
	@Test
	public void test_simpleInfo_2() throws Exception {
		mockLogin();
		add( "id", user.getId() );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );
		assertTrue( mj.getResult() instanceof UserForSimpleInfo );
		UserForSimpleInfo su = (UserForSimpleInfo) mj.getResult();
		assertEquals( su.getName(), user.getName() );
		assertEquals( su.getId(), user.getId() );
	}
}
