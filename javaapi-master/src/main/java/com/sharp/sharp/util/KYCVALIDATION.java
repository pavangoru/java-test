package com.sharp.sharp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.sharp.sharp.entity.BankDetailsVivo;
import com.sharp.sharp.entity.PanCard;
import com.sharp.sharp.entity.PanKycRequestEntity;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KYCVALIDATION {

	public static String panNumberValidation(PanCard panDetails) throws IOException {
		String upperCase = panDetails.getPanNumber().toUpperCase();
		char c[] = upperCase.toCharArray();

		if (c.length != 10) {
			return Constants.FAILURE;
		}
		for (int i = 0; i < 10; i++) {
			if ((i >= 0 && i <= 4) && (c[i] < 'A' || c[i] > 'Z')) {
				return Constants.FAILURE;
			} else if ((i > 4 && i <= 8) && (c[i] < 48 || c[i] > 57)) {
				return Constants.FAILURE;
			} else if (i == 9 && (c[i] < 'A' || c[i] > 'Z')) {
				return Constants.FAILURE;
			}
		}
		Gson gson = new Gson();
		PanKycRequestEntity panKycRequestEntity = new PanKycRequestEntity();
		panKycRequestEntity.setName(panDetails.getFullName());
		panKycRequestEntity.setPan(panDetails.getPanNumber().toUpperCase());

		String payload = gson.toJson(panKycRequestEntity);
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, payload);// Replace with your beneficiary details.

		Request request = new Request.Builder().url("https://api.cashfree.com/verification/pan").post(body)
				.addHeader("accept", "application/json").addHeader("x-api-version", "2022-10-26")
				.addHeader("x-client-id", "CF201201CGF8S972JDPPVJJL92PG")
				.addHeader("x-client-secret", "4f44ef1fa247a2d17604f0eaa66f39449167a0b3")
				.addHeader("content-type", "application/json").build();

		Response response = client.newCall(request).execute();
		String rbody = response.body().string();
		System.out.println(rbody);

		return rbody;

	}

	public static BankDetailsVivo bankIFSCValidation(String ifsc) {

		try {

			URL url = new URL("https://ifsc.razorpay.com/" + ifsc);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			boolean status = false;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				if (output.toUpperCase().contains(ifsc.toUpperCase())) {
					status = true;
					Gson gson = new Gson();
					BankDetailsVivo fromJson = gson.fromJson(output.toString(), BankDetailsVivo.class);
					return fromJson;
				} else
					status = false;
				return null;
			}

			conn.disconnect();
			return null;
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return null;

		} catch (IOException e) {

			e.printStackTrace();

			return null;
		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}

	}
}
