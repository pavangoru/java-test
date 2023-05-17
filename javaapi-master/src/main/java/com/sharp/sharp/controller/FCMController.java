
package com.sharp.sharp.controller;

import java.io.IOException;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.sharp.entity.Contestdetails;
import com.sharp.sharp.entity.FCMNoticationVO;
import com.sharp.sharp.entity.PushNotificationResponse;
import com.sharp.sharp.service.FCMNotificationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class FCMController {

	@Autowired
	private FCMNotificationService fCMNotificationService;

	@PostMapping("firebase/notification/")
	public ResponseEntity<PushNotificationResponse> sendTokenNotification(@RequestBody FCMNoticationVO fCMNoticationVO) {
		try {
			//String[] array = fCMNoticationVO.getUserIds().toArray(new String[fCMNoticationVO.getUserIds().size()]);
			fCMNotificationService.sendFCMNotifications(fCMNoticationVO.getUserIds(), new Contestdetails(), 3);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new PushNotificationResponse(HttpStatus.OK.value(), "Error while sending Notification"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
				HttpStatus.OK);
	}

}
