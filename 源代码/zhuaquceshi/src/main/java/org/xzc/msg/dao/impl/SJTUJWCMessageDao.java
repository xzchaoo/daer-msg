package org.xzc.msg.dao.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Component;
import org.xzc.msg.convert.MessageUtils;
import org.xzc.msg.domain.SJTUJWCMessage;
import org.xzc.msg.service.MessageType;
import org.xzc.msg.service.MongoDBService;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

@Component
public class SJTUJWCMessageDao  {

	@Resource
	private MongoDBService mongoDBService;
	private MongoCollection<Document> msgs;

	public SJTUJWCMessage get(String link) {
		Document d = msgs.find( Filters.and( Filters.eq( "type", MessageType.SJTUJWC ), Filters.eq( "link", link ) ) )
				.first();
		return (SJTUJWCMessage) MessageUtils.toMessage( d );
	}

	@PostConstruct
	public void init() {
		msgs = mongoDBService.getMsgs();
	}

}
