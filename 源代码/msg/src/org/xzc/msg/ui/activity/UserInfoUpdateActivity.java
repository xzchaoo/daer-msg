package org.xzc.msg.ui.activity;

import org.xzc.msg.R;
import org.xzc.msg.ui.fragments.UserInfoUpdateFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 查看和修改当前用户
 * @author xzchaoo
 *
 */
@ContentView(R.layout.fl)
public class UserInfoUpdateActivity extends FragmentActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		ViewUtils.inject( this );
		getSupportFragmentManager().beginTransaction().replace( R.id.fl, new UserInfoUpdateFragment() ).commit();
	}
}
