package org.xzc.msg.action;

import javax.annotation.Resource;

import org.xzc.msg.service.RepairService;

public class RepairAction extends ActionBase {
	@Resource
	private RepairService repairService;

	public void clearUMS() {
		repairService.clearUMS();
	}

	public void resetAll() {
		repairService.resetAll();
	}

	public void repairCreatorId() {
		repairService.repairCreatorId();
	}

	public void repairSimpleMessageCreateTime() {
		repairService.repairSimpleMessageCreateTime();
	}
	public void repairGroupCreateTime() {
		repairService.repairGroupCreateTime();
	}
	public void repairUserPublishMessageCount() {
		repairService.repairUserPublishMessageCount();
	}
	
}
