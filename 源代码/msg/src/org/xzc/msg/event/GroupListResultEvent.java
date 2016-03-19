package org.xzc.msg.event;

import java.util.List;

import org.xzc.msg.domain.GroupForList;
import org.xzc.msg.result.ResultBase;

/**
 *  group list 的结果
 * @author xzchaoo
 *
 */
public class GroupListResultEvent extends ResultBase{
	public int total;
	public List<GroupForList> groups;
}
