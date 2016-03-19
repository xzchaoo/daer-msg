package org.xzc.msg.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xzc.msg.app.MsgApplication;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.domain.SimpleMessageForPublish;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.event.MessageByIdResultEvent;
import org.xzc.msg.event.MessageListResultEvent;
import org.xzc.msg.event.MessagePublishResultEvent;
import org.xzc.msg.http.MyJsonHandler;
import org.xzc.msg.params.ParamsForMessageList;
import org.xzc.msg.utils.API;
import org.xzc.msg.utils.Callback;
import org.xzc.msg.utils.GsonUtils;
import org.xzc.msg.utils.MessageUtils;
import org.xzc.msg.utils.MyHttpUtils;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class MessageService {
	private static MessageService sMessageService;

	public static MessageService getInstance() {
		if (sMessageService == null) {
			synchronized (MessageService.class) {
				if (sMessageService == null) {
					sMessageService = new MessageService();
				}
			}
		}
		return sMessageService;
	}

	private AsyncHttpClient ahc;

	public MessageService() {
		ahc = MsgApplication.ahc;
	}

	public void byid(final int id, Callback<MessageByIdResultEvent> cb) {
		final MessageByIdResultEvent e = new MessageByIdResultEvent();
		RequestParams rp = new RequestParams();
		rp.put( "id", id );
		ahc.get( API.MSG_BYID_URL, rp, new MyJsonHandler( e, cb ) {
			public void onSuccess(JSONObject json) throws JSONException {
				Message msg = MessageUtils.toMessage( json.getJSONObject( "msg" ) );
				UserForSimpleInfo creator = GsonUtils.fromJson( json.getJSONObject( "creator" ),
						UserForSimpleInfo.class );
				e.message = msg;
				e.creator = creator;
			}
		} );
	}

	public void list(ParamsForMessageList p, final Callback<MessageListResultEvent> cb) {
		final MessageListResultEvent e = new MessageListResultEvent();
		RequestParams rp = new RequestParams();
		rp.put( "type", p.type );
		rp.put( "offset", p.offset );
		rp.put( "size", p.size );
		rp.put( "by", p.by );
		rp.put( "order", p.order );
		rp.put( "keyword", p.keyword );
		rp.put( "creator", p.creator );
		Log.i( "za", "order=" + p.order );
		ahc.get( API.MSG_LIST_URL, rp, new MyJsonHandler( e, cb ) {
			public void onSuccess(JSONObject json) throws JSONException {
				JSONArray list = json.getJSONArray( "list" );
				List<MessageForList> messageList = GsonUtils.fromJson( list, new TypeToken<List<MessageForList>>() {
				}.getType() );
				e.messages = messageList;
				e.total = json.getInt( "total" );
			}
		} );
	}

	/**
	 * 提交post请求到服务端
	 * @param m
	 */
	public void publishMessage(SimpleMessageForPublish m, Callback<MessagePublishResultEvent> cb) {
		final MessagePublishResultEvent e = new MessagePublishResultEvent();
		e.type = 1;
		RequestParams rp = new RequestParams();
		rp.put( "type", 1 );
		rp.put( "groupId", m.groupId );
		rp.put( "name", m.name );
		rp.add( "content", m.content );
		rp.add( "location", m.location );
		rp.add( "startTime", MyHttpUtils.formatToServer( m.startTime ) );
		rp.add( "endTime", MyHttpUtils.formatToServer( m.endTime ) );
		ahc.post( API.MSG_ADD_URL, rp, new MyJsonHandler( e, cb ) {
			public void onSuccess(JSONObject json) throws JSONException {
				e.id = json.getInt( "id" );
			}
		} );
	}
}
