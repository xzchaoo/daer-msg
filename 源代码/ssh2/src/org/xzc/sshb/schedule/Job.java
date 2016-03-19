package org.xzc.sshb.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

/**
 * 为了方便,名字尽量短,封装了一个任务以怎样的cronExpression执行
 * 
 * @author xzchaoo
 * 
 */
public class Job {
	private String cronExpression;
	private Object target;
	private String method;

	public MethodInvokeJobDetail generateJobDetail() {
		return new MethodInvokeJobDetail( target, method );
	}

	public CronTrigger generateCronTrigger() {
		return TriggerBuilder.newTrigger()
				.withSchedule( CronScheduleBuilder.cronSchedule( cronExpression ) ).build();
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public void setTarget(Object target) {
		this.target=target;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object getTarget() {
		return target;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public String getMethod() {
		return method;
	}

}
