package org.xzc.msg.site.haodaxue;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.xzc.msg.http.MyClient;

public class Main {

	@Test
	public void testaa() throws Exception {
		MyClient mc = new MyClient();
		HaodaxueService s = new HaodaxueService( "ceshi_20150709@163.com", "ZaQoskjcFmfPEBv8OuKRpg==", mc );
		if (s.login()) {
			System.out.println( "登陆成功" );
		}
		//旧数据
		JSONObject old = new JSONObject(
				"{\"homeworks\":[{\"itemid\":\"16119\",\"name\":\"客观练习 之 演练\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-03-18 23:59\"}]},{\"itemid\":\"16120\",\"name\":\"主观练习 之 演练\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-03-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-03-19 00:00\",\"end\":\"2015-03-21 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-03-23 00:00\"}]},{\"itemid\":\"16854\",\"name\":\"关系模型/关系代数练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-01 23:59\"}]},{\"itemid\":\"16855\",\"name\":\"关系模型/关系代数 主观练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-01 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-04-02 12:00\",\"end\":\"2015-04-05 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-04-07 00:00\"}]},{\"itemid\":\"16852\",\"name\":\"Web应用开发第一次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-03 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-04-12 00:00\"}]},{\"itemid\":\"22994\",\"name\":\"Web应用开发第二次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-11 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-20 00:00\"}]},{\"itemid\":\"31389\",\"name\":\"Web应用开发第三次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-28 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-07-06 00:00\"}]},{\"itemid\":\"22937\",\"name\":\"SQL基本练习-客观\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-03 23:59\"}]},{\"itemid\":\"26084\",\"name\":\"SQL判断题\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"}]},{\"itemid\":\"26085\",\"name\":\"SQL上机练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-19 00:00\",\"end\":\"2015-05-22 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-25 00:00\"}]},{\"itemid\":\"26083\",\"name\":\"数据库设计E/R\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-19 00:00\",\"end\":\"2015-05-22 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-24 00:00\"}]},{\"itemid\":\"29359\",\"name\":\"数据库设计\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-30 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-31 00:00\",\"end\":\"2015-06-03 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-06-05 00:00\"}]},{\"itemid\":\"32839\",\"name\":\"范式分析【客观题】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"}]},{\"itemid\":\"32840\",\"name\":\"隔离级别分析【客观题】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"}]},{\"itemid\":\"32841\",\"name\":\"隔离级别分析【主观题】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-06-19 00:00\",\"end\":\"2015-06-21 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-06-23 00:00\"}]}],\"updateTime\":\"2015-07-10 10:22\"}" );
		//新数据
		JSONObject jo = s.getHomework( "159" );
		//我们要能反应出 增加 删除 修改
		mc.close();
	}

	public static final class HomeworkAndJSON {
		public Homework homework;
		public JSONObject json;

		public HomeworkAndJSON(Homework homework, JSONObject json) {
			this.homework = homework;
			this.json = json;
		}
	}

	@Test
	public void testChagne() {
		JSONObject o = new JSONObject("{\"homeworks\":[{\"itemid\":\"16120\",\"name\":\"主观练习 之 演练\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-03-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-03-19 00:00\",\"end\":\"2015-03-21 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-03-23 00:00\"}]},{\"itemid\":\"16854\",\"name\":\"关系模型/关系代数练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-01 23:59\"}]},{\"itemid\":\"16855\",\"name\":\"关系模型/关系代数 主观练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-01 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-04-02 12:00\",\"end\":\"2015-04-05 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-04-07 00:00\"}]},{\"itemid\":\"16852\",\"name\":\"Web应用开发第一次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-03 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-04-12 00:00\"}]},{\"itemid\":\"22994\",\"name\":\"Web应用开发第二次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-11 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-20 00:00\"}]},{\"itemid\":\"31389\",\"name\":\"Web应用开发第三次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-28 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-07-06 00:00\"}]},{\"itemid\":\"22937\",\"name\":\"SQL基本练习-客观\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-03 23:59\"}]},{\"itemid\":\"26084\",\"name\":\"SQL判断题\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"}]},{\"itemid\":\"26085\",\"name\":\"SQL上机练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-19 00:00\",\"end\":\"2015-05-22 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-25 00:00\"}]},{\"itemid\":\"26083\",\"name\":\"数据库设计E/R\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-19 00:00\",\"end\":\"2015-05-22 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-24 00:00\"}]},{\"itemid\":\"29359\",\"name\":\"数据库设计\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-30 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-31 00:00\",\"end\":\"2015-06-03 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-06-05 00:00\"}]},{\"itemid\":\"32839\",\"name\":\"范式分析【客观题】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"}]},{\"itemid\":\"32840\",\"name\":\"隔离级别分析【客观题】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"}]},{\"itemid\":\"32841\",\"name\":\"隔离级别分析【主观题】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-06-19 00:00\",\"end\":\"2015-06-21 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-06-23 00:00\"}]}],\"updateTime\":\"2015-07-10 10:22\"}" );
		JSONObject n = new JSONObject("{\"homeworks\":[{\"itemid\":\"16119\",\"name\":\"客观练习 之 演练\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-03-18 23:59\"}]},{\"itemid\":\"16854\",\"name\":\"关系模型/关系代数练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-01 23:59\"}]},{\"itemid\":\"16855\",\"name\":\"关系模型/关系代数 主观练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-01 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-04-02 12:00\",\"end\":\"2015-04-05 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-04-07 00:00\"}]},{\"itemid\":\"16852\",\"name\":\"Web应用开发第一次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-04-03 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-04-12 00:00\"}]},{\"itemid\":\"22994\",\"name\":\"Web应用开发第二次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-11 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-20 00:00\"}]},{\"itemid\":\"31389\",\"name\":\"Web应用开发第三次迭代\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-28 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-07-06 00:00\"}]},{\"itemid\":\"22937\",\"name\":\"SQL基本练习-客观\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-03 23:59\"}]},{\"itemid\":\"26084\",\"name\":\"SQL判断题\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"}]},{\"itemid\":\"26085\",\"name\":\"SQL上机练习\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-19 00:00\",\"end\":\"2015-05-22 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-25 00:00\"}]},{\"itemid\":\"26083\",\"name\":\"数据库设计E/R\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-19 00:00\",\"end\":\"2015-05-22 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-05-24 00:00\"}]},{\"itemid\":\"29359\",\"name\":\"数据库设计\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-05-30 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-05-31 00:00\",\"end\":\"2015-06-03 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-06-05 00:00\"}]},{\"itemid\":\"32839\",\"name\":\"范式分析【客观题】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"}]},{\"itemid\":\"32840\",\"name\":\"隔离级别分析【客观题】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"}]},{\"itemid\":\"32841\",\"name\":\"隔离级别分析【主观题2】\",\"stages\":[{\"name\":\"提交阶段\",\"end\":\"2015-06-18 23:59\"},{\"name\":\"互评阶段\",\"start\":\"2015-06-19 00:00\",\"end\":\"2015-06-21 23:59\"},{\"name\":\"成绩阶段\",\"start\":\"2015-06-23 00:00\"}]}],\"updateTime\":\"2015-07-10 10:22\"}" );
		Map<Integer, HomeworkAndJSON> om = getMap( o );
		Map<Integer, HomeworkAndJSON> nm = getMap( n );
		for (Integer itemid : nm.keySet()) {
			HomeworkAndJSON nhj = nm.get( itemid );
			HomeworkAndJSON ohj = om.remove( itemid );
			if (ohj == null) {
				//有新作业
				System.out.println( "有新作业" );
			//} else if (!ohj.json.toString().equals( nhj.json.toString() )) {
			} else if (!ohj.homework.toString2().equals( nhj.homework.toString2() )) {
				//注意别让null值影响你的判断
				//主要是担心json的属性的顺序不一致 导致判断结果错误
				//TODO 这里只用json的字符串比较 靠谱吗?
				//作业状态不一样 可能是时间被改动了
				System.out.println( "作业改动" );
			} else {
				//都一样 这个作业没有变化
			}
		}
		if (!om.isEmpty()) {
			//则om里剩下的都是被删除的作业 虽然好像不会发生的样子啊 但是还是标记一下
			System.out.println( "有被删除的作业" );
		}
	}

	private static Map<Integer, HomeworkAndJSON> getMap(JSONObject o) {
		JSONArray hs = o.getJSONArray( "homeworks" );
		Map<Integer, HomeworkAndJSON> ret = new HashMap<Integer, HomeworkAndJSON>();
		for (int i = 0; i < hs.length(); ++i) {
			JSONObject json = hs.getJSONObject( i );
			Homework h = new Homework( json );
			ret.put( h.itemid, new HomeworkAndJSON( h, json ) );
		}
		return ret;
	}

	public void testName() throws Exception {

		String user = "70862045@qq.com";
		String password = "123456";

		MyClient mc = new MyClient();
		HaodaxueService s = new HaodaxueService( user, password, mc );
		s.login();
		//s.getCourseMy();
		//System.out.println( s.getCourseList() );
		//System.out.println( s.getCourseLearn( 1262 ) );
		System.out.println( s.getCourseLearn( "159" ) );

		// s.getCourseDetail();
		/*
		if (s.login()) {
			System.out.println( "登陆成功" );
		} else {
			System.out.println( "登陆失败" );
		}*/
		/*
				List<NameValuePair> datas = new ArrayList<NameValuePair>();
				datas.add( new BasicNameValuePair( "cmd", "sys.login" ) );
				datas.add( new BasicNameValuePair( "client", "cnmooc" ) );
				datas.add( new BasicNameValuePair( "user", user ) );
				datas.add( new BasicNameValuePair( "password", epassword ) );
				List<String> list = new ArrayList<String>();
				list.add( "sys.login" );
				list.add( "cnmooc" );
				list.add( user );
				list.add( epassword );
				list.add( "cnmooc@wisedu.com" );
				String elist = Utils.setHashMD5List( list );
				datas.add( new BasicNameValuePair( "sign", elist.toLowerCase() ) );

				String url = "http://api.cnmooc.org/v1";
				BasicCookieStore bcs = new BasicCookieStore();
				init( bcs );
				CloseableHttpClient hc = HttpClients.custom().setDefaultCookieStore( bcs ).build();
				CloseableHttpResponse res = hc.execute( RequestBuilder.post( url )
						.setEntity( new UrlEncodedFormEntity( datas ) ).build() );
				System.out.println( EntityUtils.toString( res.getEntity() ) );
				System.out.println( bcs );

				res = hc.execute( RequestBuilder.post( url ).setEntity( new UrlEncodedFormEntity( datas ) ).build() );
				System.out.println( EntityUtils.toString( res.getEntity() ) );
				System.out.println( bcs );

				hc.close();*/
	}

}
