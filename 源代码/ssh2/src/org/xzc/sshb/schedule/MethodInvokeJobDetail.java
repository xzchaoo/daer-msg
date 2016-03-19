package org.xzc.sshb.schedule;

import org.quartz.JobKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.utils.Key;

/**
 * 根据Quartz的意思,一个detail封装了关于一个job的信息 由于JobDetail实现麻烦,这里直接集成了JobDetailImpl 并且该类是与MethodInvokeJob一起使用的
 * 
 * @author xzchaoo
 * 
 */
class MethodInvokeJobDetail extends JobDetailImpl {
	private MethodInvokeJob job;

	public MethodInvokeJobDetail(Object target, String method) {
		this.job = new MethodInvokeJob( target, method );
		setJobClass( MethodInvokeJob.class );
		setKey( new JobKey( Key.createUniqueName( null ), null ) );
	}

	/**
	 * 对于这个实现来说,job是个单例就行了,其他的没必要
	 * 
	 * @return
	 */
	public MethodInvokeJob getJob() {
		return job;
	}

}
