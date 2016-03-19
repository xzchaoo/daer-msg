package org.xzc.msg.utils;

import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.bson.Document;
import org.xzc.msg.domain.IdAndName;
import org.xzc.msg.domain.Message3;
import org.xzc.msg.domain.User;

public class Utils {
	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	private static final BeanUtilsBean2 b;
	static {
		b = new BeanUtilsBean2();
		b.getConvertUtils().register( new DateTimeConverter() {
			{
				this.setDefaultValue( null );
			}

			protected Class<?> getDefaultType() {
				return Date.class;
			}
		}, Date.class );
	}

	public static void copy(Object from, Object to) {
		try {
			b.copyProperties( to, from );
		} catch (Exception e) {
			throwAsRuntimeException( e );
		}
	}

	public static Map<String, Object> describe(Object bean) {
		try {
			Map<String, Object> map = b.getPropertyUtils().describe( bean );
			map.remove( "class" );
			return map;
		} catch (Exception e) {
			throwAsRuntimeException( e );
		}
		return null;
	}

	public static void populate(Object bean, Map<String, Object> map) {
		try {
			b.populate( bean, map );
		} catch (Exception e) {
			throwAsRuntimeException( e );
		}
	}

	public static void throwAsRuntimeException(Exception e) {
		if (e instanceof RuntimeException)
			throw (RuntimeException) e;
		throw new RuntimeException( e );
	}

	public static IdAndName toIdAndName(Document d) {
		IdAndName ian = new IdAndName();
		ian.id = d.getInteger( "_id" );
		ian.name = d.getString( "name" );
		return ian;
	}

	public static void fromDocument(Document d, Object obj) {
		if (d.containsKey( "_id" )) {
			d.append( "id", d.get( "_id" ) );
		}
		populate( obj, d );
	}

	public static Document toDocument(Object obj) {
		if (obj == null)
			return null;
		Map<String, Object> map = describe( obj );
		map.remove( "id" );
		Document d = new Document( map );
		return d;
	}

}
