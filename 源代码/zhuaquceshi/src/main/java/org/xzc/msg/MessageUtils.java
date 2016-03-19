package org.xzc.msg;


/*
public class MessageUtils {
	public static TongquListActBean toTongquListActBean(JSONObject json) {
		TongquListActBean ret = new TongquListActBean();

		JSONObject result = json.getJSONObject( "result" );
		JSONObject opt = json.getJSONObject( "opt" );

		List<TongquMessage> acts = new ArrayList<TongquMessage>();

		JSONArray ja = result.getJSONArray( "acts" );
		for (int i = 0; i < ja.length(); ++i) {
			JSONObject jo = ja.getJSONObject( i );
			acts.add( _toTongquMessage( jo ) );
		}

		ret.setActs( acts );

		ret.setOrder( opt.getString( "order" ) );
		ret.setOffset( opt.getInt( "offset" ) );
		ret.setSize( opt.getInt( "number" ) );//与number对应
		ret.setStatus( opt.getInt( "time_status" ) );
		ret.setTotal( result.getInt( "count" ) );//总个数与count对应
		ret.setType( opt.getInt( "type_id" ) );

		return ret;
	}

	public static TongquMessage toTongquMessage(JSONObject json) {
		JSONObject main_info = json.getJSONObject( "main_info" );
		TongquMessage tm = _toTongquMessage( main_info );
		tm.setContent( json.optString( "body" ) );
		//TODO 临时
		tm.setId( tm.getActid() );
		return tm;
	}

	public static List<TongquType> toTongquTypeList(JSONArray json) {
		List<TongquType> ret = new ArrayList<TongquType>();
		for (int i = 0; i < json.length(); ++i) {
			JSONObject jo = json.getJSONObject( i );
			TongquType tt = new TongquType();
			tt.setRank( jo.getInt( "rank" ) );
			tt.setTypeid( jo.getInt( "typeid" ) );
			tt.setTypename( jo.getString( "typename" ) );
			ret.add( tt );
		}
		return ret;
	}

	private static TongquMessage _toTongquMessage(JSONObject json) {
		TongquMessage tm = new TongquMessage();
		tm.setActid( json.optInt( "actid" ) );
		tm.setDetailType( "tongquwang" );
		tm.setEndTime( json.optString( "end_time" ) );
		tm.setFrom( "同去网" );
		tm.setKeyword( json.optString( "key_word" ) );
		tm.setLocation( json.optString( "location" ) );
		tm.setMaxMember( json.optInt( "max_member" ) );
		tm.setMemberCount( json.optInt( "member_count" ) );
		tm.setName( json.optString( "name" ) );
		tm.setPhotolink( json.optString( "photolink" ) );
		tm.setSignEndTime( json.optString( "sign_end_time" ) );
		tm.setSignStartTime( json.optString( "sign_start_time" ) );
		tm.setSource( json.optString( "source" ) );
		tm.setStartTime( json.optString( "start_time" ) );
		tm.setType( MessageType.TONGQU );
		tm.setType2( json.optInt( "type" ) );
		//TODO 临时 
		tm.setId( tm.getActid() );
		return tm;
	}

	public static TongquSearchBean toTongquSearchBean(JSONObject json, String keyword) {
		TongquSearchBean ret = new TongquSearchBean();
		ret.setKeyword( keyword );
		ret.setPage( json.getJSONObject( "page" ).getInt( "current" ) );
		ret.setTotalPage( json.getJSONObject( "page" ).getInt( "total" ) );
		ret.setTotal( json.getInt( "count" ) );
		List<TongquMessage> acts = new ArrayList<TongquMessage>();
		ret.setActs( acts );
		JSONArray ja = json.getJSONArray( "acts" );
		for (int i = 0; i < ja.length(); ++i) {
			JSONObject jo = ja.getJSONObject( i );
			TongquMessage tm = _toTongquMessage( jo );
			tm.setPhotolink( jo.getString( "poster" ) );
			acts.add( tm );
		}
		return ret;
	}
}
*/