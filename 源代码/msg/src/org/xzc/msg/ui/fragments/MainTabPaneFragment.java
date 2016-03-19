package org.xzc.msg.ui.fragments;

import org.xzc.msg.R;
import org.xzc.msg.event.MessageListResultEvent;
import org.xzc.msg.params.ParamsForMessageList;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.service.UserMessageService;
import org.xzc.msg.ui.fragments.MessageListFragment.IMessageListFragmentHelper;
import org.xzc.msg.utils.Callback;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar.Gravity;

/**
 * 主面板 
 * @author xzchaoo
 *
 */
public class MainTabPaneFragment extends LazyFragment {
	
	private static Fragment getMessageListFragment() {
		final MessageListFragment f = new MessageListFragment();
		f.setHelper( new IMessageListFragmentHelper() {
			public void loadMore(ParamsForMessageList p) {
				MessageService.getInstance().list( p, new Callback<MessageListResultEvent>() {
					public void onResult(MessageListResultEvent e) {
						f.onMessageListResult( e );
					}
				} );
			}
		} );
		return f;
	}
	private static Fragment getUserMessageListFragment() {
		final MessageListFragment f = new MessageListFragment();
		f.setShowOptionsMenu(false);
		f.setShowCreator(false);
		f.setShowKeyword(false);
		f.setShowType(false);
		f.setHelper( new IMessageListFragmentHelper() {
			public void loadMore(ParamsForMessageList p) {
				UserMessageService.getInstance().list( p, new Callback<MessageListResultEvent>() {
					public void onResult(MessageListResultEvent e) {
						f.onMessageListResult( e );
					}
				} );
			}
		} );
		return f;
	}
	
	private class MyIndicatorPagerAdapter extends IndicatorFragmentPagerAdapter {

		private int[] tabIcons = { R.drawable.ic_message_black_36dp, R.drawable.ic_shuffle_black_36dp,
				R.drawable.ic_settings_applications_black_36dp };
		private String[] tabNames = { "消息市场", "群组", "我的消息" };

		public MyIndicatorPagerAdapter(FragmentManager fragmentManager) {
			super( fragmentManager );
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			switch (position) {
			case 0:
				return getMessageListFragment();
			case 1:
				return new GroupListFragment();
			case 2:
				return getUserMessageListFragment();
			}
			return null;
		}

		@Override
		public View getViewForTab(int position, View convertView, ViewGroup container) {
			if (convertView == null) {
				convertView = inflater.inflate( R.layout.i_tab_1, container, false );
			} else {
			}
			TextView tv = (TextView) convertView;
			tv.setCompoundDrawablesWithIntrinsicBounds( 0, tabIcons[position], 0, 0 );
			tv.setText( tabNames[position] );
			return convertView;
		}

	}

	private FixedIndicatorView mFixedIndicatorView;

	private IndicatorViewPager mIndicatorViewPager;

	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setHasOptionsMenu( true );
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.i( "za", "ListMsgFragment onCreateOptionsMenu" );
		super.onCreateOptionsMenu( menu, inflater );
	}

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_list_msg_1 );

		mViewPager = (ViewPager) findViewById( R.id.vp );

		//这个函数是作者额外加的 进制viewpager滑动
		mViewPager.setCanScroll( true );
		//设置保留在内存里的页面数量
		mViewPager.setOffscreenPageLimit( 4 );
		//设置预加载数量为0 即不预加载 但是会缓存4个page
		mViewPager.setPrepareNumber( 0 );

		mFixedIndicatorView = (FixedIndicatorView) findViewById( R.id.fixed_indivator );

		mFixedIndicatorView.setScrollBar( new ColorBar( getActivity(), Color.RED, 5, Gravity.TOP_FLOAT ) );

		mIndicatorViewPager = new IndicatorViewPager( mFixedIndicatorView, mViewPager );
		mIndicatorViewPager.setAdapter( new MyIndicatorPagerAdapter( getFragmentManager() ) );
		
	}

}