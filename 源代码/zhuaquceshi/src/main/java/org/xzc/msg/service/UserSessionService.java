package org.xzc.msg.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.xzc.msg.Utils;
import org.xzc.msg.domain.User;

@Service
public class UserSessionService {
	public static final String LOGIN_KEY = "login_key";

	public boolean isLogin(Map<String, Object> session) {
		return getCurrentUser( session ) != null;
	}

	public User getCurrentUser(Map<String, Object> session) {
		Utils.checkNull( session, "session" );
		return (User) session.get( LOGIN_KEY );
	}

	public void setCurrentUser(Map<String, Object> session, User user) {
		Utils.checkNull( session, "sessoin" );
		Utils.checkNull( session, "user" );
		session.put( LOGIN_KEY, user );
	}

	public void logout(Map<String, Object> session) {
		Utils.checkNull( session, "sessoin" );
		session.remove( LOGIN_KEY );
	}
}
