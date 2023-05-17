package com.sharp.sharp.serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.sharp.entity.LoginSession;
import com.sharp.sharp.entity.OTPValidation;
import com.sharp.sharp.repository.LoginRepository;
import com.sharp.sharp.repository.OtpVaidateRepositpry;
import com.sharp.sharp.service.LoginService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.Sharp6Validation;

@Component
@Transactional
public class LoginserviceImplementation implements LoginService {
	@Autowired
	private LoginRepository loginDao;

	@Autowired
	private OtpVaidateRepositpry otpDao;

	@Override
	public LoginSession insertLogin(LoginSession login) {
		// TODO Auto-generated method stub

		LoginSession loginObj = new LoginSession();
		try {
			loginObj = loginDao.save(login);
		} catch (Exception e) {
			// TODO: handle exception
			loginObj = null;
		}
		return loginObj;
	}

	@Override
	public String deleteLogin(String userId, String logouttime) {
		try {
			LoginSession byId = loginDao.getByUserId(userId);
			System.out.println(userId);
			byId.setLogouttime(logouttime);
			LoginSession save = loginDao.save(byId);

			return Constants.SUCCESS;

		} catch (Exception e) {
			return Constants.FAILURE;
		}

	}

	@Override
	public String userRegisterOTP(String mobileNumber, String content, String otp) {
		try {
			String data = "";

			URL url = new URL("http?" + data);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line, status = null;
			while ((line = rd.readLine()) != null) {
				if (line.contains("Success")) {
					status = Constants.SUCCESS;
				} else {
					status = Constants.FAILURE;
				}
			}
			wr.close();
			rd.close();
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return Constants.FAILURE;
		}

	}

	@Override
	public OTPValidation insertOTPForValidation(OTPValidation otpValidationObj) {
		return otpDao.save(otpValidationObj);
	}

	@Override
	public OTPValidation validateOTP(String mobileNumber, String otp) {
		// TODO Auto-generated method stub
		OTPValidation validateOTP = null;
		try {
			validateOTP = otpDao.validateOTP(mobileNumber, otp);
			if (!Sharp6Validation.isEmpty(validateOTP))
				return validateOTP;

		} catch (Exception e) {
			// TODO: handle exception
			return validateOTP;
		}
		return validateOTP;

	}

	@Override
	public String remooveOtp(String mobileNumber, String otp) {
		// TODO Auto-generated method stub
		try {

			otpDao.remooveOtp(mobileNumber, otp);
			return Constants.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}
	}

}
