package org.xzc.msg.event;

import org.xzc.msg.result.ResultBase;

/**
 *  group/addUserToGroup 的结果
 * @author xzchaoo
 *
 */
public class AddUserToGroupResultEvent extends ResultBase{
	public int userId;
	public int groupId;
}
