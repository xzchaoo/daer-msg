package org.xzc.msg.convert;

import org.bson.Document;
import org.xzc.msg.domain.Group;
import org.xzc.msg.domain.GroupForList;
import org.xzc.msg.domain.GroupFullInfo;
import org.xzc.msg.utils.Utils;

public class GroupUtils {
	public static Document toDocument(Group g) {
		Document d = new Document( Utils.describe( g ) );
		d.remove( "id" );
		return d;
	}

	public static Group toGroup(Document d) {
		if (d == null)
			return null;
		Group g = new Group();
		d.append( "id", d.remove( "_id" ) );
		Utils.populate( g, d );
		return g;
	}

	public static GroupFullInfo toGroupFullInfo(Document d) {
		if (d == null)
			return null;
		GroupFullInfo g = new GroupFullInfo();
		d.append( "id", d.remove( "_id" ) );
		Utils.populate( g, d );
		return g;
	}

}
