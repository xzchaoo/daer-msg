package org.xzc.msg.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import org.xzc.msg.R;
import org.xzc.msg.domain.GroupForList;
import org.xzc.msg.event.GroupListResultEvent;
import org.xzc.msg.params.ParamsForGroupList;
import org.xzc.msg.service.GroupService;
import org.xzc.msg.service.UserService;
import org.xzc.msg.utils.ActivityUtils;
import org.xzc.msg.utils.Callback;
import org.xzc.msg.utils.DateUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shizhefei.fragment.LazyFragment;

public class GroupListFragment extends LazyFragment {

	private class MyGroupListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mGroups.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = inflater.inflate( R.layout.i_group_list_item, parent, false );
				vh = new ViewHolder();
				ViewUtils.inject( vh, convertView );
				convertView.setTag( vh );
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			GroupForList g = mGroups.get( position );
			vh.tvName.setText( g.name );
			vh.tvDesc.setText( g.desc );
			vh.tvCreator.setText( "by " + g.creator.name );
			vh.tvCreateTime.setText( "创建于 " + DateUtils.format( g.createTime ) );
			return convertView;
		}

	}

	private static final class ViewHolder {
		@ViewInject(R.id.iv_logo)
		ImageView ivLogo;
		@ViewInject(R.id.tv_createTime)
		TextView tvCreateTime;
		@ViewInject(R.id.tv_creator)
		TextView tvCreator;
		@ViewInject(R.id.tv_desc)
		TextView tvDesc;
		@ViewInject(R.id.tv_name)
		TextView tvName;
	}

	public static final int HANDLER_FLAG_REFRESH_COMPLETE = 2;
	public static final int HANDLER_FLAG_REFRESH_DATA = 1;

	private MyGroupListAdapter mAdapter;

	private String mBy;

	private boolean mCanLoadMore = false;

	@ViewInject(R.id.cb_my_create_group)
	private CheckBox mCBMyCreateGgoup;

	private int mCreatorId;
	/**
	 * 是否是追加模式 用于区分 刷新数据 和 加载更多数据
	 */
	private boolean mGroupAppendMode = true;

	@ResInject(id = R.array.msg_by_values, type = ResType.StringArray)
	private String[] mGroupByValues;

	@ResInject(id = R.array.msg_order_values, type = ResType.IntArray)
	private int[] mGroupOrderValues;

	private List<GroupForList> mGroups = new ArrayList<GroupForList>();

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_FLAG_REFRESH_DATA:
				refreshData();
				break;
			case HANDLER_FLAG_REFRESH_COMPLETE:
				mPTRLVGroups.onRefreshComplete();
				break;
			}
		}
	};

	private int mOldCreatorId;
	private int mOrder;

	private boolean mPickMode;
	@ViewInject(R.id.ptrlv)
	private PullToRefreshListView mPTRLVGroups;

	@ViewInject(R.id.sp_by)
	private Spinner mSPBy;

	@ViewInject(R.id.sp_order)
	private Spinner mSPOrder;

	@ViewInject(R.id.sv_keyword)
	private SearchView mSVKeyword;

	public GroupListFragment() {
		mPickMode = false;
		mCreatorId = mOldCreatorId = 0;
	}

	public GroupListFragment(boolean pickMode, int creatorId) {
		this.mPickMode = pickMode;
		this.mCreatorId = mOldCreatorId = creatorId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setHasOptionsMenu( true );
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate( R.menu.group_fragment, menu );
		super.onCreateOptionsMenu( menu, inflater );
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			mPTRLVGroups.setRefreshing();
			refreshData();
			break;
		case R.id.add:
			ActivityUtils.toGroupAddActivity( getActivity() );
			break;
		}
		return super.onOptionsItemSelected( item );
	}

	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_group_list );
		ViewUtils.inject( this, getContentView() );
		mAdapter = new MyGroupListAdapter();
		mPTRLVGroups.setAdapter( mAdapter );

		//加载更多
		mPTRLVGroups.setOnScrollListener( new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (mCanLoadMore && !mPTRLVGroups.isRefreshing()
						&& firstVisibleItem + visibleItemCount >= totalItemCount) {
					mCanLoadMore = false;
					mGroupAppendMode = true;
					loadMore();
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		} );

		//item点击 就打开 group 详情
		mPTRLVGroups.setOnItemClickListener( new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//好像是因为使用了pull to refresh list view 导致position比普通的索引大1
				--position;
				GroupForList g = mGroups.get( position );
				if (mPickMode) {
					Intent data = new Intent();
					data.putExtra( "groupId", g.id );
					data.putExtra( "groupName", g.name );
					getActivity().setResult( Activity.RESULT_OK, data );
					getActivity().finish();
				} else {
					ActivityUtils.toGroupDetailActivity( getActivity(), g.id );
				}
			}
		} );

		//下拉刷新
		mPTRLVGroups.setOnRefreshListener( new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshData();
			}
		} );

		//关键字变化
		mSVKeyword.setOnQueryTextListener( new OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				sendRefreshDataRequest();
				return true;
			}

			public boolean onQueryTextSubmit(String query) {
				return true;
			}
		} );
		//by变化
		mSPBy.setOnItemSelectedListener( new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mBy = mGroupByValues[position];
				refreshData();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		} );
		//order变化
		mSPOrder.setOnItemSelectedListener( new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mOrder = mGroupOrderValues[position];
				refreshData();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}

		} );
		// "只显示我创建的组" 变化
		mCBMyCreateGgoup.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mCreatorId = UserService.getInstance().getCurrentUserLocally().id;
				} else {
					mCreatorId = mOldCreatorId;
				}
				refreshData();
			}
		} );
		mOrder = mGroupOrderValues[0];
		mBy = mGroupByValues[0];
	}

	private void loadMore() {
		ParamsForGroupList p = new ParamsForGroupList();
		p.creatorId = mCreatorId;
		p.offset = mGroupAppendMode ? mGroups.size() : 0;
		p.size = 10;
		p.by = mBy;
		p.order = mOrder;
		p.keyword = mSVKeyword.getQuery().toString();
		GroupService.getInstance().list( p, new Callback<GroupListResultEvent>() {
			public void onResult(GroupListResultEvent e) {
				mHandler.sendEmptyMessageDelayed( HANDLER_FLAG_REFRESH_COMPLETE, 400 );
				if (e.success) {
					if (!mGroupAppendMode)
						mGroups.clear();
					mGroups.addAll( e.groups );
					mCanLoadMore = mGroups.size() < e.total;
					mAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText( getActivity(), "获取数据失败,请检查网络情况.", Toast.LENGTH_SHORT ).show();
				}
			}
		} );
	}

	private void refreshData() {
		mCanLoadMore = false;
		mGroupAppendMode = false;
		loadMore();
	}

	private void sendRefreshDataRequest() {
		mHandler.removeMessages( HANDLER_FLAG_REFRESH_DATA );
		mHandler.sendEmptyMessageDelayed( HANDLER_FLAG_REFRESH_DATA, 500 );
	}
}
