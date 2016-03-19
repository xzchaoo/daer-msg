package org.xzc.msg.pull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * 将所有实现了IMessagePullCreator的实例聚集起来 提供一个getMessagePullCreator方法让外界获取
 * @author xzchaoo
 *
 */
@Service
public class MessagePullService {
	private static final Log LOG = LogFactory.getLog( MessagePullService.class );

	@Resource
	private List<IMessagePullCreator> creatorList;

	private Map<Integer, IMessagePullCreator> creatorMap;

	public IMessagePullCreator getMessagePullCreator(int type) {
		return creatorMap.get( type );
	}

	@PostConstruct
	public void init() {
		creatorMap = new HashMap<Integer, IMessagePullCreator>();
		for (IMessagePullCreator c : creatorList) {
			int type = c.getHandleType();
			IMessagePullCreator pre = creatorMap.put( type, c );
			if (pre != null) {
				LOG.warn( "type=" + type + "有多个creator,只有一个creator会起作用." );
			}
		}
	}
}
