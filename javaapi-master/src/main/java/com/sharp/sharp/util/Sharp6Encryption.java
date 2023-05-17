package com.sharp.sharp.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Sharp6Encryption {

	private static final char[] KATCHUP = "JmIaTnoEirNhaDr9tRhanAmTen".toCharArray();
	private static final char[] FACEPRO = "MmjAaiNnt0ieHnAdRrANine10".toCharArray();
	private static final byte[] SALT = { (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, (byte) 0xde, (byte) 0x33,
			(byte) 0x10, (byte) 0x12, };

	public static String encrypt(String property, String moduleName)
			throws GeneralSecurityException, UnsupportedEncodingException {
		char[] PASSWORD = null;
		if (moduleName.equalsIgnoreCase("KATCHUP"))
			PASSWORD = KATCHUP;
		else
			PASSWORD = FACEPRO;
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
		return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
	}

	private static String base64Encode(byte[] bytes) {
		// NB: This class is internal, and you probably should use another impl
		return Base64.getEncoder().encodeToString(bytes);
	}

	public static String decrypt(String property, String moduleName) {
		try {
			char[] PASSWORD = null;
			if (moduleName.equalsIgnoreCase("KATCHUP"))
				PASSWORD = KATCHUP;
			else
				PASSWORD = FACEPRO;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
			return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
		} catch (Exception e) {
			return property;
		}

	}

	private static byte[] base64Decode(String property) {
	return Base64.getDecoder().decode(property);
	}

	/*
	 * public static void main(String [] args) { try {
	 * System.out.println("DEcrypt:::: ::: "+encrypt("A","KATCHUP"));
	 * System.out.println("DEcrypt:::: ::: "+decrypt("8yI5VhKJzzo=","KATCHUP")); }
	 * catch (Exception e) { System.out.println("exception message"+e.getMessage());
	 * e.printStackTrace(); } }
	 */
	public static void main(String[] args) throws ParseException {
		java.util.Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse("2012-07-10 14:58:00.000000");
		System.out.println(temp);
	}
}
