package org.xzc.action.test.MessageAction;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.xzc.action.test.MyTestBase;
import org.xzc.msg.action.MessageAction;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;
import org.xzc.msg.domain.Message;

public class TestAdd extends MyTestBase<MessageAction> {
	public static final String URL = "/api2/msg/add";

	/**
	 * 注意hh和HH的区别啊
	 * hh01-12 会导致时间被截断!
	 */
	private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	public TestAdd() {
		super( URL );
	}

	/**
	 * 还没登陆就想发布消息
	 * @throws Exception
	 */
	@Test
	public void test_add_1() throws Exception {
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.NOT_LOGIN );
	}

	/**
	 * 登陆后发布 不支持的类型的消息
	 * @throws Exception
	 */
	@Test
	public void test_add_2() throws Exception {
		mockLogin();
		//不存在的type
		add( "type", "70862045" );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.UNSUPPORTED_TYPE );
	}

	/**
	 * 添加简单消息 type=1 但是缺少缺少关键是属性
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_add_3() throws Exception {
		mockLogin();

		add( "type", "1" );
		//add( "name", "明天体育课因为天气原因放假." );
		add( "content", "明天体育课因为天气原因放假.同学们在宿舍 里自习吧." );
		add( "location", "南体" );
		Date now = new Date();
		add( "startTime", SDF.format( now ) );

		MyJSON mj = execute();
		assertEquals( mj.code(), Code.INVALID_MESSAGE );
	}

	/**
	 * 添加简单消息 type=1 必填字段都填写了 合法
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_add_4() throws Exception {
		mockLogin();

		add( "type", "1" );
		add( "name", "明天体育课因为天气原因放假." );
		add( "content", "明天体育课因为天气原因放假.同学们在宿舍 里自习吧." );
		add( "location", "南体" );
		Date now = new Date();
		now = DateUtils.truncate( now, Calendar.SECOND );

		add( "startTime", SDF.format( now ) );

		MyJSON mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );
		assertTrue( mj.getResult() instanceof Message );
		Message msg = (Message) mj.getResult();
		assertEquals( msg.getName(), "明天体育课因为天气原因放假." );
		System.out.println( msg.getStartTime().getTime() );
		System.out.println( now.getTime() );
		//需要截断
		assertEquals( msg.getStartTime(), now );
	}

	/**
	 * 添加同去网上的消息
	 * @throws Exception
	 */
	@Test
	public void test_add_5() throws Exception {
		mockLogin();
		add( "type", 2 );
		add( "actid", 9687 );
		MyJSON mj = execute();
		assertEquals( mj.code(), Code.SUCCESS );
	}

}
