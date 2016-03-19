package org.xzc.action.test.ScheduleAction;

import org.junit.Test;
import org.xzc.action.test.MyTestBase;
import org.xzc.msg.action.ScheduleAction;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;

public class TestTongqu extends MyTestBase<ScheduleAction> {

	public static final String URL = "/api2/schedule/tongqu";

	public TestTongqu() {
		super( URL );
	}

	@Test
	public void test_tongqu_1() throws Exception {
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );
	}
}
