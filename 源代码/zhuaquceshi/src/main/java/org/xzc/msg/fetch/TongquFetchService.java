package org.xzc.msg.fetch;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.xzc.msg.convert.TongquConvertUtils;
import org.xzc.msg.domain.TongquMessage;
import org.xzc.msg.http.MyClient;
import org.xzc.msg.http.MyParams;
import org.xzc.msg.service.MessageType;

/**
 * 不带2的是原始的json对象
 * 带2的都是表示加工过后的对象
 * @author xzchaoo
 *
 */
@Service
public class TongquFetchService {
	public static final String INDEX = "http://www.tongqu.me/index.php";
	private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );

	private static final SimpleDateFormat SDF2 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	@Resource
	private MyClient myClient;

	public TongquFetchService() {
	}

	public JSONObject getActDetail(int actid) {
		return new JSONObject( myClient.getString( INDEX + "/api/act/detail?id=" + actid ) );
	}

	public TongquMessage getActDetail0(int actid) {
		JSONObject jo = getActDetail( actid );
		if (jo.getInt( "error" ) != 0)
			return null;
		JSONObject mi = jo.getJSONObject( "main_info" );

		TongquMessage tm = new TongquMessage();

		try {
			tm.setCreateTime( SDF2.parse( mi.getString( "create_time" ) ) );
			tm.setSignStartTime( SDF2.parse( mi.getString( "sign_start_time" ) + ":00" ) );
			tm.setSignEndTime( SDF2.parse( mi.getString( "sign_end_time" ) + ":00" ) );
			tm.setEndTime( SDF2.parse( mi.getString( "end_time" ) + ":00" ) );
			tm.setStartTime( SDF2.parse( mi.getString( "start_time" ) + ":00" ) );
		} catch (ParseException e) {
			throw new RuntimeException( e );
		}

		tm.setActid( actid );
		tm.setMaxMember( mi.getInt( "max_member" ) );
		tm.setMemberCount( mi.getInt( "member_count" ) );

		//某些活动这个属性可能不存在
		tm.setPhotolink( mi.optString( "photolink" ) );

		tm.setType2( mi.getInt( "type" ) );
		tm.setContent( jo.getString( "body" ) );
		tm.setLocation( mi.getString( "location" ) );
		tm.setName( mi.getString( "name" ) );
		tm.setSource( mi.getString( "source" ) );
		tm.setFrom( "同去网" );
		tm.setType( MessageType.TONGQU );
		return tm;
	}

	public Document getActDetail3(int actid) {
		JSONObject json = getActDetail( actid );
		return TongquConvertUtils.detailJSONToDocument( json );
	}

	public JSONObject getListAct(int type, int status, String order, int offset, int number) {
		/*
		-1最新 0全部 1艺术 2讲座 4招聘 5游学 7比赛 8其他 9招新 10户外
		rb.addParameter( "type", "-1" );
		// 0全部 1正在报名 2未开始报名 3正在进行 4已结束
		rb.addParameter( "status", "0" );
		// hotvalue热度 view_count浏览量 act.create_time发布时间 sign_start_time报名时间 start_time活动时间
		rb.addParameter( "order", "hotvalue" );
		// 起点
		rb.addParameter( "offset", "0" );
		// 个数 好像不设上限...
		rb.addParameter( "number", "77" );
		// 整个不知道什么作用
		rb.addParameter( "desc", "false" );
		*/
		return new JSONObject( myClient.getString( INDEX + "/api/act/type",
				MyParams.create( "type", type ).add( "status", status ).add( "order", order ).add( "offset", offset )
						.add( "number", number ) ) );
	}

	public String getListAct0(int type, int status, String order, int offset, int number) {
		return myClient.getString( INDEX + "/api/act/type", MyParams.create( "type", type ).add( "status", status )
				.add( "order", order ).add( "offset", offset ).add( "number", number ) );
	}

	/**
	 * 每页固定显示5个
	 * @param keyword
	 * @param page
	 * @return
	 */
	public JSONObject getSearch(String keyword, int page) {
		return new JSONObject( myClient.getString( INDEX + "/api/v1/search/act/search",
				MyParams.create( "word", keyword ).add( "page", page ) ) );
	}

	/**
	 * 返回当前的所有任务的总数
	 * 一般没太大用处 除非是用来判断说有没有任务增加了...
	 * @return
	 */
	public int getTotalCount() {
		JSONObject jo = getListAct( 0, 0, "hotvalue", 0, 1 );
		return jo.getJSONObject( "result" ).getInt( "count" );
	}

	public JSONArray getTypeList() {
		return new JSONArray( myClient.getString( INDEX + "/api/act/getTypeList" ) );
	}

	@PostConstruct
	public void init() {

	}
}
