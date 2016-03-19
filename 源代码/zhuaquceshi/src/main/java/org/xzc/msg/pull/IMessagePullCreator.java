package org.xzc.msg.pull;

import org.xzc.action.model.MessageActionModel;
import org.xzc.msg.domain.Message;
import org.xzc.msg.exception.InvalidMessageException;

/**
 * 当有消息被推送归来的时候 根据具体的消息类型 进行消息的创建
 * @author xzchaoo
 *
 */
public interface IMessagePullCreator {
	/**
	 * 遇到不合法的消息格式的手可以扔异常 指明原因 比如缺少某些重要字段
	 * 返回值不为空 有问题直接扔异常
	 * @param model
	 * @return 返回创建完毕后的Document 里面包含了所有这个消息所需要的必要属性
	 * @throws InvalidMessageException
	 */
	public Message create(MessageActionModel model) throws InvalidMessageException;

	/**
	 * 返回它可以处理哪种类型
	 * @return
	 */
	public int getHandleType();
}
