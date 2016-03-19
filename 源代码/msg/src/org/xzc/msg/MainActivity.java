package org.xzc.msg;

import java.util.Arrays;
import java.util.List;

import org.xzc.msg.service.UserService;
import org.xzc.msg.ui.activity.HelpActivity;
import org.xzc.msg.ui.fragments.MainTabPaneFragment;
import org.xzc.msg.utils.ActivityUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 主Activity 里面放了 侧滑菜单 + tab面板
 * @author xzchaoo
 *
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends FragmentActivity {
	private boolean mFinishNextBack = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mFinishNextBack = false;
		}
	};
	private SlidingMenu mMenu;

	private ArrayAdapter<String> mMenuListAdapter;

	private ListView mMenuListView;

	@Override
	public void onBackPressed() {
		if (mFinishNextBack)
			super.onBackPressed();
		else {
			Toast.makeText( this, "再点击一次后退,退出该程序.", Toast.LENGTH_SHORT ).show();
			mFinishNextBack = true;
			mHandler.sendEmptyMessageDelayed( 0, 2000 );
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );

		if (!UserService.getInstance().isLogin()) {
			ActivityUtils.toLoginActivity( this );
			finish();
			return;
		}

		ViewUtils.inject( this );

		setUpSlidingMenu();

		setUpLeftMenuListView();

		onLeftMenuItemSelected( 0 );

	}

	private void onLeftMenuItemSelected(int index) {
		switch (index) {
		case 0:
			FragmentManager fm = getSupportFragmentManager();
			if (fm.findFragmentById( R.id.fl ) == null)
				fm.beginTransaction().replace( R.id.fl, new MainTabPaneFragment() ).commit();
			break;
		case 1:
			ActivityUtils.toUserInfoUpdateActivity( this );
			break;
		case 2:
			UserService.getInstance().logout();
			Toast.makeText( MainActivity.this, "注销成功", Toast.LENGTH_SHORT ).show();
			ActivityUtils.toLoginActivity( this );
			finish();
			break;
		case 3:
			ActivityUtils.toPreferencesSettingActivity( this );
			break;
		case 4:
			this.startActivity( new Intent( this, HelpActivity.class ) );
			break;
		}
	}

	private static final List<String> mLeftListViewItems = Arrays.asList( "主界面", "个人中心", "注销", "设置", "帮助" );

	/**
	 * 建立左边的listview
	 */
	private void setUpLeftMenuListView() {
		mMenuListView = (ListView) mMenu.getMenu().findViewById( R.id.lv );
		mMenuListAdapter = new ArrayAdapter<String>( this, R.layout.i_left_menu_item, mLeftListViewItems );

		mMenuListView.setAdapter( mMenuListAdapter );
		mMenuListView.setOnItemClickListener( new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mMenu.showContent();
				onLeftMenuItemSelected( position );
			}
		} );
	}

	private void setUpSlidingMenu() {
		mMenu = new SlidingMenu( this );
		mMenu.setMode( SlidingMenu.LEFT );
		//mMenu.setTouchModeAbove( SlidingMenu.TOUCHMODE_FULLSCREEN );
		mMenu.setTouchModeAbove( SlidingMenu.TOUCHMODE_MARGIN );
		mMenu.setBehindWidth( 400 );
		mMenu.setShadowWidth( 16 );
		mMenu.setShadowDrawable( R.drawable.left_menu_item_shadow_1 );
		mMenu.setMenu( R.layout.left_menu );
		//mMenu.attachToActivity( this, SlidingMenu.SLIDING_WINDOW | SlidingMenu.SLIDING_CONTENT );
		mMenu.attachToActivity( this, SlidingMenu.SLIDING_WINDOW );
	}

}
