package org.xzc.action.test;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xzc.msg.action.json.MyJSON;
import org.xzc.msg.domain.User;
import org.xzc.msg.interceptor.IMyJSONSupported;
import org.xzc.msg.service.UserServiceForTest;
import org.xzc.msg.service.UserSessionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MyTestBase<T extends IMyJSONSupported> extends StrutsSpringJUnit4TestCase<T> {

	protected final String url;

	public MyTestBase(String url) {
		this.url = url;
	}

	protected MyTestBase<T> add(String name, Object obj) {
		request.addParameter( name, obj.toString() );
		return this;
	}

	protected MyJSON execute(String url) throws UnsupportedEncodingException, ServletException {
		executeAction( url );
		return getAction().getMyJSON();
	}

	protected MyJSON execute() throws UnsupportedEncodingException, ServletException {
		executeAction( this.url );
		return getAction().getMyJSON();
	}

	protected static void assertEquals(Object o1, Object o2) {
		org.junit.Assert.assertEquals( o1, o2 );
	}

	protected User user;

	@Resource
	protected UserServiceForTest userServiceForTest;

	//伪造登陆
	protected void mockLogin() {
		user = new User();
		user.setName( "ceshi" );
		user.setPassword( "70862045" );
		userServiceForTest.ensureExists( user );
		//伪装登陆
		request.getSession().setAttribute( UserSessionService.LOGIN_KEY, user );
	}
	
}
