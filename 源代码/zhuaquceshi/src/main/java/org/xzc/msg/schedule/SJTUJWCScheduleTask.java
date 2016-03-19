package org.xzc.msg.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.xzc.msg.domain.SJTUJWCMessage;
import org.xzc.msg.exception.InvalidMessageException;
import org.xzc.msg.exception.UserNotExistException;
import org.xzc.msg.fetch.SJTUJWCMessageFetchService;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.service.SJTUJWCMessageService;
import org.xzc.msg.service.UserServiceForFetch;

@Component
public class SJTUJWCScheduleTask {
	private static final Log LOG = LogFactory.getLog( SJTUJWCScheduleTask.class );

	@Resource
	private MessageService messageService;

	@Resource
	private SJTUJWCMessageFetchService sJTUJWCMessageFetchService;

	@Resource
	private SJTUJWCMessageService sjtujwcMessageService;

	@Resource
	private UserServiceForFetch userServiceForFetch;

	private static boolean running = false;

	private void _run() {

		if (LOG.isInfoEnabled())
			LOG.info( "SJTUJWC自动扫描任务开始" );

		List<SJTUJWCMessage> list = sJTUJWCMessageFetchService.list();
		int count = 0;
		for (SJTUJWCMessage m : list) {
			//个数太多先限制一下
			if (++count == 10)
				break;

			SJTUJWCMessage m2 = sjtujwcMessageService.get( m.getLink() );
			if (m2 != null) {
				if (LOG.isInfoEnabled())
					LOG.info( "SJTUJWC,link=" + m.getLink() + ",已经存在,跳过" );
				continue;
			} else {
				String content = sJTUJWCMessageFetchService.getContent( m.getLink() );
				m.setContent( content );
				m.creatorId = userServiceForFetch.getMessagePublisher( m.getType() ).id;
				m.setCreateTime( m.startTime );
				try {
					messageService.publishMessage( m );
					LOG.info( "SJTUJWC,link=" + m.getLink() + "添加成功" );
				} catch (InvalidMessageException e) {
					LOG.info( "SJTUJWC,link=" + m.getLink() + "添加失败" );
				} catch (UserNotExistException e) {
					LOG.info( "SJTUJWC,link=" + m.getLink() + "添加失败" );
				}
			}
		}
	}

	public void run() {
		synchronized (SJTUJWCScheduleTask.class) {
			if (running)
				return;
		}
		running = true;
		try {
			_run();
		} finally {
			running = false;
		}
	}
}
