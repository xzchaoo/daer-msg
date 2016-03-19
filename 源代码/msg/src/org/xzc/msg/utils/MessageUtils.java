package org.xzc.msg.utils;

import org.json.JSONObject;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.SJTUJWCMessage;
import org.xzc.msg.domain.SimpleMessage;
import org.xzc.msg.domain.TongquMessage;

public class MessageUtils {
	public static Message toMessage(JSONObject json) {
		int type = json.optInt( "type", 0 );
		switch (type) {
		case 1:
			return GsonUtils.fromJson( json, SimpleMessage.class );
		case 2:
			return GsonUtils.fromJson( json, TongquMessage.class );
		case 3:
			return GsonUtils.fromJson( json, SJTUJWCMessage.class );
		}
		return null;
	}
}
