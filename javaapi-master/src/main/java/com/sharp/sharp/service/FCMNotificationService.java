package com.sharp.sharp.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sharp.sharp.entity.Contestdetails;
import com.sharp.sharp.repository.UserRepository;
import com.sharp.sharp.util.Constants;

@Service
public class FCMNotificationService {

	@Value("${fcm.notification-body}")
	private String notificationBody;

	@Value("${fcm.notification-tittle}")
	private String notificationTittle;

	@Value("${fcm.data-body}")
	private String dataBody;

	@Value("${fcm.data-tittle}")
	private String dataTittle;

	@Value("${fcm.datakey1}")
	private String dataKey1;

	@Value("$fcm.datakey2}")
	private String dataKey2;

	@Value("${fcm.authorization-key}")
	private String authorizationKey;

	@Autowired
	private UserRepository userdao;

	// public JSONObject sendFCMNotifications(String [] array) throws IOException,
	// JSONException {
	public JSONObject sendFCMNotifications(List<String> list, Contestdetails cObj, long difference)
			throws IOException, JSONException {
		// String[] array = null;
		String query = Constants.FCM_URL;
		JSONObject jsonObject = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject parent = new JSONObject();
		JSONObject notification = new JSONObject();
		ArrayList<String> tokenIds = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			// array[i] = list.get(i);
			String userToken = userdao.getTokenByUserId(list.get(i));
			tokenIds.add(userToken);
		}
		String[] array = tokenIds.toArray(new String[tokenIds.size()]);

		notification.put("body", "Your Game " + cObj.getContestname1() + " is going to start in " + difference + "m");
		notification.put("title", "Fact6 - " + cObj.getShowname());
		data.put("body", "Your Game " + cObj.getContestname1() + " is going to start  in " + difference + "m");
		data.put("title", dataTittle);
		data.put("key_1", dataKey1);
		data.put("key_2", dataKey2);
		String authorization = "key=" + authorizationKey;
		parent.put("registration_ids", array);
		parent.put("notification", notification);
		parent.put("data", data);
		parent.put("collapse_key", "sharp");
		System.out.println("JSON OBJ: " + parent.toString());
		try {
			URL url = new URL(query);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", authorization);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Length", "0");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			OutputStream os = conn.getOutputStream();
			os.write(parent.toString().getBytes("UTF-8"));
			os.close();

			// read the response
			InputStream in = new BufferedInputStream(conn.getInputStream());
			String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
			jsonObject = new JSONObject(result);

			in.close();
			conn.disconnect();
		} catch (Exception e) {
			e.getMessage();
		}
		System.out.println("jsonObject return : " + jsonObject.toString());
		return jsonObject;
	}

}
