package org.xzc.msg.fetch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.xzc.msg.domain.SJTUJWCMessage;
import org.xzc.msg.http.MyClient;
import org.xzc.msg.service.MessageType;

@Service
public class SJTUJWCMessageFetchService {
	public static final String NOTICE_URL = "http://www.jwc.sjtu.edu.cn/rss/rss_notice.aspx?SubjectID=198015&TemplateID=221009";
	private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	@Resource
	private MyClient myClient;

	public String getContent(String link) {
		//好像GBK比GB2312能显示更多
		String content = myClient.getString( link, "GBK" );
		Document doc = null;
		doc = Jsoup.parse( content );
		Elements e = doc.select( ".main_r_co_fo" );
		if (e.size() == 0) {
			//不是传统的消息页面
			return doc.select( "body" ).html();
		} else {
			return e.toString();
		}
	}

	/**
	 * TODO 教务处网站好像坏了 没法测试
	 * @return
	 */
	public List<SJTUJWCMessage> list() {
		List<SJTUJWCMessage> ret = new ArrayList<SJTUJWCMessage>();
		String content = myClient.getString( NOTICE_URL );
		Document d = Jsoup.parse( content, "", Parser.xmlParser() );
		for (Element item : d.select( "item" )) {
			String title = item.select( "title" ).text();
			String link = item.select( "link" ).text();
			String pubDate = item.select( "pubDate" ).text();
			try {
				SJTUJWCMessage n = new SJTUJWCMessage();
				n.setType( MessageType.SJTUJWC );
				n.setName( title );
				n.setStartTime( SDF.parse( pubDate ) );
				//list的消息不要contentn.setContent( getContent( link ) );
				n.setLink( link );
				ret.add( n );
			} catch (ParseException pe) {
				throw new RuntimeException( pe );
			}
		}
		return ret;
	}
}
