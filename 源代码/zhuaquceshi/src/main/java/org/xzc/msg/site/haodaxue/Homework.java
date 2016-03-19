package org.xzc.msg.site.haodaxue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Homework {
	public final int itemid;
	public final String name;
	public final List<Stage> stages;

	public Homework(JSONObject h) {
		this.itemid = h.getInt( "itemid" );
		this.name = h.getString( "name" );

		List<Stage> stages = new ArrayList<Stage>();
		JSONArray ja = h.getJSONArray( "stages" );
		for (int i = 0; i < ja.length(); ++i) {
			JSONObject stage = ja.getJSONObject( i );
			stages.add( new Stage( stage.getString( "name" ), stage.optString( "start" ), stage.optString( "end" ) ) );
		}
		this.stages = Collections.unmodifiableList( stages );
	}

	private String toString2Cache = null;

	public String toString2() {
		if (toString2Cache != null)
			return toString2Cache;
		StringBuilder sb = new StringBuilder();
		sb.append( itemid );
		sb.append( name );
		for (Stage s : stages)
			sb.append( s.toString2() );
		toString2Cache = sb.toString();
		return toString2Cache;
	}

}
