package org.xzc.msg.pull;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.xzc.action.model.MessageActionModel;
import org.xzc.msg.domain.TongquMessage;
import org.xzc.msg.exception.InvalidMessageException;
import org.xzc.msg.fetch.TongquFetchService;
import org.xzc.msg.service.MessageType;

/**
 * type=2
 * create方法里提到的都是必填字段
 * 由于同去网不太可能主动推送消息过来 因此这个类几乎不会被用到...
 * @author xzchaoo
 *
 */
@Component
public class TongquMessagePullCreator implements IMessagePullCreator {
	@Resource
	private TongquFetchService tongquFetchService;

	public TongquMessage create(MessageActionModel model) throws InvalidMessageException {
		int actid = model.actid;

		TongquMessage tm = tongquFetchService.getActDetail0( actid );
		if (tm == null)
			throw new InvalidMessageException( "actid=" + actid + "消息不存在." );
		return tm;
	}

	public int getHandleType() {
		return MessageType.TONGQU;
	}

}
