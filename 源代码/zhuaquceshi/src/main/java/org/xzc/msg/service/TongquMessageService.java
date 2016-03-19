package org.xzc.msg.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.xzc.msg.dao.impl.TongquMessageDao;
import org.xzc.msg.domain.TongquMessage;

@Component
public class TongquMessageService {

	@Resource
	private TongquMessageDao tongquMessageDao;

	public TongquMessage get(int actid) {
		return tongquMessageDao.get( actid );
	}
}
