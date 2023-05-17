package com.sharp.sharp.service;

import org.springframework.stereotype.Service;

import com.sharp.sharp.entity.PushNotificationRequest;

@Service
public class PushNotificationService {
	
       
    private FCMService fcmService;
    
    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }
    
    
    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            System.out.println("send notification to token");
        }
    }
   
}
