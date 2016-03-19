package org.xzc.msg.schedule;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.xzc.msg.domain.TongquMessage;
import org.xzc.msg.exception.InvalidMessageException;
import org.xzc.msg.exception.UserNotExistException;
import org.xzc.msg.fetch.TongquFetchService;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.service.TongquMessageService;
import org.xzc.msg.service.UserServiceForFetch;

@Component
public class TongquScheduleTask implements IScheduleTask {
	private static final Log LOG = LogFactory.getLog( TongquScheduleTask.class );

	@Resource
	private MessageService messageService;

	@Resource
	private TongquFetchService tongquFetchService;

	@Resource
	private TongquMessageService tongquMessageService;

	@Resource
	private UserServiceForFetch userServiceForFetch;

	private static boolean running = false;

	public void run() {
		synchronized (TongquScheduleTask.class) {
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

	public void _run() {
		if (LOG.isInfoEnabled())
			LOG.info( "同去网自动扫描任务开始" );

		//TODO 为了调试方便只扫描最新的100个任务 否则任务会太多
		JSONObject jo = tongquFetchService.getListAct( 0, 0, "act.create_time", 0, 10 );
		JSONObject result = jo.getJSONObject( "result" );
		JSONArray acts = result.getJSONArray( "acts" );

		for (int i = 0; i < acts.length(); ++i) {
			JSONObject act = acts.getJSONObject( i );
			int actid = act.getInt( "actid" );

			//查看是否已经有过记录了
			TongquMessage tm = tongquMessageService.get( actid );
			if (tm != null) {
				if (LOG.isInfoEnabled()) {
					LOG.info( "actid=" + actid + ",已经存在,跳过它" );
				}
				continue;
			}

			tm = tongquFetchService.getActDetail0( actid );
			if (tm == null) {
				//获得了一个不存在的任务 同去网删除了这个任务 遇到过若干次...
				if (LOG.isInfoEnabled()) {
					LOG.info( "actid=" + actid + "已经被同去网删除.跳过这个任务" );
				}
				continue;
			}
			try {
				tm.creatorId = userServiceForFetch.getMessagePublisher( tm.getType() ).getId();
				messageService.publishMessage( tm );
				LOG.info( "actid=" + actid + ",添加成功" );
			} catch (InvalidMessageException e) {
				LOG.info( "actid=" + actid + ",添加失败" );
				LOG.warn( e );
			} catch (UserNotExistException e) {
				LOG.warn( e );
			}
		}
	}

}
