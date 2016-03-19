package org.xzc.msg.site.haodaxue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xzc.msg.http.MyClient;
import org.xzc.msg.http.MyParams;

/**
 * 基本思路已经摸清除了
 * 向http://api.cnmooc.org/v1 post数据来获得json反馈
 * post的数据格式
 * cmd=你要执行的命令 比如sys.login 一定要写
 * client="cnmooc" 这个我看是一个定值吧? 对于cnmooc这个app来说
 * 接着你根据你cmd的不同的具体参数 比如cmd=sys.login时需要有:
 * user=账号
 * password=加密后的密码
 * 最后是一个签名
 * sign=取md5(sys.log+"cnmooc"+账号+加密后的密码+"cnmooc@wisedu.com")
 * 因此对于我们要执行的命令来说 我们只需要关注 cmd和你特殊的参数即可
 * 我使用helpcall来简化这个过程
 * 
 * 每个json的code如果是"000000"则表示成功 另外 message属性页表示了code对应的描述
 * @author xzchaoo
 * 
 */
public class HaodaxueService {

	/**
	 * http客户端
	 */
	private MyClient mc;

	/**
	 * 账号
	 */
	private String user;

	/**
	 * 加密后的密码
	 */
	private String password;

	/**
	 * API的url
	 */
	public static final String API_URL = "http://api.cnmooc.org/v1";

	/**
	 * 构造函数接受账号和密码 并且初始化一些东西
	 * 这里传进来的密码必须是已经加密了的
	 * @param user
	 * @param password
	 */
	public HaodaxueService(String user, String password, MyClient mc) {
		this.user = user;
		//this.password = Utils.encryptText( password );
		this.password = password;
		this.mc = mc;
	}

	/**
	 * 获得一个课程的课件作业信息,注意这里的courseid好像没有发挥作用 或者说根本不需要
	 * 虽然不需要 但是还是要填写 可以任意填写
	 */
	public JSONObject getCourseLearn(String sessionId) {
		return wrapCall( "course.learn", "course", 0, "session", sessionId, "all", 1 );
	}

	public JSONObject getCourseMy() {
		return wrapCall( "course.my" );
	}

	/**
	 * 获得一个开课的详情
	 * course无用但还是要指定
	 * @param sessionId
	 * @return
	 */
	public JSONObject getCourseDetail(String sessionId) {
		return wrapCall( "course.detail", "course", 0, "session", sessionId );
	}

	/**
	 * 获取课程分类为subject的 并且含有关键字 keyword的 第index页 每页size条
	 * 如果index=0 则获取全部
	 * @param subject 如果是一位数需要补0
	 * @param keyword
	 * @param index
	 * @param size
	 * @return
	 */
	public JSONObject getCourseList(String subject, String keyword, int index, int size) {
		return wrapCall( "course.list", "index", index, "size", size, "subject", subject, "keyword", keyword );
	}

	/**
	 * 获得课程的类型
	 * 不需要登陆
	 * @return
	 */
	public JSONObject getCourseSubject() {
		return wrapCall( "course.subject", "level", 1 );
	}

	public JSONObject wrapCall(String cmd, Object... params) {
		MyParams mp = MyParams.create( "cmd", cmd ).add( "client", "cnmooc" );
		StringBuilder list = new StringBuilder();
		list.append( cmd );
		list.append( "cnmooc" );
		if (params != null) {
			if (params.length % 2 != 0) {
				throw new IllegalArgumentException( "params参数长度必须为偶数" );
			}
			for (int i = 0; i < params.length; i += 2) {
				String name = params[i].toString();
				Object value = params[i + 1];
				if (value == null)
					value = "";
				mp.add( name, value );
				list.append( value );
			}
		}
		list.append( "cnmooc@wisedu.com" );
		try {
			mp.add( "sign", Utils.hashMD5( list.toString() ).toLowerCase() );
			return new JSONObject( mc.postString( API_URL, null, mp ) );
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
	}

	private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	private static final Pattern DATE_PATTERN = Pattern.compile( "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}" );

	/**
	 * 格式化时间
	 * @param stageEnd
	 * @return
	 */
	private static final String formatTime(String stageEnd) {
		Matcher matcher = DATE_PATTERN.matcher( stageEnd );
		if (matcher.find()) {
			return matcher.group();
		}
		System.out.println( "没找到,不科学" );
		return stageEnd;
	}

	/**
	 * 获得这个开课的作业,无需选课就可以查看
	 * @param sessionId
	 * @return
	 */
	public JSONObject getHomework(String sessionId) {

		JSONObject ret = new JSONObject();
		JSONArray homeworks = new JSONArray();
		ret.put( "homeworks", homeworks );

		//拿到html内容
		String url = "http://www.cnmooc.org/examTest/stuExamListPreview/" + sessionId + ".mooc";
		Document doc = Jsoup.parse( mc.getString( url ) );

		for (Element tr : doc.select( ".table-public tr" )) {//for每一个作业
			try {
				//拿到名字和id
				String name = tr.select( ".td1" ).text();
				String itemid = tr.select( ".td4" ).attr( "itemid" );

				JSONObject homework = new JSONObject().put( "name", name ).put( "itemid", itemid );
				JSONArray stages = new JSONArray();
				homework.put( "stages", stages );
				//互评阶段要特殊处理一下 因为它有两个时间段
				JSONObject hupingStage = null;

				Elements ps = tr.select( ".td3 p" );
				if (ps.size() > 0) {//有p 即有多个阶段
					for (Element p : ps) {
						String stageTest = p.select( ".stage-test" ).text();
						String stageEnd = p.select( ".stage-end" ).text();
						stageEnd = formatTime( stageEnd );
						if ("提交阶段".equals( stageTest )) {
							stages.put( new JSONObject().put( "name", stageTest ).put( "end", stageEnd ) );
						} else if ("互评阶段".equals( stageTest )) {
							hupingStage = new JSONObject().put( "name", stageTest ).put( "start", stageEnd );
							stages.put( hupingStage );
						} else if ("".equals( stageTest )) {
							hupingStage.put( "end", stageEnd );
						} else if ("成绩阶段".equals( stageTest )) {
							stages.put( new JSONObject().put( "name", stageTest ).put( "start", stageEnd ) );
						}
					}
				} else {
					String stageTest = tr.select( ".stage-test" ).text();
					String stageEnd = tr.select( ".stage-end" ).text();
					stageEnd = formatTime( stageEnd );
					stages.put( new JSONObject().put( "name", stageTest ).put( "end", stageEnd ) );
				}
				homeworks.put( homework );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//附上一些信息吧
		ret.put( "updateTime", SDF.format( new Date() ) );
		//ret.put( "url", url );

		return ret;
	}

	public JSONObject getLogin() {
		return wrapCall( "sys.login", "user", user, "password", password );
	}

	/**
	 * 登陆 成功为true 失败为false
	 * @return
	 */
	public boolean login() {
		JSONObject jo = getLogin();
		boolean ret = jo.getString( "code" ).equals( "000000" );
		if (ret) {
			copyCookie( mc.getCookieStore() );
		}
		return ret;
	}

	/**
	 * 我们的cookie一开始是从api.cnmooc.org获得的
	 * 将它的域增加www.cnmooc.org
	 * @param cs
	 */
	private static void copyCookie(CookieStore cs) {
		for (Cookie c : cs.getCookies()) {
			BasicClientCookie bcc = new BasicClientCookie( c.getName(), c.getValue() );
			bcc.setDomain( "www.cnmooc.org" );
			bcc.setExpiryDate( null );
			bcc.setSecure( false );
			bcc.setPath( "/" );
			cs.addCookie( bcc );
		}
	}

}
