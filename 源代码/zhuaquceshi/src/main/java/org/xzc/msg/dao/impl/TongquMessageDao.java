package org.xzc.msg.dao.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Component;
import org.xzc.msg.convert.MessageUtils;
import org.xzc.msg.domain.TongquMessage;
import org.xzc.msg.service.MessageType;
import org.xzc.msg.service.MongoDBService;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

@Component
public class TongquMessageDao  {

	@Resource
	private MongoDBService mongoDBService;
	private MongoCollection<Document> msgs;

	public TongquMessage get(int actid) {
		Document d = msgs.find( Filters.and( Filters.eq( "type", MessageType.TONGQU ), Filters.eq( "actid", actid ) ) )
				.first();
		return (TongquMessage) MessageUtils.toMessage( d );
	}

	@PostConstruct
	public void init() {
		msgs = mongoDBService.getMsgs();
	}

}
