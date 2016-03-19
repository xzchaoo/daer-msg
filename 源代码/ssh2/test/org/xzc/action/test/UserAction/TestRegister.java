package org.xzc.action.test.UserAction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;

public class TestRegister extends TestUserActionBase {
	public static final String URL = "/api2/user/register";

	public TestRegister() {
		super( URL );
	}

	/**
	 * 账号合法 密码格式都合法 并且假设账号不存在
	 * @throws Exception
	 */
	@Test
	public void test_login_6() throws Exception {
		String name = "ceshi";
		String password = "70862045";
		userService.delete( name );
		_name( name )._password( password );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );
	}

	/**
	 * 账号密码都不写 应该返回账号格式错误
	 * @throws Exception
	 */
	@Test
	public void test_register_1() throws Exception {
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.WRONG_NAME_FORMAT );
	}

	/**
	 * 账号和密码长度都不足 应该提示账号格式错误(因为检查顺序)
	 * @throws Exception
	 */
	@Test
	public void test_register_2() throws Exception {
		_name( "aa" )._password( "12345" );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.WRONG_NAME_FORMAT );
	}

	/**
	 * 账号密码长度太长 应该提示name格式错误
	 * @throws Exception
	 */
	@Test
	public void test_register_3() throws Exception {
		_name( "12345678901234567980" )._password( "12345678901234567980" );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.WRONG_NAME_FORMAT );
	}

	/**
	 * 账号合法 密码包含非法字符
	 * @throws Exception
	 */
	@Test
	public void test_register_4() throws Exception {
		_name( "xzchaoo" )._password( "70862045@" );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.WRONG_PASSWORD_FORMAT );
	}

	/**
	 * 账号合法 密码格式都合法 并且假设账号已经存在
	 * @throws Exception
	 */
	@Test
	public void test_register_5() throws Exception {
		String name = "ceshi";
		String password = "70862045";
		userService._create( name, password );
		_name( name )._password( password );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.NAME_EXISTS );
	}

}
