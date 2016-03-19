package org.xzc.msg.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.msg.dao.impl.UserDao;
import org.xzc.msg.domain.User;
import org.xzc.msg.domain.UserForRegisterAndLogin;

@Service
public class UserServiceForFetch {

	@Resource
	private UserDao userDao;

	public User getMessagePublisher(int type) {
		switch (type) {
		case MessageType.TONGQU:
			return userDao.get( "tongquwang" );
		case MessageType.SJTUJWC:
			return userDao.get( "sjtujwc" );
		}
		return null;
	}

	@PostConstruct
	public void init() {
		//保证发布账号都存在
		ensureUserExists("tongquwang","70862045");
		ensureUserExists("sjtujwc","70862045");
	}

	private void ensureUserExists(String name, String password) {
		User u = userDao.get( name );
		if (u == null) {
			UserForRegisterAndLogin user=new UserForRegisterAndLogin();
			user.name=name;
			user.password=password;
			userDao.add( user );
		} else {
			u.setPassword( password );
			userDao.update( u );
		}
	}
}
