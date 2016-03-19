package org.xzc.msg.convert;

import org.bson.Document;
import org.json.JSONObject;
import org.xzc.msg.service.MessageType;

public final class TongquConvertUtils {
	public TongquConvertUtils() {
	}

	/**
	 * 转换从TongquService.getActDetail里返回的原始json 为 Document
	 * @return
	 */
	public static Document detailJSONToDocument(JSONObject json) {
		if (json == null)
			throw new IllegalArgumentException( "json不能为null." );
		if (json.getInt( "error" ) == 1)
			return null;

		JSONObject mi = json.getJSONObject( "main_info" );
		Document nd = new Document();

		//TODO 有些地方用的是 get 有些是opt 需要再来处理一下
		nd.append( "actid", mi.getInt( "actid" ) );
		nd.append( "type", MessageType.TONGQU );
		nd.append( "from", "同去网" );

		nd.append( "name", mi.getString( "name" ) );
		nd.append( "source", mi.getString( "source" ) );
		nd.append( "content", json.getString( "body" ) );
		nd.append( "keyword", mi.optString( "key_word" ) );

		nd.append( "startTime", mi.getString( "start_time" ) );
		nd.append( "endTime", mi.getString( "end_time" ) );
		nd.append( "signStartTime", mi.getString( "sign_start_time" ) );
		nd.append( "signEndTime", mi.getString( "sign_end_time" ) );
		nd.append( "photolink", mi.optString( "photolink" ) );
		nd.append( "location", mi.getString( "location" ) );
		nd.append( "memberCount", mi.getInt( "member_count" ) );
		nd.append( "maxMember", mi.getInt( "max_member" ) );

		return nd;
	}
}
