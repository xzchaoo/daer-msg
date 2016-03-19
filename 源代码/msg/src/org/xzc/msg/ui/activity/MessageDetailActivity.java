package org.xzc.msg.ui.activity;

import org.xzc.msg.R;
import org.xzc.msg.ui.fragments.SJTUJWCMessageDetailFragment;
import org.xzc.msg.ui.fragments.SimpleMessageDetailFragment;
import org.xzc.msg.ui.fragments.TongquMessageDetailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_message_detail)
public class MessageDetailActivity extends FragmentActivity {
	public static final String ARGS_MSG_ID = "msg.id";
	public static final String ARGS_MSG_TYPE = "msg.type";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		Intent i = getIntent();
		int msgId = i.getIntExtra( ARGS_MSG_ID, 0 );
		int type = i.getIntExtra( ARGS_MSG_TYPE, 0 );
		if (msgId == 0 || type == 0) {
			Log.i( "MessageDetailActivity", "无效参数 id=" + msgId + " type=" + type );
			finish();
			return;
		} else {
			getActionBar().setDisplayHomeAsUpEnabled( true );
			ViewUtils.inject( this );
			Bundle args = new Bundle();
			args.putInt( ARGS_MSG_ID, msgId );
			args.putInt( ARGS_MSG_TYPE, type );

			Fragment f = getFragment( type );

			f.setArguments( args );
			getSupportFragmentManager().beginTransaction().replace( R.id.fl, f ).commit();
		}
	}

	private Fragment getFragment(int type) {
		switch (type) {
		case 1:
			return new SimpleMessageDetailFragment();
		case 2:
			return new TongquMessageDetailFragment();
		default:
			return new SJTUJWCMessageDetailFragment();
		}
	}
}
