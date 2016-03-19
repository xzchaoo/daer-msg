package org.xzc.msg.convert;

import java.util.Map;

import org.bson.Document;
import org.xzc.msg.domain.UserMessage;
import org.xzc.msg.utils.Utils;

public class UserMessageUtils {

	public static UserMessage toUserMessage(Document d) {
		UserMessage um = new UserMessage();
		Utils.populate( um, d );
		um.setId( d.getInteger( "_id" ) );
		return um;
	}

	public static Document toDocument(UserMessage um) {
		Map<String, Object> map = Utils.describe( um );
		map.put( "_id", map.remove( "id" ) );
		Document d = new Document( map );
		return d;
	}

}
