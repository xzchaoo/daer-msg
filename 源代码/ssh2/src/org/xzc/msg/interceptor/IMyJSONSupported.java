package org.xzc.msg.interceptor;

import org.xzc.msg.action.json.MyJSON;

/**
 * 为Action类打开一个标记
 * 当导致
 * 	1.全部的返回值都是"success" 因此你的方法可以返回void
 * 2.所有没有接住(处理)的方法都会导致返回 一个表示异常的json 并且被记录日志
 * @author xzchaoo
 *
 */
public interface IMyJSONSupported {
	public MyJSON getMyJSON();
}
