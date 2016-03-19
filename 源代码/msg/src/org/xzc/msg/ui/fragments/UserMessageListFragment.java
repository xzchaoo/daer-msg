package org.xzc.msg.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import org.xzc.msg.R;
import org.xzc.msg.domain.UserMessage;
import org.xzc.msg.event.MessageListResultEvent;
import org.xzc.msg.params.ParamsForMessageList;
import org.xzc.msg.result.UserMessageListResult;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.service.UserMessageService;
import org.xzc.msg.ui.fragments.MessageListFragment.IMessageListFragmentHelper;
import org.xzc.msg.ui.fragments.MessageListFragment.MyMessageListAdapter;
import org.xzc.msg.utils.Callback;

import android.os.Bundle;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shizhefei.fragment.LazyFragment;

public class UserMessageListFragment extends LazyFragment {

	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_user_message_list );
		ViewUtils.inject( this, getContentView() );
	}

	private List<UserMessage> userMessageList = new ArrayList<UserMessage>();

	private void loadMore() {
		ParamsForMessageList p = new ParamsForMessageList();
		p.offset = userMessageList.size();
		p.size = 10;
		UserMessageService.getInstance().list( p, new Callback<MessageListResultEvent>() {
			public void onResult(MessageListResultEvent e) {
				//Toast.makeText( getActivity(), "结果" + e.messages, Toast.LENGTH_SHORT ).show();
			}
		} );
	}
}
