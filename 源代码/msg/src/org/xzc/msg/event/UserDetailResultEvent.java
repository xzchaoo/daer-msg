package org.xzc.msg.event;

import org.xzc.msg.domain.UserForDetail;
import org.xzc.msg.result.ResultBase;

/**
 * 查询某个用户的简单信息的结果
 * @author xzchaoo
 *
 */
public class UserDetailResultEvent extends ResultBase{
	public UserForDetail user;
}
