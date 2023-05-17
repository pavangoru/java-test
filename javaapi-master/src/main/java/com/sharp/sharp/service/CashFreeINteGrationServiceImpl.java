package com.sharp.sharp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sharp.sharp.controller.BankAccount;
import com.sharp.sharp.controller.BenifitiaryVivo;
import com.sharp.sharp.entity.BankTransferPojo;
import com.sharp.sharp.entity.BenefitiaryDetails;
import com.sharp.sharp.entity.CashFreeKeys;
import com.sharp.sharp.entity.Payments;
import com.sharp.sharp.entity.TokenData;
import com.sharp.sharp.util.Constants;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class CashFreeINteGrationServiceImpl implements CashFreeINteGrationService {
	private static final Logger logger = Logger.getLogger(CashFreeINteGrationServiceImpl.class);

	@Value("${cash.X-Client-Id}")
	static private String clientId;

	@Value("${cash.X-Client-Secret}")
	static private String clientSecret;

	@Autowired
	private EntityManager entityManager;

	public String createOrder(Payments payment) throws IOException, InterruptedException {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");

		RequestBody body = RequestBody.create(mediaType,
				"{\"customer_details\":{\"customer_id\":\"" + payment.getUserid() + "\",\"customer_email\":\""
						+ payment.getEmailId() + "\",\"customer_phone\":\"" + payment.getUserid()
						+ "\"},\"order_id\":\"" + "order_rcptid_fact6_" + new Date().getTime() + "\",\"order_amount\":"
						+ payment.getAmount() + ",\"order_currency\":\"INR\"}");
		// https://sandbox.cashfree.com/pg/orders
		// https://api.cashfree.com/pg/orders
		Request request = new Request.Builder().url("https://api.cashfree.com/pg/orders").post(body)
				.addHeader("Accept", "application/json").addHeader("x-client-id", CashFreeKeys.PaymentGateway_clientId)
				.addHeader("x-client-secret", CashFreeKeys.PaymentGateway_clientsecret)
				.addHeader("x-api-version", "2022-09-01").addHeader("Content-Type", "application/json").build();
		System.out.println(request.body().toString());
		Response response = client.newCall(request).execute();
		String rbody = response.body().string();
		System.out.println(rbody);
		return rbody;
	}

	@Override
	public TokenData GenerateToken(String clientId, String clientSecret) {
		try {
			// String signature = Client.generateEncryptedSignature(clientId + "." +
			// Instant.now().getEpochSecond());
			URL url = new URL("https://payout-api.cashfree.com/payout/v1/authorize");
			// live-https://payout-api.cashfree.com/payout/v1/authorize
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("X-Client-Id", CashFreeKeys.PaymentGateway_clientId);
			conn.setRequestProperty("X-Client-Secret", CashFreeKeys.PaymentGateway_clientsecret);
			// conn.setRequestProperty("X-Cf-Signature", signature);
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line, status = null;
			while ((line = rd.readLine()) != null) {

				status = line;

			}
			logger.info("GenerateToken response " + status + " with timestamp "
					+ new Timestamp(System.currentTimeMillis()));
			Gson json = new Gson();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println(new Timestamp(System.currentTimeMillis()));
			TokenData organisation = json.fromJson(status, TokenData.class);

			wr.close();
			rd.close();
			return organisation;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public BenefitiaryDetails getBenefitiaryDetails(String token, String benId) {
		try {

			URL url = new URL("https://payout-api.cashfree.com/payout/v1/getBeneficiary/" + benId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestProperty("Authorization", "Bearer " + token);
			// e.g. bearer token=
			// eyJhbGciOiXXXzUxMiJ9.eyJzdWIiOiPyc2hhcm1hQHBsdW1zbGljZS5jb206OjE6OjkwIiwiZXhwIjoxNTM3MzQyNTIxLCJpYXQiOjE1MzY3Mzc3MjF9.O33zP2l_0eDNfcqSQz29jUGJC-_THYsXllrmkFnk85dNRbAw66dyEKBP5dVcFUuNTA8zhA83kk3Y41_qZYx43T

			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;

			String response = "";
			while ((output = in.readLine()) != null) {
				response = response + output;
			}

			in.close();
			// printing result from response
			System.out.println("Response:-" + response.toString());

			Gson json = new Gson();

			BenefitiaryDetails organisation = json.fromJson(response, BenefitiaryDetails.class);
			return organisation;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public String directTransferToBenefitiary(BankTransferPojo benefitiaryDetails, String token) {
		try {

			Gson gson = new Gson();
			String payload = gson.toJson(benefitiaryDetails);
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, payload);// Replace with your beneficiary details.
			Request request = new Request.Builder().url("https://payout-api.cashfree.com/payout/v1/directTransfer")
					.post(body).addHeader("Accept", "application/json").addHeader("Authorization", "Bearer " + token)
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string());

			return response.body().string();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			System.out.println("Failed successfully");
			return Constants.FAILURE;
		}

	}

	@Override
	public BankAccount addBefi(BankAccount bank) {
		try {
			TokenData result = GenerateToken(CashFreeKeys.PaymentGateway_clientId,
					CashFreeKeys.PaymentGateway_clientsecret);
			bank.setAccountNumber(bank.getBankAccount());
			bank.setBeneId(bank.getUserid());
			BenifitiaryVivo vivo = new BenifitiaryVivo();
			vivo.setBeneId(bank.getUserid());
			vivo.setBankAccount(bank.getAccountNumber());
			vivo.setIfsc(bank.getIfsc());
			if (null != result && null != result.getData()) {
				Gson gson = new Gson();
				HttpRequest request = HttpRequest.newBuilder()
						// .uri(URI.create("https://payout-api.cashfree.com/payout/v1/addBeneficiary"))

						.uri(URI.create("https://payout-api.cashfree.com/payout/v1/addBeneficiary"))
						.header("Accept", "application/json")
						.header("Authorization", "Bearer " + result.getData().getToken())// your token
						.header("Content-Type", "application/json")
						.method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(bank))).build();
				HttpResponse<String> response = HttpClient.newHttpClient().send(request,
						HttpResponse.BodyHandlers.ofString());
				System.out.println(response.body().toString());
				bank.setResponse(response.body().toString());
			}
			return bank;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	@Override
	public String getBenIdByUserId(String userid) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(BankAccount.class);
			criteria.add(Restrictions.eq("beneId", userid));
			BankAccount result = (BankAccount) criteria.uniqueResult();
			return result.getBeneId();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public BankAccount saveBenfitiary(BankAccount addBefi) {
		Session session = entityManager.unwrap(Session.class);
		try {
			session.save(addBefi);
			return addBefi;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
