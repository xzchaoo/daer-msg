package org.xzc.sshb.schedule;

import java.util.List;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Scheduler的启动入口
 * 
 * @author xzchaoo
 * 
 */
public class Scheduler {
	private org.quartz.Scheduler s;
	private List<Job> jobs;
	private Object defaultJobHolder;

	/**
	 * bean初始化后调用
	 * 
	 * @throws SchedulerException
	 */
	public void init() throws SchedulerException {
		s = new StdSchedulerFactory().getScheduler();
		s.setJobFactory( new MethodInvokeJobFactory() );
		if (jobs != null)
			for (Job e : jobs) {
				if (defaultJobHolder != null && e.getTarget() == null)
					e.setTarget( defaultJobHolder );
				s.scheduleJob( e.generateJobDetail(), e.generateCronTrigger() );
			}
		s.start();
	}

	/**
	 * bean关闭的时候调用
	 * 
	 * @throws SchedulerException
	 */
	public void close() throws SchedulerException {
		s.shutdown();
	}

	/**
	 * 注入任务安排
	 * 
	 * @param job1s
	 */
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public void setDefaultJobHolder(Object defaultJobHolder) {
		this.defaultJobHolder = defaultJobHolder;
	}

}
