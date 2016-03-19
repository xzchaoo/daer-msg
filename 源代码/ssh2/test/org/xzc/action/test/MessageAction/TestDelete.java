package org.xzc.action.test.MessageAction;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.junit.Test;
import org.xzc.action.test.MyTestBase;
import org.xzc.msg.action.MessageAction;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;
import org.xzc.msg.domain.Message;
import org.xzc.msg.service.MessageService;

public class TestDelete extends MyTestBase<MessageAction> {
	public static final String URL = "/api2/msg/delete";

	@Resource
	private MessageService messageService;

	public TestDelete() {
		super( URL );
	}

	@Test
	public void test_delete_1() throws UnsupportedEncodingException, ServletException {
		mockLogin();

		add( "type", "1" );
		add( "name", "明天体育课因为天气原因放假." );
		add( "content", "明天体育课因为天气原因放假.同学们在宿舍 里自习吧." );
		add( "location", "南体" );

		MyJSON mj = execute( TestAdd.URL );
		assertEquals( mj.code(), Code.SUCCESS );
		assertTrue( mj.getResult() instanceof Message );
		Message msg = (Message) mj.getResult();
		assertEquals( msg.getName(), "明天体育课因为天气原因放假." );

		finishExecution();
		add( "id", msg.getId() );
		mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );

		finishExecution();

		add( "id", msg.getId() );
		mj = execute( TestByid.URL );
		assertEquals( mj.code(), Code.NO_MESSAGE );
	}

}
