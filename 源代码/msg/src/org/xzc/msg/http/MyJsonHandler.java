package org.xzc.msg.http;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xzc.msg.result.ResultBase;
import org.xzc.msg.utils.Callback;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 使用者只需要重载 新的 onSuccess 和 onFailure
 * @author xzchaoo
 *
 */
public class MyJsonHandler extends JsonHttpResponseHandler {

	private boolean mIsArray = false;

	private ResultBase rb;
	private Callback cb;

	public MyJsonHandler(ResultBase rb, Callback cb) {
		this.rb = rb;
		this.cb = cb;
	}

	public MyJsonHandler(ResultBase rb, Callback cb, boolean isArray) {
		this.rb = rb;
		this.cb = cb;
		mIsArray = isArray;
	}

	@Override
	public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
		onFailure( statusCode, -1, "网络异常." );
	}

	@Override
	public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		Log.i( "za", "raw=" + responseString );
		onFailure( statusCode, -1, "网络异常." );
	}

	@Override
	public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
		onFailure( statusCode, -1, "网络异常." );
	}

	@Override
	public final void onSuccess(int statusCode, Header[] headers, JSONObject json) {
		try {
			int code = json.getInt( "code" );
			String msg = json.getString( "msg" );
			rb.msg = msg;
			if (code == 0||code==4) {
				rb.success = true;
				if (mIsArray)
					onSuccess( json.optJSONArray( "result" ) );
				else
					onSuccess( json.optJSONObject( "result" ) );
				cb.onResult( rb );
			} else {
				rb.success = false;
				onFailure( statusCode, code, msg );
			}
		} catch (JSONException e) {
			onFailure( statusCode, -1, "无效的json数据." );
		}
	}

	public void onSuccess(JSONArray json) throws JSONException {
	}

	public void onSuccess(JSONObject json) throws JSONException {
	}

	public void onFailure(int statusCode, int code, String msg) {
		rb.success = false;
		if (rb.msg == null)
			rb.msg = msg;
		cb.onResult( rb );
	}

}
