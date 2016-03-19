package org.xzc.msg.service;

import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;
import org.xzc.msg.app.MsgApplication;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.domain.Page;
import org.xzc.msg.event.MessageListResultEvent;
import org.xzc.msg.http.MyJsonHandler;
import org.xzc.msg.params.ParamsForMessageList;
import org.xzc.msg.utils.API;
import org.xzc.msg.utils.Callback;
import org.xzc.msg.utils.GsonUtils;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class UserMessageService {
	private static UserMessageService sUserMessageListFragment = new UserMessageService();
	public static UserMessageService getInstance() {
		return sUserMessageListFragment;
	}

	private AsyncHttpClient ahc;

	public UserMessageService() {
		ahc = MsgApplication.ahc;
	}

	public void list(ParamsForMessageList p, final Callback<MessageListResultEvent> cb) {
		final MessageListResultEvent e= new MessageListResultEvent();
		RequestParams rp = new RequestParams();
		rp.put( "offset", p.offset );
		rp.put( "size", p.size );
		rp.put( "by", p.by);
		rp.put( "order", p.order);
		ahc.get( API.USER_MESSAGE_LIST_URL, rp, new MyJsonHandler(e,cb) {
			public void onSuccess(JSONObject json) throws JSONException {
				Page<MessageForList> p = GsonUtils.fromJson( json, new TypeToken<Page<MessageForList>>() {
				}.getType() );
				e.messages = p.list;
			}
		} );
	}
}
