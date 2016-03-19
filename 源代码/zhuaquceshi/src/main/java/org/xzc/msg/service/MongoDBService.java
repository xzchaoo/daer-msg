package org.xzc.msg.service;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.stereotype.Service;
import org.xzc.msg.mongodb.IDGenerator;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class MongoDBService {
	private MongoClient mc;
	private MongoDatabase md;

	@PostConstruct
	public void init() {
		mc = new MongoClient();
		md = mc.getDatabase( "msg" );
	}

	public MongoCollection<Document> getUsers() {
		return md.getCollection( "users" );
	}

	public MongoCollection<Document> getMsgs() {
		return md.getCollection( "msgs" );
	}

	/**
	 * 获得用于表示 每个网站的更新情况 的集合
	 * @return
	 */
	public MongoCollection<Document> getSiteStateCollection() {
		return md.getCollection( "site_state" );
	}

	public MongoCollection<Document> getUserMessageCollections() {
		return md.getCollection( "user_messages" );
	}

	public IDGenerator getIDGenerator(String idName) {
		return new IDGenerator( md.getCollection( "counters" ), idName );
	}

	public MongoCollection<Document> getGroups() {
		return md.getCollection( "groups" );
	}

	public MongoDatabase getDatabase() {
		return md;
	}
}
