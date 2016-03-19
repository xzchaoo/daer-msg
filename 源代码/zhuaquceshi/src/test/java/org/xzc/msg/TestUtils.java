package org.xzc.msg;

import org.junit.Test;

import com.google.gson.Gson;

public class TestUtils {
	@Test
	public void testza() {
		Gson g = new Gson();
		Boolean b = g.fromJson( "null", Boolean.class );
		System.out.println(b);
	}
}
