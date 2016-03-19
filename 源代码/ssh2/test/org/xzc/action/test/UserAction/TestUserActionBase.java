package org.xzc.action.test.UserAction;

import javax.annotation.Resource;

import org.xzc.action.test.MyTestBase;
import org.xzc.msg.action.UserAction;
import org.xzc.msg.service.UserService;

public class TestUserActionBase extends MyTestBase<UserAction> {
	@Resource
	protected UserService userService;

	public TestUserActionBase(String url) {
		super( url );
	}

	protected TestUserActionBase _name(String name) {
		request.addParameter( "name", name );
		return this;
	}

	protected TestUserActionBase _password(String password) {
		request.addParameter( "password", password );
		return this;
	}
}
