package org.xzc.msg.ui.fragments;

import java.util.List;

import org.xzc.msg.R;
import org.xzc.msg.domain.GroupForByid;
import org.xzc.msg.domain.IdAndName;
import org.xzc.msg.event.GroupByidResultEvent;
import org.xzc.msg.ui.fragments.GroupDetailTabsFragment.IGroupProvider;
import org.xzc.msg.utils.ActivityUtils;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shizhefei.fragment.LazyFragment;

/**
 * 列出组的成员
 * @author xzchaoo
 *
 */
public class GroupMemberListFragment extends LazyFragment {

	private class MyGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMembers.size();
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
			ViewHolder vh = null;
			if (convertView == null) {
				convertView = inflater.inflate( R.layout.i_group_user, parent, false );
				vh = new ViewHolder();
				vh.ivHead = (ImageView) convertView.findViewById( R.id.iv_head );
				vh.tvName = (TextView) convertView.findViewById( R.id.tv_name );
				convertView.setTag( vh );
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			IdAndName u = mMembers.get( position );
			vh.tvName.setText( u.name );
			return convertView;
		}

	}

	private static final class ViewHolder {
		ImageView ivHead;
		TextView tvName;
	}

	private GroupForByid mGroup;

	@ViewInject(R.id.gv)
	private GridView mGV;
	private List<IdAndName> mMembers;

	private MyGridViewAdapter mMyGridViewAdapter;

	public GroupMemberListFragment(IGroupProvider gp) {
		this.mGroup = gp.getGroup();
		mMembers = mGroup.members;
	}

	public void onEvent(GroupByidResultEvent e) {
		if (e.success) {
			mMembers.clear();
			mMembers.addAll( e.group.members );
			mMyGridViewAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_group_member_list );
		ViewUtils.inject( this, getContentView() );

		mMyGridViewAdapter = new MyGridViewAdapter();
		mGV.setAdapter( mMyGridViewAdapter );
		mGV.setOnItemClickListener( new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				IdAndName u = mMembers.get( position );
				ActivityUtils.toUserDetail( getActivity(), u.id );
			}
		} );

	}

}
