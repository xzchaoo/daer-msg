package org.xzc.msg.ui.activity;

import org.xzc.msg.R;
import org.xzc.msg.ui.fragments.UserDetailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 查看一个用户的详情
 * @author xzchaoo
 *
 */
@ContentView(R.layout.activity_user_detail)
public class UserDetailActivity extends FragmentActivity {
	public static final String ARGS_USER_ID = "user.id";
	public static final String ARGS_USER_NAME = "user.name";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getActionBar().setDisplayHomeAsUpEnabled( true );
		ViewUtils.inject( this );
		Intent intent = getIntent();
		int id = intent.getIntExtra( ARGS_USER_ID, 0 );
		Bundle args = new Bundle();
		Fragment f = new UserDetailFragment();
		if (id != 0) {
			args.putInt( ARGS_USER_ID, id );
		} else {
			String name = intent.getStringExtra( ARGS_USER_NAME );
			if (name != null) {
				args.putString( ARGS_USER_NAME, name );
			} else {
				finish();
				return;
			}
		}
		f.setArguments( args );
		getSupportFragmentManager().beginTransaction().replace( R.id.fl, f ).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected( item );
	}
}
