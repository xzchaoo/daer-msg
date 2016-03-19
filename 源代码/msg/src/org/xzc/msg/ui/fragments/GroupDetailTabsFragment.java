package org.xzc.msg.ui.fragments;

import org.xzc.msg.R;
import org.xzc.msg.domain.Group;
import org.xzc.msg.domain.GroupForByid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar.Gravity;

public class GroupDetailTabsFragment extends Fragment {

	public interface IGroupProvider {
		void setGroup(GroupForByid g);
		GroupForByid getGroup();
	}

	private IGroupProvider mGroupProvider = new IGroupProvider() {
		public void setGroup(GroupForByid g) {
			mGroup = g;
		}

		public GroupForByid getGroup() {
			return mGroup;
		}
	};

	private class MyIndicatorPagerAdapter extends IndicatorFragmentPagerAdapter {

		private LayoutInflater inflater;

		public MyIndicatorPagerAdapter(LayoutInflater inflater, FragmentManager fragmentManager) {
			super( fragmentManager );
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			return TAB_NAMES.length;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			Fragment f = null;
			if (position == 0) {
				f = new GroupDetailFragment1( mGroupProvider );
			} else {
				f = new GroupMemberListFragment( mGroupProvider );
			}
			return f;
		}

		@Override
		public View getViewForTab(int position, View convertView, ViewGroup container) {
			if (convertView == null) {
				convertView = inflater.inflate( R.layout.i_tab_1, container, false );
			}
			TextView tv = (TextView) convertView;
			tv.setText( TAB_NAMES[position] );
			return convertView;
		}
	}

	//private static final String[] TAB_NAMES = { "介绍", "成员", "消息" };
	private static final String[] TAB_NAMES = { "介绍" };

	private GroupForByid mGroup;

	@ViewInject(R.id.indicator)
	private FixedIndicatorView mIndicator;

	private IndicatorViewPager mIndicatorViewPager;

	@ViewInject(R.id.vp)
	private ViewPager mVP;

	private MyIndicatorPagerAdapter pagerAdapter;

	public GroupDetailTabsFragment(GroupForByid group) {
		mGroupProvider.setGroup( group );
		//this.mGroup = group;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//return super.onCreateView( inflater, container, savedInstanceState );
		View contentView = inflater.inflate( R.layout.f_group_detail_tabs, container, false );
		ViewUtils.inject( this, contentView );
		//这个函数是作者额外加的 进制viewpager滑动
		mVP.setCanScroll( true );
		//设置保留在内存里的页面数量
		mVP.setOffscreenPageLimit( 1 );
		//设置预加载数量为0 即不预加载 但是会缓存4个page
		mVP.setPrepareNumber(1 );

		mIndicator.setScrollBar( new ColorBar( getActivity(), Color.RED, 5, Gravity.TOP_FLOAT ) );

		mIndicatorViewPager = new IndicatorViewPager( mIndicator, mVP );
		pagerAdapter = new MyIndicatorPagerAdapter( inflater, getFragmentManager() );
		mIndicatorViewPager.setAdapter( pagerAdapter );
		return contentView;
	}

}
