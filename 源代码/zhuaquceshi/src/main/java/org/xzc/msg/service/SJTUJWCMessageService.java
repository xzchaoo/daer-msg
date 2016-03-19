package org.xzc.msg.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.xzc.msg.dao.impl.SJTUJWCMessageDao;
import org.xzc.msg.domain.SJTUJWCMessage;

@Component
public class SJTUJWCMessageService {

	@Resource
	private SJTUJWCMessageDao sjtuIsjtujwcMessageDao;

	public SJTUJWCMessage get(String link) {
		return sjtuIsjtujwcMessageDao.get( link );
	}
}
