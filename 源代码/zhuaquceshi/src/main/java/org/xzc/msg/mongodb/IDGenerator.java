package org.xzc.msg.mongodb;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;

/**
 * 用于MongoDB里面生成 自动增长的主键
 * @author xzchaoo
 *
 */
public class IDGenerator {

	private final MongoCollection<Document> counters;
	private final String idname;

	public IDGenerator(MongoCollection<Document> counters, String idname) {
		this.counters = counters;
		this.idname = idname;
	}

	public int getNextId() {
		return getNextId( 1 );
	}

	public int getNextId(int count) {
		//_id是id的名字 value是值
		Document d = counters.findOneAndUpdate( eq( "_id", idname ), new Document( "$inc",
				new Document( "value", count ) ), new FindOneAndUpdateOptions().upsert( true ) );
		return d == null ? 1 : ( d.getInteger( "value" ) + 1 );
	}

}
