package org.xzc.sshb.schedule;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * 对原有的SimpleJobFactory(这个是Quartz的默认实现)进行增强
 * 如果目标是MethodInvokeJobDetail,那么就处理它,否则就扔给SimpleJobFactory去实现
 * 
 * @author xzchaoo
 * 
 */
class MethodInvokeJobFactory implements JobFactory {
	private SimpleJobFactory simpleJobFactory = new SimpleJobFactory();

	public Job newJob(TriggerFiredBundle tfb, Scheduler s) throws SchedulerException {
		JobDetail jd = tfb.getJobDetail();
		if (jd instanceof MethodInvokeJobDetail) {
			return ( (MethodInvokeJobDetail) jd ).getJob();
		}
		return simpleJobFactory.newJob( tfb, s );
	}

}
