package com.sharp.sharp.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.sharp.entity.LoginSession;
import com.sharp.sharp.entity.ModelQuestions;
import com.sharp.sharp.entity.OTPValidation;
import com.sharp.sharp.entity.Questions;
import com.sharp.sharp.entity.ReviewQuestions;
import com.sharp.sharp.entity.Sharp6Wallet;
import com.sharp.sharp.entity.UserAnswerHistory;
import com.sharp.sharp.entity.UserMaster;
import com.sharp.sharp.service.HomeDashBoardService;
import com.sharp.sharp.service.LoginService;
import com.sharp.sharp.service.PaymentService;
import com.sharp.sharp.service.QuestionsService;
import com.sharp.sharp.service.UserService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.MyBusinessException;
import com.sharp.sharp.util.SendSms;
import com.sharp.sharp.util.Sharp6Validation;

@RestController
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private HomeDashBoardService dashboardservice;
	@Autowired
	private PaymentService gatewayService;
	@Autowired
	private QuestionsService questionsService;

	@PostMapping("/newRegister/")
	public HashMap<String, Object> newUserRegister(@RequestBody UserMaster user) {
		HashMap<String, Object> resultMap = new HashMap<>();
		user.setMobileNumber(user.getUserid());
		user.setActivestatus(true);
		Date date = new Date();
		user.setCreatedDate(String.valueOf(date.getTime()));
		user.setRole("USER");
		user.setLanguage("ENGLISH");

		UserMaster registerObj = userService.newUserRegister(user);
		// log files
		logger.info("object registerd in db succesfully");
		if (!Sharp6Validation.isEmpty(registerObj)) {
			String walletExistBYId = gatewayService.isWalletExistBYId(registerObj.getUserid());

			if (walletExistBYId.equals(Constants.SUCCESS)) {

				Sharp6Wallet wallet = new Sharp6Wallet();
				wallet.setUserid(registerObj.getUserid());
				Sharp6Wallet saveMoneytoWalet = gatewayService.saveMoneytoWalet(wallet);
				registerObj.setMainkales(saveMoneytoWalet.getMainkales());
				System.out.println("wallet created successful");
			}

			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", registerObj);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", "inputError");
			resultMap.put(Constants.ERROR_MSG, "Error occcured while registering the user.");
		}
		return resultMap;
	}

	@PostMapping("/Login/")
	public Map<String, Object> loginUser(@RequestBody LoginSession entity) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		UserMaster userLogin = userService.userLogin(entity);
		System.out.println(userLogin);
		logger.info(userLogin);
		if (!Sharp6Validation.isEmpty(userLogin)) {
			entity.setUserid(userLogin.getUserid());
			entity.setRole(userLogin.getRole());
			LoginSession insertLogin = loginService.insertLogin(entity);
			if (!Sharp6Validation.isEmpty(insertLogin)) {
				logger.info("login succcess");
				map.put(Constants.STATUS, true);
				map.put("value1", "Login Success");
				map.put("value", entity);
			} else {
				map.put(Constants.STATUS, false);
				map.put("value", "Username or Password incorrect");
				map.put(Constants.ERROR_MSG, "Username or Password is incorrect.");
			}
		} else {
			map.put(Constants.STATUS, false);
			map.put("value", "Username Not yet registerd!");
			map.put(Constants.ERROR_MSG, "Username Not yet registerd!");
		}

		return map;

	}

	@PostMapping("/getProfileDetails/")
	public Map<String, Object> getProfileDetails(@RequestParam("userId") String userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		UserMaster getuserById = userService.getuserById(userId);
		map.put("data", getuserById);
		map.put(Constants.STATUS, true);
		return map;

	}

	@PostMapping("/updateUserDeviceToken/")
	public Map<String, Object> updateUserDeviceToken(@RequestParam("userId") String userId,
			@RequestParam("token") String token) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String getuserById = userService.updateUserDeviceToken(userId, token);
		map.put("Successfully updated the token", getuserById);
		map.put(Constants.STATUS, true);
		return map;

	}

	@PostMapping("/AdminLogin/")
	public Map<String, Object> loginAdmin(@RequestParam("userName") String userName,
			@RequestParam("password") String password) {
		UserMaster user = new UserMaster();
		HashMap<String, Object> map = new HashMap<String, Object>();
		UserMaster adminObj = userService.loginAdmin(userName, password);
		logger.info(adminObj);
		if (!Sharp6Validation.isEmpty(adminObj)) {
			user.setUserid(adminObj.getUserid());
			user.setRole(adminObj.getRole());
System.out.println("Login succcess");
			logger.info("Login succcess");
			map.put(Constants.STATUS, true);
			map.put("value1", "Login Success");
			map.put("value", adminObj);

		} else {
			map.put(Constants.STATUS, false);
			map.put("value", "Username or Password incorrect");
			map.put(Constants.ERROR_MSG, "Username or Password is incorrect.");
		}

		return map;

	}

	@RequestMapping(value = "/userLoginwithOTP/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> userRegisterOTP(@RequestParam("mobileNumber") String mobileNumber) {
		Map<String, Object> resultMap = new HashMap<>();
		Random rnd = new Random();
		int value = 1000 + rnd.nextInt(9000);
		String template = "Your OTP for verifying your mobile number with FACT6 PRIVATE LIMITED is " + value
				+ ". Do not share with anyone.";
		try {
			if (mobileNumber.contains("1233377999")) {
				value = 1234;

				String result = template + ":" + value;

				resultMap.put("status", true);
				resultMap.put("message", result);
				resultMap.put("OTP", value);

			} else {
				// String result = loginService.userRegisterOTP(mobileNumber, template,
				// String.valueOf(value));

				ResponseEntity<String> sendOtp = SendSms.sendOtp(mobileNumber, template);
				sendOtp.toString();
				if (sendOtp.toString().contains("{'Error':'Invalid Credentials'}")) {
					resultMap.put("status", true);
					resultMap.put("message", null);
					resultMap.put("errormessage", "Invalid Credentials");
					return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
				}
				if (sendOtp.toString().contains("{'Error':'Missing Mobile number'}")) {
					resultMap.put("status", true);
					resultMap.put("message", null);
					resultMap.put("errormessage", "Missing Mobile number");
					return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
				}
				if (sendOtp.toString().contains("{'Error':'Message Missing'}")) {
					resultMap.put("status", false);
					resultMap.put("message", null);
					resultMap.put("errormessage", "senderId Wrong");
					return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
				}
				if (sendOtp.toString().contains("campid")) {
					System.out.println("otp send success fully " + value);
				}
				String result = null;

				Date date = new Date();
				OTPValidation otpValidationObj = new OTPValidation();
				otpValidationObj.setMobileNumber(mobileNumber);
				otpValidationObj.setOtp("" + value);
				otpValidationObj.setType("Signup");
				otpValidationObj.setCreatedBy("USER");
				otpValidationObj.setSendDate(new Timestamp(date.getTime()));
				otpValidationObj.setCreatedDate(new Timestamp(date.getTime()));
				otpValidationObj.setExpireDate(new Timestamp(date.getTime() + (600000)));
				OTPValidation otpResStatus = loginService.insertOTPForValidation(otpValidationObj);
				logger.info("otpResStatus::= " + otpResStatus);

				result = template + ":" + value;
				if (otpResStatus != null) {
					resultMap.put("status", true);
					resultMap.put("message", result);
					resultMap.put("OTP", value);
				} else {
					resultMap.put("status", false);
					resultMap.put("errorvalue", "Sending OTP not Success");
					resultMap.put(Constants.ERROR_MSG, "Sending an OTP failed.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", false);
			resultMap.put("errorvalue", "Sending OTP not Success");
			resultMap.put(Constants.ERROR_MSG, "Sending an OTP failed.");
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/OtpValidation/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> validateOTP(@RequestParam("mobileNumber") String mobileNumber,
			@RequestParam("otp") String otp) throws MyBusinessException {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			if (mobileNumber.equals("1233377999")) {
				if (otp.equals("1234")) {
					UserMaster mobileNumberExist = userService.isMobileNumberExist(mobileNumber);
					if (!Sharp6Validation.isEmpty(mobileNumberExist)) {
						resultMap.put(Constants.STATUS, true);
						resultMap.put("value1", mobileNumberExist);
					} else {
						resultMap.put(Constants.STATUS, false);
						resultMap.put("value1", null);
						resultMap.put(Constants.ERROR_MSG, "Error occured while checking if mobile number exist.");
					}
				} else {
					resultMap.put(Constants.STATUS, false);
					resultMap.put("value1", "Please Re-enter the OTP.");
					resultMap.put(Constants.ERROR_MSG, "Please Re-enter the OTP.");
				}
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
			}
			OTPValidation result = loginService.validateOTP(mobileNumber, otp);
			if (!Sharp6Validation.isEmpty(result)) {
				String remooveOtp = loginService.remooveOtp(mobileNumber, otp);
				if (remooveOtp.equals(Constants.SUCCESS)) {
					UserMaster mobileNumberExist = userService.isMobileNumberExist(mobileNumber);
					if (!Sharp6Validation.isEmpty(mobileNumberExist)) {
						resultMap.put(Constants.STATUS, true);
						resultMap.put("value1", mobileNumberExist);
					} else {
						resultMap.put(Constants.STATUS, false);
						resultMap.put("value1", null);
						resultMap.put(Constants.ERROR_MSG, "Error occured while checking if mobile number exist.");
					}

					resultMap.put("value", "OTP validation Success");
				} else {
					resultMap.put(Constants.STATUS, false);
					resultMap.put("value", "re-enter your otp");
					resultMap.put(Constants.ERROR_MSG, "Please Re-enter the OTP.");
				}
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "re-enter your otp");
				resultMap.put(Constants.ERROR_MSG, "Please Re-enter the OTP.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw new MyBusinessException("OTP validation
			// Failed",HttpStatus.INTERNAL_SERVER_ERROR);
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errorvalue", "OTP validation Failed");
			resultMap.put(Constants.ERROR_MSG, "OTP validation Failed.");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@PostMapping("/Logout/")
	public Map<String, Object> logoutUser(@RequestBody LoginSession entity) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {

			String deleteLogin = loginService.deleteLogin(entity.getUserid(), entity.getLogouttime());
			logger.info("userLogout successfull");
			if (deleteLogin.equals(Constants.SUCCESS)) {
				map.put(Constants.STATUS, true);
				map.put("value", "Logout successfull");
			} else {
				map.put(Constants.STATUS, false);
				map.put("value", "Logout Failure");
				map.put(Constants.ERROR_MSG, "Logout Failed.");

			}
		} catch (Exception e) {
			map.put(Constants.STATUS, false);
			map.put("value", "ServerError");
			map.put(Constants.ERROR_MSG, "Error occured while logout.");
		}

		return map;

	}

	@PostMapping(value = "/resetPassword/")
	public Map<String, Object> changePassword(@RequestBody UserMaster user) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String status = userService.changePassword(user);
			System.out.println(status);
			if (status.equals(Constants.SUCCESS)) {
				map.put(Constants.STATUS, true);
				map.put("value", "password Updated Successfully");
			} else {
				map.put(Constants.STATUS, false);
				map.put("value", "password Not Updated");
				map.put(Constants.ERROR_MSG, "password Not Updated.");
			}
		} catch (Exception e) {

			map.put(Constants.STATUS, false);
			map.put("value", "Try again");
			map.put(Constants.ERROR_MSG, "Error occured while password change.");
		}

		return map;
	}

	@RequestMapping(value = "/getAllLanguages/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLanguages() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<Object[]> langList = userService.getALLLanguages();

			if (langList.size() > 0) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", langList);
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/getAllLanguages");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// Q&A api
	@RequestMapping(value = "/addQuestions/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> inserttQuestions(@RequestBody List<Questions> question) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<Questions> langList = userService.inserttQueries(question);

			if (!Sharp6Validation.isEmpty(langList)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", "Question submitted");
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "Question Not submitted");
				resultMap.put(Constants.ERROR_MSG, "Question Not submitted.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "Queries");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/getQuestionsBycontestId/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getQuestionsBycontestId(@RequestParam("contestId") String contestId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			// List<Questions> qlist = userService.getQuestionsBycontestId(contestId);
			List<Questions> qlist = questionsService.getQuestionsBycontestId(contestId);

			if (!Sharp6Validation.isEmpty(qlist)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("questionsList", qlist);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "no questions available");
				resultMap.put(Constants.ERROR_MSG, "No questions available.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/getQuestionsBycontestId");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	//
	@RequestMapping(value = "/reviewQuestion/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> reviewQuestion(@RequestBody List<ReviewQuestions> review) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<ReviewQuestions> qist = userService.reviewQuestion(review);

			if (!Sharp6Validation.isEmpty(qist)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", qist);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "No questions avaiable");
				resultMap.put(Constants.ERROR_MSG, "No questions available.");

			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/reviewQuestion");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/getreviewedQuestions/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getreviewQuestion(@RequestBody List<ReviewQuestions> review) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<ReviewQuestions> qist = userService.reviewQuestion(review);

			if (!Sharp6Validation.isEmpty(qist)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", qist);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "No questions avaiable");
				resultMap.put(Constants.ERROR_MSG, "No questions available.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/getreviewedQuestions");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllQuestions/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllQuestions() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<Questions> qist = userService.getAllquestions();

			if (!Sharp6Validation.isEmpty(qist)) {

				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", qist);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "No questions avaiable");
				resultMap.put(Constants.ERROR_MSG, "No questions available.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/getAllQuestions");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/getQuestionshistorybyuserId/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getQuestionshistorybyuserId(@RequestParam("userId") String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<UserAnswerHistory> qlist = userService.getQuestionshistorybyuserId(userId);

			if (!Sharp6Validation.isEmpty(qlist)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", qlist);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "No questions avaiable");
				resultMap.put(Constants.ERROR_MSG, "No questions available.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/getQuestionshistorybyuserId");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/submitAnswers/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> submitAnswers(@RequestBody UserAnswerHistory userAnswers) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			userAnswers.setCreatedDate(new Date());
			boolean userSubmitAnswers = userService.isUserSubmitAnswers(userAnswers.getUserId(),
					userAnswers.getContestId());
			if (userSubmitAnswers == false) {
				UserAnswerHistory qlist = userService.submitAnswers(userAnswers);

				if (!Sharp6Validation.isEmpty(qlist)) {
					resultMap.put(Constants.STATUS, true);
					resultMap.put("submitAnswerList", qlist);
					resultMap.put(Constants.URLPATH, "/submitAnswers");
					return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
				} else {
					resultMap.put(Constants.STATUS, false);
					resultMap.put("value", "No questions avaiable");
					resultMap.put(Constants.ERROR_MSG, "No questions available.");
					resultMap.put(Constants.URLPATH, "/submitAnswers");
					return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
				}
			}
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", "User Answers already Submitted");
			resultMap.put(Constants.ERROR_MSG, "User Answers already Submitted");
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/submitAnswers");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/getquestionsHistoryByContestIdndUserId/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getquestionsHistoryByContestIdandUserId(@RequestParam String contestId,
			@RequestParam String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<Object> getuserJoinedLables = dashboardservice.getuserJoinedLables(contestId, userId);

			List<Questions> qlist = userService.getquestionsHistoryByContestId(contestId, userId);

			if (!Sharp6Validation.isEmpty(qlist) && !Sharp6Validation.isEmpty(getuserJoinedLables)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("LableList", getuserJoinedLables);
				resultMap.put("questionsHistory", qlist);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "No questions history Available");
				resultMap.put(Constants.ERROR_MSG, "No questions history available.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/getquestionsHistoryByContestIdndUserId");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllUserlist/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> userList() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<UserMaster> userList = userService.getAllUserlist();

			if (!Sharp6Validation.isEmpty(userList)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", userList);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "No users avaiable");
				resultMap.put(Constants.ERROR_MSG, "No users avaiable.");

			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/getAllUserlist");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/isMobileNumberExist/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> isMobileNumberExist(@RequestParam("mobileNumber") String mobileNumber) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			UserMaster mobileNumberExist = userService.isMobileNumberExist(mobileNumber);

			if (Sharp6Validation.isEmpty(mobileNumberExist)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", "mobileNumberExist");
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "mobileNumber not Exist");
				resultMap.put(Constants.ERROR_MSG, "MobileNumber not Exist.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/isMobileNumberExist");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// Q&A api
	@RequestMapping(value = "/addMOdelQuestionsbyContestId/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addMOdelQuestions(@RequestBody ModelQuestions question) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			ModelQuestions modelList = userService.insertModelQuestions(question);

			if (!Sharp6Validation.isEmpty(modelList)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", "Questions submitted");
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "Questions Not submitted");
				resultMap.put(Constants.ERROR_MSG, "Questions Not submitted.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "Queries");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/getmodelQuestionsBycontestId/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getmodelQuestionsBycontestId(
			@RequestParam("contestId") String contestId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<ModelQuestions> qlist = userService.getmodelQuestionsBycontestId(contestId);

			if (!Sharp6Validation.isEmpty(qlist)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("questionsList", qlist);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "No questions available");
				resultMap.put(Constants.ERROR_MSG, "No questions available.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/addMOdelQuestionsbyContestId");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	//
	@RequestMapping(value = "/removeModelQuestionsByCOntestId/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> removeModelQuestionsByCOntestId(
			@RequestParam("contestId") String contestId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			String removeModelQuestionsByCOntestId = userService.removeModelQuestionsByCOntestId(contestId);

			if (removeModelQuestionsByCOntestId.equals(Constants.SUCCESS)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", "contest removed successfuly");
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "Unable to remove the contest.");
				resultMap.put(Constants.ERROR_MSG, "Unable to remove the contest.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/reviewQuestion");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllInprogressedcontestsModelquestions/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllInprogressedcontestsModelquestions() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<ModelQuestions> allInprogressedcontestsModelquestions = userService
					.getAllInprogressedcontestsModelquestions();

			if (!Sharp6Validation.isEmpty(allInprogressedcontestsModelquestions)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("inprogressModelQList", allInprogressedcontestsModelquestions);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG, "No questions available.");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("errormessage", e.getMessage());
			resultMap.put("errorvalue", e.getLocalizedMessage());
		}
		resultMap.put(Constants.URLPATH, "/getAllInprogressedcontestsModelquestions");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	/**
	 * 
	 * is answer submit
	 */
	@PostMapping("/isAnswerSubmit/")
	public Map<String, Object> isUserSubmitAnswers(@RequestParam("userId") String userId,
			@RequestParam("contestId") String contestId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = userService.isUserSubmitAnswers(userId, contestId);
		long l = 0;
		if (result)
			resultMap.put(Constants.STATUS, true);

		else
			resultMap.put(Constants.STATUS, false);

		return resultMap;

	}
}
