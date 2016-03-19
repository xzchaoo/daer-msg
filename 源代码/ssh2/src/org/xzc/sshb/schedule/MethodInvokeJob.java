package org.xzc.sshb.schedule;

import org.apache.commons.beanutils.MethodUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 用于调用target的method方法
 * 
 * @author xzchaoo
 * 
 */
class MethodInvokeJob implements Job {
	private String method;
	private Object target;

	public MethodInvokeJob(Object target, String method) {
		this.method = method;
		this.target = target;
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			MethodUtils.invokeMethod( target, method, null );
		} catch (Exception e) {
			throw new JobExecutionException( e );
		}
	}

}
