package org.xzc.sshb.schedule;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xzc.msg.schedule.SJTUJWCScheduleTask;
import org.xzc.msg.schedule.TongquScheduleTask;

public class MyJobHolder {
	private static final Log LOG = LogFactory.getLog( MyJobHolder.class );
	
	@Resource
	private SJTUJWCScheduleTask sjtujwcScheduleTask;
	
	@Resource
	private TongquScheduleTask tongquScheduleTask;
	
	public void autoScanSJTUJWC() {
		sjtujwcScheduleTask.run();
	}
	public void autoScanTongqu() {
		tongquScheduleTask.run();
	}
}
