package org.xzc.msg.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.xzc.msg.domain.User;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.utils.Utils;

public class UserUtils {

	public static User toUser(Document d) {
		if (d == null)
			return null;
		User u = new User();
		Utils.populate( u, d );
		u.setId( d.getInteger( "_id" ) );
		return u;
	}

	public static Document toDocument(User user) {
		if (user == null)
			return null;
		Map<String, Object> map = Utils.describe( user );
		map.remove( "id" );
		Document d = new Document( map );
		return d;
	}

	public static List<User> toUserList(List<Document> documents) {
		if (documents == null)
			return null;
		List<User> ret = new ArrayList<User>();
		for (Document d : documents) {
			ret.add( toUser( d ) );
		}
		return ret;
	}

	public static UserForSimpleInfo toSimpleUser(Document d) {
		if (d == null)
			return null;

		UserForSimpleInfo su = new UserForSimpleInfo();
		Utils.populate( su, d );
		su.setId( d.getInteger( "_id" ) );
		return su;
	}

}
