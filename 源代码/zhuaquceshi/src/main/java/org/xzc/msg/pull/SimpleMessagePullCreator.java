package org.xzc.msg.pull;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.xzc.action.model.MessageActionModel;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.SimpleMessage;
import org.xzc.msg.exception.InvalidMessageException;
import org.xzc.msg.service.MessageType;
import org.xzc.msg.utils.Assert;
import org.xzc.msg.utils.Utils;

/**
 * type=1
 * 必填字段 type name content
 * startTime endTime location选填
 * @author xzchaoo
 *
 */
@Component
public class SimpleMessagePullCreator implements IMessagePullCreator {

	public Message create(MessageActionModel model) throws InvalidMessageException {
		Assert.notNull( model, "model不能为null." );

		if (Utils.isEmpty( model.getName() ))
			throw new InvalidMessageException( "type=" + model.type + "的消息需要指定name." );
		if (Utils.isEmpty( model.getContent() ))
			throw new InvalidMessageException( "type=" + model.type + "的消息需要指定content." );

		SimpleMessage sm = new SimpleMessage();

		sm.setType( model.type );
		sm.setName( model.name );
		sm.setContent( model.content );

		sm.setStartTime( model.startTime );
		sm.setEndTime( model.endTime );
		sm.setLocation( model.location );
		
		sm.setType( getHandleType() );
		sm.createTime=new Date();
		return sm;
	}

	public int getHandleType() {
		return MessageType.SIMPLE;
	}

}
