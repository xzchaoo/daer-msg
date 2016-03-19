package org.xzc.msg.site.haodaxue;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
	
	static final byte[] RAW_KEYS = { 14, -74, 79, 26, 2, -3, 88, 121, 25, -83, 84, 52, 13, 61, -101, 1, 42, 25, 19, 70,
			124, -95, -50, 44 };

	public static String encryptText(String paramString) {
		SecretKeySpec localSecretKeySpec;
		try {
			localSecretKeySpec = new SecretKeySpec( RAW_KEYS, "DESede" );
			Cipher localCipher = Cipher.getInstance( "DESede" );
			localCipher.init( 1, localSecretKeySpec );
			paramString = new String( Base64.getEncoder().encode( localCipher.doFinal( paramString.getBytes() ) ) );
			return paramString;
		} catch (Exception e) {
		}
		return "";
	}

	public static String setHashMD5List(List<String> paramList) {
		String str = "";
		for (int i = 0; i < paramList.size(); ++i) {
			//str += hashMD5( paramList.get( i ) );
			str+=paramList.get( i );
		}
		return hashMD5( str );
	}

	public static String hashMD5(String text) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance( "md5" );
			byte[] datas = md.digest( text.getBytes( "utf-8" ) );
			StringBuilder sb = new StringBuilder();
			for (byte b : datas) {
				int i=b;
				if (i < 0)
					i += 256;
				if (i < 16) {
					sb.append( '0' );
				}
				sb.append( Integer.toHexString( i ) );
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
