package com.sharp.sharp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.OTPValidation;

@Repository
public interface OtpVaidateRepositpry extends JpaRepository<OTPValidation, Long> {
	@Query(value = "SELECT * FROM otpvalidation WHERE mobilenumber = ?1 and otp = ?2" /* and otpvalidationstatus = N */, nativeQuery = true)
	OTPValidation validateOTP(String mobileNumber, String otp);

	@Modifying
	@Query(value = "DELETE FROM otpvalidation WHERE mobilenumber = ?1 and otp =?2", nativeQuery = true)
	void remooveOtp(String mobileNumber, String otp);

}
