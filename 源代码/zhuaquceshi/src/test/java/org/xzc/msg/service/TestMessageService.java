package org.xzc.msg.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.xzc.msg.factory.XZCFactory;
import org.xzc.msg.fetch.TongquFetchService;

public class TestMessageService {
	private static final Log LOG = LogFactory.getLog( TestMessageService.class );

	private MessageService ms = XZCFactory.getMessageService();

	private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	@Test
	public void testaa() {
		TongquFetchService ts = XZCFactory.getTongquService();
		JSONObject jo = ts.getListAct( 0, 0, "act.create_time", 0, 100 );
		JSONObject result = jo.getJSONObject( "result" );
		System.out.println( result.getInt( "count" ) );
		System.out.println( jo );
	}

	public void autoSearch() {
		LOG.info( "自动扫描同去网 开始 当前时间:" + SDF.format( new Date() ) );
		//System.out.println(XZCFactory.getTongquService().getTotalCount());

		//采用这样的策略
		//actid总体是递增的
		//保存上次扫描到哪个actid  lastActid=9700
		//然后获取当前最大的(同去网的actid与发布时间有点差(并不总是一样))
		//不过我们总是可以取最新的100个 然后选择最大的actid 这样总该不会错了吧?
		//然后 for actid=lastActid -> maxActId 这两者之间一般 不会差太多
		//这之间的内容就是新的消息 注意同去网上有部分actid是无效的 他们的内容被删除了 返回error=1
		//扫描完后 lastActid=maxActid
		//如此循环就可以不断添加新消息

		//然后关于如果消息被改变了
		//我们可以利用在get函数里的callback
		//我们加入一个字段 lastUpdateTime 上一次从源网站更新时间
		//如果超过1天 或 多少时间 我们就更新一次
		//这样一定程度上自动避免了活动内容改变

		//但是同去网有一些棘手的字段 比如当前报名人数
		//策略1.让客户端自己去获取 手机上是可以跨域的
		//策略2.采用消息改变的处理方法 大概若干分钟就算超时 然后就可以重新刷一次 这样压力应该不会太大吧?

		//活动被删除
		//这个还真是坑爹 一般应该不会的吧?
		//通过某种方式标记一个document为永远不再从源地址更新

		TongquFetchService ts = XZCFactory.getTongquService();
		JSONObject jo = ts.getListAct( 0, 0, "act.create_time", 0, 100 );
		//找出当前最大的actid
		JSONArray acts = jo.getJSONObject( "result" ).getJSONArray( "acts" );

		int maxActId = -1;
		for (int i = 0; i < acts.length(); ++i) {
			JSONObject a = acts.getJSONObject( i );
			int actid = a.getInt( "actid" );
			maxActId = maxActId > actid ? maxActId : actid;
		}
		LOG.info( "maxActid=" + maxActId );
		int lastActid = 9900;
		for (int actid = lastActid; actid <= maxActId; ++actid) {
			Document d = ts.getActDetail3( actid );
			if (d != null)
				XZCFactory.getMessageService().publishMessage( d );
		}
		/*for (int i = 0; i < acts.length(); ++i) {
			int cur=Integer.parseInt(acts.getJSONObject( i ).getString( "actid" ));
			if(cur>=last){
				System.out.println("出错了 "+last+" "+cur);
			}
			last=cur;
		}*/
		LOG.info( "自动扫描同去网 结束 当前时间:" + SDF.format( new Date() ) );
	}

	@Test
	public void testAdd() {
		JSONObject jo = XZCFactory.getTongquService().getListAct( 0, 0, "act.create_time", 4460, 7 );
		System.out.println( jo );
		JSONArray acts = jo.getJSONObject( "result" ).getJSONArray( "acts" );
		System.out.println( acts.length() );
		System.out.println( acts.getJSONObject( 0 ).toString( 2 ) );
		System.out.println( acts.getJSONObject( acts.length() - 1 ).toString( 2 ) );
		/*
		 * List<Document> toBeInserted = new ArrayList<Document>();
		for (int i = 0; i < acts.length(); ++i) {
			JSONObject a = acts.getJSONObject( i );
			Document d = new Document();
			d.append( "name", a.getString( "name" ) );
			d.append( "type", MessageType.TONGQU );
			d.append( "startTime", a.getString( "start_time" ) );
			d.append( "endTime", a.getString( "end_time" ) );
			d.append( "from", "同去网" );
			d.append( "source",a.getString( "source" ));
			
			//同去网额外属性
			d.append( "actid", a.getString( "actid" ) );
			toBeInserted.add( d );
		}
		
		ms.addMany(toBeInserted);*/
	}

	@Test
	public void testList() {
		Document d = ms.list( 130, 10,0 );
		System.out.println( d.toJson() );
	}
}
