package org.xzc.msg.ui.activity;

import java.lang.ref.WeakReference;

import org.xzc.msg.R;
import org.xzc.msg.event.GroupByidResultEvent;
import org.xzc.msg.service.GroupService;
import org.xzc.msg.ui.fragments.GroupDetailTabsFragment;
import org.xzc.msg.utils.Callback;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 显示组的详情
 * @author xzchaoo
 *
 */
@ContentView(R.layout.fl)
public class GroupDetailActivity extends FragmentActivity {

	private static class PlaceholderFragment extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate( R.layout.placeholder_fragment, container, false );
		}
	}

	public static final String ARGS_GROUP_ID = "groupId";

	private static class MyHandler extends Handler {
		private WeakReference<GroupDetailActivity> mWeakReference;

		public MyHandler(GroupDetailActivity gda) {
			mWeakReference = new WeakReference<GroupDetailActivity>( gda );
		}

		public void handleMessage(Message msg) {
			GroupDetailActivity gda = mWeakReference.get();
			if (gda != null) {
				gda.getGroupInfo();
			} else {
				Log.i( "za", "有可能为null!" );
			}
		}
	}

	private Handler mHandler = new MyHandler( this );

	private int mGroupId;

	private void getGroupInfo() {
		GroupService.getInstance().byid( mGroupId, new Callback<GroupByidResultEvent>() {
			public void onResult(GroupByidResultEvent e) {
				if (e.success) {
					getSupportFragmentManager().beginTransaction()
							.replace( R.id.fl, new GroupDetailTabsFragment( e.group ) ).commit();
				} else {
					Toast.makeText( GroupDetailActivity.this, e.msg != null ? e.msg : "获取群组信息失败,请检查网络情况.",
							Toast.LENGTH_SHORT ).show();
					//4秒后重试
					mHandler.sendEmptyMessageDelayed( 1, 4000 );
				}
			}
		} );
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		ViewUtils.inject( this );
		getSupportFragmentManager().beginTransaction().replace( R.id.fl, new PlaceholderFragment() ).commit();
		mGroupId = getIntent().getIntExtra( ARGS_GROUP_ID, 0 );
		getGroupInfo();
	}

}
