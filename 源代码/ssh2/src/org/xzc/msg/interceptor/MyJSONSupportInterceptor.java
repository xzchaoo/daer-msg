package org.xzc.msg.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;
import org.xzc.msg.exception.AuthorityException;
import org.xzc.msg.exception.GroupNotExistException;
import org.xzc.msg.exception.InvalidMessageException;
import org.xzc.msg.exception.InvalidOperationException;
import org.xzc.msg.exception.KnownException;
import org.xzc.msg.exception.MessageNotExistException;
import org.xzc.msg.exception.RepeatedMessageInGroupException;
import org.xzc.msg.exception.RepeatedUserException;
import org.xzc.msg.exception.RepeatedUserGroupException;
import org.xzc.msg.exception.RepeatedUserInGroupException;
import org.xzc.msg.exception.UserNotExistException;
import org.xzc.msg.exception.WrongUserFormatException;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

public class MyJSONSupportInterceptor extends AbstractInterceptor {
	private static final Log LOG = LogFactory.getLog( MyJSONSupportInterceptor.class );

	private static class MyPreResultListener implements PreResultListener {
		public void beforeResult(ActionInvocation ai, String code) {
			ai.setResultCode( "success" );
		}
	}

	private MyPreResultListener myPreResultListener = new MyPreResultListener();

	private static final Map<Class, Code> exceptionToCodeMap = new HashMap<Class, Code>();
	static {
		exceptionToCodeMap.put( UserNotExistException.class, Code.NO_USER );
		exceptionToCodeMap.put( MessageNotExistException.class, Code.NO_MESSAGE );
		exceptionToCodeMap.put( GroupNotExistException.class, Code.NO_GROUP );
		exceptionToCodeMap.put( RepeatedUserGroupException.class, Code.REPEATED_USER_GROUP );
		exceptionToCodeMap.put( RepeatedUserInGroupException.class, Code.REPEATED_USER_IN_GROUP );
		exceptionToCodeMap.put( InvalidOperationException.class, Code.INVALID_OPERATION );
		exceptionToCodeMap.put( RepeatedMessageInGroupException.class, Code.REPEATED_MESSAGE_IN_GROUP );
		exceptionToCodeMap.put( AuthorityException.class, Code.NO_AUTHORITY );
		exceptionToCodeMap.put( InvalidMessageException.class, Code.INVALID_MESSAGE );
		exceptionToCodeMap.put( WrongUserFormatException.class, Code.WRONG_USER_FORMAT );
		exceptionToCodeMap.put( RepeatedUserException.class, Code.NAME_EXISTS );

	}

	public String intercept(ActionInvocation ai) throws Exception {
		Object action = ai.getAction();
		if (action instanceof IMyJSONSupported) {
			ai.addPreResultListener( myPreResultListener );
			MyJSON mj = ( (IMyJSONSupported) action ).getMyJSON();
			try {
				ai.invoke();
			} catch (KnownException e) {
				mj.code( exceptionToCodeMap.get( e.getClass() ) );
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info( e );
				mj.code( Code.EXCEPTION ).msg( e.getMessage() );
			}
			return "success";
		}
		return ai.invoke();
	}
}
