package com.sharp.sharp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.sharp.entity.LoginSession;
import com.sharp.sharp.entity.OTPValidation;

@Service
@Transactional
public interface LoginService {
	LoginSession insertLogin(LoginSession login);

	String deleteLogin(String userId, String logouttime);

	String userRegisterOTP(String mobileNumber, String template, String valueOf);

	OTPValidation insertOTPForValidation(OTPValidation otpValidationObj);

	OTPValidation validateOTP(String mobileNumber, String otp);

	String remooveOtp(String mobileNumber, String otp);

}
