package org.xzc.action.test.MessageAction;

import org.junit.Test;
import org.xzc.action.test.MyTestBase;
import org.xzc.msg.action.MessageAction;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.Page;

public class TestByid extends MyTestBase<MessageAction> {
	public static final String URL = "/api2/msg/byid";

	public TestByid() {
		super( URL );
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void test_byid_1() throws Exception {
		add( "type", 2 );
		MyJSON mj = execute( "/api2/msg/list" );
		assertEquals( mj.code(), Code.SUCCESS );
		Page<Message> p = (Page<Message>) mj.getResult();
		finishExecution();
		if (p.getSize() > 0) {
			Message m = p.getList().get( 0 );
			add( "id", m.getId() );
			mj = execute(URL);
			assertEquals( mj.code(), Code.SUCCESS );
			assertEquals( ( (Message) mj.getResult() ).getName(), m.getName() );
		}
		finishExecution();
	}

	/**
	 * 不存在的id
	 * @throws Exception
	 */
	@Test
	public void test_byid_2() throws Exception {
		add( "id", 70862045 );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.NO_MESSAGE );
	}

}
