package org.xzc.msg.ui.activity;

import org.xzc.msg.R;
import org.xzc.msg.ui.fragments.GroupListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 目前只用于选择出某个用户创建的组
 * @author xzchaoo
 *
 */
@ContentView(R.layout.fl)
public class GroupListActivity extends FragmentActivity {
	public static final String ACTION_PICK = "action_pick";
	public static final String EXTRA_CREATOR_ID = "creatorId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getActionBar().setDisplayHomeAsUpEnabled( true );
		setTitle( "选择群组" );
		ViewUtils.inject( this );
		int creatorId = getIntent().getIntExtra( EXTRA_CREATOR_ID, 0 );
		if (creatorId == 0) {
			finish();
			return;
		}
		GroupListFragment f = new GroupListFragment( true, creatorId );
		getSupportFragmentManager().beginTransaction().replace( R.id.fl, f ).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			setResult( RESULT_CANCELED );
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected( item );
		}
	}
}
