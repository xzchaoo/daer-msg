package org.xzc.msg.convert;

import java.util.Map;

import org.bson.Document;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.SJTUJWCMessage;
import org.xzc.msg.domain.SimpleMessage;
import org.xzc.msg.domain.TongquMessage;
import org.xzc.msg.service.MessageType;
import org.xzc.msg.utils.Utils;

public class MessageUtils {
	public static Document simpleToDocument(Message msg) {
		Map<String, Object> map = Utils.describe( msg );
		Document d = new Document( map );
		return d;
	}

	public static Message toMessage(Document d) {
		if (d == null)
			return null;
		int type = d.getInteger( "type" );
		Message m = null;
		switch (type) {
		case MessageType.SIMPLE:
			m = new SimpleMessage();
			break;
		case MessageType.TONGQU:
			m = new TongquMessage();
			break;
		case MessageType.SJTUJWC:
			m = new SJTUJWCMessage();
			break;
		}
		if (m != null) {
			Utils.fromDocument( d, m );
			return m;
		}
		throw new RuntimeException( "没有对应的type" + type );
	}

}
