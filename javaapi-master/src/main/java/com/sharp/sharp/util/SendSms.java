package com.sharp.sharp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

public class SendSms {
	public static void main(String[] args) {
		System.out.println(sendAmountCredited("8106298454",
				"Fact6: Fantasy Quiz Game: Rs. 5000.00 credited to your wallet 9393333577 on 19-03-23 linked to (Ref No 307888953670)."));
	}

	public static ResponseEntity<String> sendOtp(String mobileNo, String msg) {

		/*
		 * String url =
		 * "http://smslogin.co/v3/api.php?username=sharp6&apikey=91631cb4b578b2ec85dc&senderid=SHHARP&mobile="
		 * + mobileNo + "&message=" + msg;
		 */
		String uri = "https://smslogin.co/v3/api.php?username=fact6&apikey=78b6c412d471cae58476&senderid=FACTSX&mobile="
				+ mobileNo + "&message=" + msg;
		Gson json = new Gson();
		String payload = json.toJson(uri);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		System.out.println("mobile number : " + mobileNo);

		HttpEntity<String> entity = new HttpEntity<String>(payload, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		System.out.println(exchange.getBody().toString());
		return exchange;
	}

	// To send SMS
	public static String sendSms(String mobileNo, String msg) {
		try {
			String data = "";
			data += "apikey=91631cb4b578b2ec85dc";
			data += "&senderid=SHHARP";
			data += "&mobile=" + mobileNo;

			data += "&message=" + msg;
			URL url = new URL("http://smslogin.co/v3/api.php?username=sharp6&" + data);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setAllowUserInteraction(false);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			// wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line, status = null;
			while ((line = rd.readLine()) != null) {
				if (line.contains("campid")) {
					status = "success";
				} else {
					status = "fail";
				}
			}
			wr.close();
			rd.close();
			return status;
		} catch (Exception e) {

			return "error";

		}
	}
	
	public static ResponseEntity<String> sendAmountCredited(String mobileNo, String msg) {

		/*
		 * String url =
		 * "http://smslogin.co/v3/api.php?username=sharp6&apikey=91631cb4b578b2ec85dc&senderid=SHHARP&mobile="
		 * + mobileNo + "&message=" + msg;
		 */
		String uri = "https://smslogin.co/v3/api.php?username=fact6&apikey=78b6c412d471cae58476&senderid=FACTSX&mobile="
				+ mobileNo + "&message=" + msg;
		Gson json = new Gson();
		String payload = json.toJson(uri);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		System.out.println("mobile number : " + mobileNo);

		HttpEntity<String> entity = new HttpEntity<String>(payload, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		System.out.println(exchange.getBody().toString());
		return exchange;
	}


	
}
