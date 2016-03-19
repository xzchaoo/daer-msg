package org.xzc.msg.service.callback;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.TongquMessage;
import org.xzc.msg.fetch.TongquFetchService;
import org.xzc.msg.service.TongquMessageService;

@Component
public class TongquServiceCallback implements IServiceCallback {
	private static final Log LOG = LogFactory.getLog( TongquServiceCallback.class );

	@Resource
	private TongquMessageService tongquMessageService;

	@Resource
	private TongquFetchService tongquService;

	public Message beforeAddCallback(Message msg) {
		TongquMessage tm = (TongquMessage) msg;
		TongquMessage tm2 = tongquMessageService.get( tm.getActid() );
		if (tm2 != null)
			return null;
		return tm;
	}

	public Message beforeGetCallback(Message msg) {
		TongquMessage tm = (TongquMessage) msg;
		return tm;
	}

	public Message beforeUpdateCallback(Message msg) {
		TongquMessage tm = (TongquMessage) msg;
		return tm;
	}

}
