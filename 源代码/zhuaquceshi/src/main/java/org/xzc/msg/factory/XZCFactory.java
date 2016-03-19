package org.xzc.msg.factory;

import java.util.HashMap;
import java.util.Map;

import org.xzc.msg.pull.IMessagePullCreator;
import org.xzc.msg.pull.SimpleMessagePullCreator;
import org.xzc.msg.pull.TongquMessagePullCreator;
import org.xzc.msg.service.MessageType;

public class XZCFactory {
//	private static final MyClient mc;
//	private static final TongquService ts;
	//private static final MessageService ms;
	//private static final MongoDBService mdbs;
	//private static final UserService us;
	//private static final UserMessageService ums;
	
	private static final Map<Integer,IMessagePullCreator> msgCreatorMap;
	
	static {
//		mc=new MyClient();
//		ts = new TongquService();
		//ms = new MessageService();
		//mdbs = new MongoDBService();
		//us = new UserService();
		//ums=new UserMessageService();
		
		msgCreatorMap=new HashMap<Integer,IMessagePullCreator>();
		msgCreatorMap.put( MessageType.SIMPLE, new SimpleMessagePullCreator() );
		msgCreatorMap.put( MessageType.TONGQU, new TongquMessagePullCreator() );
		//mc.init();
//		ts.init();
		//ms.init();
		//mdbs.init();
		//us.init();
		//ums.init();
	}

//	public static UserService getUserService() {
//		return us;
//	}

/*	public static MongoDBService getMongoDBService() {
		return mdbs;
	}*/

//	public static MyClient getMyClient() {
//		return mc;
//	}

//	public static TongquService getTongquService() {
//		return ts;
//	}

	//public static MessageService getMessageService() {
	//	return ms;
	//}
	
	public static IMessagePullCreator getMessagePullCreator(int type){
		return msgCreatorMap.get( type );
	}

	//public static UserMessageService getUserMessageService() {
	//	return ums;
	//}

}