package org.xzc.msg.action;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xzc.msg.schedule.SJTUJWCScheduleTask;
import org.xzc.msg.schedule.TongquScheduleTask;

public class ScheduleAction extends ActionBase {

	private static final Log LOG = LogFactory.getLog( ScheduleAction.class );

	@Resource
	private SJTUJWCScheduleTask SJTUJWCScheduleTask;

	@Resource
	private TongquScheduleTask TongquScheduleTask;

	public void sjtujwc() {
		try {
			SJTUJWCScheduleTask.run();
			success();
		} catch (Exception e) {
			LOG.warn( e );
			exception( e );
		}

	}

	public void tongqu() {
		try {
			TongquScheduleTask.run();
			success();
		} catch (Exception e) {
			LOG.warn( e );
			exception( e );
		}
	}
}
