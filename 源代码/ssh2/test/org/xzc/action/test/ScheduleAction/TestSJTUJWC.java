package org.xzc.action.test.ScheduleAction;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xzc.action.test.MyTestBase;
import org.xzc.msg.action.ScheduleAction;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;

public class TestSJTUJWC extends MyTestBase<ScheduleAction> {
	private static final String URL = "/api2/schedule/sjtujwc";

	public TestSJTUJWC() {
		super( URL );
	}

	@Test
	public void test_sjtujwc_1() throws Exception {
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );
	}
	
}
