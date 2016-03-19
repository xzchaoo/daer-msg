package org.xzc.msg.ui.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.xzc.msg.R;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.event.MessageListResultEvent;
import org.xzc.msg.params.ParamsForMessageList;
import org.xzc.msg.ui.activity.MessageDetailActivity;
import org.xzc.msg.utils.ActivityUtils;
import org.xzc.msg.utils.DateUtils;

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

/**
 * 用于显示 消息列表的 Fragment
 * @author xzchaoo
 *
 */
public class MessageListFragment extends LazyFragment implements OnItemClickListener, OnRefreshListener<ListView>,
		OnScrollListener, OnItemSelectedListener {
	public static final SimpleDateFormat SDF_MM_dd_HH_mm = new SimpleDateFormat( "yyyy-MM-dd HH" );

	public class MyMessageListAdapter extends BaseAdapter {

		public int getCount() {
			return mMessageDatas.size();
		}

		public Object getItem(int position) {
			return mMessageDatas.get( position );
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = inflater.inflate( R.layout.i_msg_list_item, parent, false );
				vh = new ViewHolder();
				ViewUtils.inject( vh, convertView );
				convertView.setTag( vh );
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			MessageForList m = mMessageDatas.get( position );
			int type = m.type;
			vh.ivLogo.setImageResource( mMsgIcon[type] );

			String name = String.format( "[%s] %s", messageTypeNames[type], m.name );
			vh.tvName.setText( name );

			vh.tvCreator.setText( m.creator.name );

			//创建于应该不用谢了vh.tvCreateTime.setText( "创建于 " + DateUtils.formatYYR( m.createTime ) );
			vh.tvCreateTime.setText( "at " + DateUtils.formatYYR( m.createTime ) );

			if (m.location == null) {
				vh.tvLocation.setVisibility( View.GONE );
			} else {
				vh.tvLocation.setText( StringUtils.abbreviate( m.location, 16 ) );
				vh.tvLocation.setVisibility( View.VISIBLE );
			}
			if (m.startTime == null) {
				vh.tvStartTime.setVisibility( View.GONE );
			} else {
				vh.tvStartTime.setVisibility( View.VISIBLE );
				vh.tvStartTime.setText( "起 " + SDF_MM_dd_HH_mm.format( m.startTime ) );
			}
			if (m.endTime == null) {
				vh.tvEndTime.setVisibility( View.GONE );
			} else {
				vh.tvEndTime.setVisibility( View.VISIBLE );
				vh.tvEndTime.setText( "止 " + SDF_MM_dd_HH_mm.format( m.endTime ) );
			}
			return convertView;
		}

	}

	private static final class ViewHolder {
		@ViewInject(R.id.iv_logo)
		public ImageView ivLogo;
		@ViewInject(R.id.tv_creator)
		public TextView tvCreator;
		@ViewInject(R.id.tv_name)
		public TextView tvName;
		@ViewInject(R.id.tv_createTime)
		public TextView tvCreateTime;
		@ViewInject(R.id.tv_startTime)
		public TextView tvStartTime;
		@ViewInject(R.id.tv_endTime)
		public TextView tvEndTime;
		@ViewInject(R.id.tv_location)
		public TextView tvLocation;
	}

	private static final int HANDLER_FLAG_REFRESH_DATA = 2;

	/**
	 * 按照哪个属性排序
	 */
	private String mBy;

	private boolean mCanLoadMore = true;

	@ResInject(id = R.array.msg_types, type = ResType.StringArray)
	private String[] messageTypeNames;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_FLAG_REFRESH_DATA:
				refreshData();
				break;
			}
		}
	};
	private String mKeyword;

	private boolean mMessageAppendMode = true;

	private List<MessageForList> mMessageDatas = new ArrayList<MessageForList>();

	@ResInject(id = R.array.msg_by_values, type = ResType.StringArray)
	private String[] mMsgByValues;

	private int[] mMsgIcon = { R.drawable.head, R.drawable.head_1, R.drawable.tongqu_logo, R.drawable.sjtu_logo };

	@ResInject(id = R.array.msg_order_values, type = ResType.IntArray)
	private int[] mMsgOrderValues;

	@ResInject(id = R.array.msg_type_values, type = ResType.IntArray)
	private int[] mMsgTypeValues;

	private MyMessageListAdapter mMyMessageListAdapter;

	/**
	 * -1降序 1升序
	 */
	private int mOrder;

	@ViewInject(R.id.ptrlv)
	private PullToRefreshListView mPullToRefreshListView;

	@ViewInject(R.id.sp_by)
	private Spinner mSPBy;

	@ViewInject(R.id.sp_order)
	private Spinner mSPOrder;

	@ViewInject(R.id.sp_type)
	private Spinner mSPType;

	@ViewInject(R.id.sv_keyword)
	private SearchView mSVKeyword;

	@ViewInject(R.id.sv_creator)
	private SearchView mSVCreator;

	/**
	 * 服务端处总的消息数量
	 */
	private int mTotal = 0;

	/**
	 * 当前要显示的消息的类型
	 */
	private int mType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setHasOptionsMenu( mShowOptionsMenu);
	}

	/**
	 * 加载 MessageList 页面特有的菜单
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate( R.menu.m_msg_list_fragment, menu );
		super.onCreateOptionsMenu( menu, inflater );
	}

	public void onMessageListResult(MessageListResultEvent e) {
		mPullToRefreshListView.onRefreshComplete();
		if (e.success) {
			if (!mMessageAppendMode)
				mMessageDatas.clear();
			mMessageDatas.addAll( e.messages );
			mTotal = e.total;
			mCanLoadMore = mMessageDatas.size() < mTotal;
			mMyMessageListAdapter.notifyDataSetChanged();
		} else {
			//获取数据失败
			Toast.makeText( getActivity(), "从服务端获取数据失败,请检查网络情况.", Toast.LENGTH_SHORT ).show();
		}
	}

	/**
	 * 当每个项被点击的时候 打开详情界面
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//不知道为何差1
		MessageForList m = mMessageDatas.get( position - 1 );
		//ActivityUtils.toMessageDetailActivity( getActivity(), m.id, m.type );
		//这里是fragment 好像行为有点不一样?
		Intent i = new Intent( getActivity(), MessageDetailActivity.class );
		i.putExtra( MessageDetailActivity.ARGS_MSG_ID, m.id );
		i.putExtra( MessageDetailActivity.ARGS_MSG_TYPE, m.type );
		startActivity( i );
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		boolean shouldRefresh = false;
		if (mSPType == parent) {
			int newType = mMsgTypeValues[position];
			if (mType != newType) {
				mType = newType;
				shouldRefresh = true;
			}
		} else if (mSPBy == parent) {
			String newBy = mMsgByValues[position];
			if (mBy != newBy) {
				mBy = newBy;
				shouldRefresh = true;
			}
		} else if (mSPOrder == parent) {
			int newOrder = mMsgOrderValues[position];
			if (mOrder != newOrder) {
				mOrder = newOrder;
				shouldRefresh = true;
			}
		}

		if (shouldRefresh) {
			refreshData();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.xiaoxi_add:
			//发布新消息
			ActivityUtils.toPublishMessageActivity( getActivity() );
			return true;
		case R.id.xiaoxi_refresh:
			mPullToRefreshListView.setRefreshing();
			refreshData();
			return true;
		}
		return super.onOptionsItemSelected( item );
	}

	public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
		refreshData();
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (!mPullToRefreshListView.isRefreshing() && mCanLoadMore
				&& firstVisibleItem + visibleItemCount >= totalItemCount) {
			mCanLoadMore = false;
			mMessageAppendMode = true;
			loadMore();
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.list_total_msg_1 );
		ViewUtils.inject( this, getContentView() );

		mSPType.setOnItemSelectedListener( this );
		mSPBy.setOnItemSelectedListener( this );
		mSPOrder.setOnItemSelectedListener( this );

		mMyMessageListAdapter = new MyMessageListAdapter();
		mPullToRefreshListView.setAdapter( mMyMessageListAdapter );
		mPullToRefreshListView.setOnItemClickListener( this );
		mPullToRefreshListView.setOnRefreshListener( this );
		mPullToRefreshListView.setOnScrollListener( this );

		mSVKeyword.setOnQueryTextListener( new OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				mKeyword = newText;
				sendRefreshDataRequeset();
				return true;
			}

			public boolean onQueryTextSubmit(String query) {
				return true;
			}
		} );
		mSVCreator.setOnQueryTextListener( new OnQueryTextListener() {
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			public boolean onQueryTextChange(String newText) {
				mCreator = newText;
				sendRefreshDataRequeset();
				return true;
			}
		} );

		mType = mMsgTypeValues[0];
		mBy = mMsgByValues[0];
		mOrder = mMsgOrderValues[0];

		if (!mShowCreator)
			mSVCreator.setVisibility( View.GONE );
		if (!mShowKeyword)
			mSVKeyword.setVisibility( View.GONE );
		if (!mShowType)
			mSPType.setVisibility( View.GONE );

		//loadMore();
	}

	/**
	 * 按照创建者搜索
	 */
	private String mCreator;

	/**
	 * 异步加载更多
	 */
	private void loadMore() {
		ParamsForMessageList p = new ParamsForMessageList();
		p.type = mType;
		p.offset = mMessageAppendMode ? mMessageDatas.size() : 0;
		p.size = 10;
		p.by = mBy;
		p.order = mOrder;
		p.creator = mCreator;
		p.keyword = mKeyword;
		mHelper.loadMore( p );
		/*MessageService.getInstance().list( p, new Callback<MessageListResultEvent>() {
			public void onResult(MessageListResultEvent e) {
				onEvent( e );
			}
		} );*/
	}

	/**
	 * 刷新数据 删掉所有数据 然后重新从服务端抓取数据
	 */
	private void refreshData() {
		mMessageAppendMode = false;
		loadMore();
	}

	private void sendRefreshDataRequeset() {
		mHandler.removeMessages( HANDLER_FLAG_REFRESH_DATA );
		mHandler.sendEmptyMessageDelayed( HANDLER_FLAG_REFRESH_DATA, 1000 );
	}

	@Override
	protected void onFragmentStartLazy() {
		getActivity().setTitle( "消息列表" );
	}

	public interface IMessageListFragmentHelper {
		public void loadMore(ParamsForMessageList p);
	}

	private IMessageListFragmentHelper mHelper;

	private boolean mShowCreator = true;

	private boolean mShowKeyword = true;

	private boolean mShowType = true;

	private boolean mShowOptionsMenu=true;

	public void setHelper(IMessageListFragmentHelper helper) {
		mHelper = helper;
	}

	public void setShowCreator(boolean b) {
		mShowCreator = b;
		if (mSVCreator != null) {
			mSVCreator.setVisibility( b ? View.VISIBLE : View.GONE );
		}
	}

	public void setShowKeyword(boolean b) {
		mShowKeyword = b;
		if (mSVKeyword != null) {
			mSVKeyword.setVisibility( b ? View.VISIBLE : View.GONE );
		}
	}

	public void setShowType(boolean b) {
		mShowType = b;
		if (mSPType != null) {
			mSPType.setVisibility( b ? View.VISIBLE : View.GONE );
		}
	}

	public void setShowOptionsMenu(boolean b) {
		mShowOptionsMenu = b;
	}

}
