package com.sharp.sharp;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import javassist.ClassClassPath;

public class Client {
	
	 @Value("${cash.free.file}")
	    private static String cashfreefile;
	 
	public static void main(String[] Key) {
		String clientIdWithEpochTimeStamp = "CF191251C97CLOE2HA3KP9U0GIQG" + "." + Instant.now().getEpochSecond();// Replace
																													// CLIENT-ID
																													// with
																													// your
																													// CLIENT-ID
		System.out.println(clientIdWithEpochTimeStamp);
		System.out.println("Signature :" + " " + generateEncryptedSignature(clientIdWithEpochTimeStamp));
	}

	public static String generateEncryptedSignature(String clientIdWithEpochTimestamp) {
		String encrytedSignature = "";
		try {
			//byte[] keyBytes = Files.readAllBytes(
				//	new File("C:/Users/Abhinav/Downloads/accountId_12986_public_key.pem").toPath());

			//File file = new File(getClass().getResource("jsonschema.json").getFile());
			File file = ResourceUtils.getFile("classpath:config/accountId_12986_public_key.pem");
			String content = new String(Files.readAllBytes(file.toPath()));
			
			/*
			 * byte[] keyBytes = Files.readAllBytes( new File(file).toPath());
			 */
			
			
			
			// Absolute Path to be replaced
			String publicKeyContent = new String(content.getBytes());
			System.out.println(publicKeyContent);
			publicKeyContent = publicKeyContent.replaceAll("[\\t\\n\\r]", "").replace("-----BEGIN PUBLIC KEY-----", "")
					.replace("-----END PUBLIC KEY-----", "");
			KeyFactory kf = KeyFactory.getInstance("RSA");
			System.out.println(publicKeyContent);
			X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
			RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
			final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			encrytedSignature = Base64.getEncoder()
					.encodeToString(cipher.doFinal(clientIdWithEpochTimestamp.getBytes()));
			System.out.println(encrytedSignature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrytedSignature;
	}
}
