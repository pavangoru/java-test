package com.sharp.sharp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sharp.sharp.awss3.AmazonImage;
import com.sharp.sharp.awss3.AmazonS3ImageService;
import com.sharp.sharp.entity.BankDetailsVivo;
import com.sharp.sharp.entity.BankNames;
import com.sharp.sharp.entity.PanCard;
import com.sharp.sharp.entity.States;
import com.sharp.sharp.entity.UserMaster;
import com.sharp.sharp.service.CashFreeINteGrationService;
import com.sharp.sharp.service.KycService;
import com.sharp.sharp.service.UserService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.KYCVALIDATION;
import com.sharp.sharp.util.Sharp6Validation;

@RestController
@RequestMapping("/kyc")
public class KYCController {
	private static final Logger logger = Logger.getLogger(KYCController.class);
	@Value("${s3.endpointUrl}")
	private String url;

	@Autowired
	private KycService kycService;;
	@Autowired
	private AmazonS3ImageService s3imageService;

	@Autowired
	private UserService userservice;

	@Autowired
	private CashFreeINteGrationService cashFreeService;

	@PostMapping("/validatePanNumber/")
	public HashMap<String, Object> panNumberKyc(@RequestParam("panNumber") String panNumber,
			@RequestParam("province") String province, @RequestParam("userid") String userid,
			@RequestParam("fullName") String fullName, @RequestPart("panIMage") MultipartFile panIMage) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String panNumberValidation;

		PanCard pancardByUserId = kycService.getPancardByUserId(userid, panNumber);

		if (Sharp6Validation.isEmpty(pancardByUserId)) {
			try {
				PanCard pancard = new PanCard();
				pancard.setId(panNumber);
				pancard.setPanNumber(panNumber);
				pancard.setUserid(userid);
				pancard.setFullName(fullName);
				pancard.setProvince(province);
				pancard.setValidationFlag(1);
				panNumberValidation = KYCVALIDATION.panNumberValidation(pancard);
				if (panNumberValidation.contains("PAN verified successfully")) {
					pancard.setValidationFlag(2);
					AmazonImage list = s3imageService.insertImages(panIMage, panNumber);
					pancard.setPanIMage(list.getImageUrl());

					String savePancardDetails = kycService.savePancardDetails(pancard);
					if (savePancardDetails.equals(Constants.SUCCESS)) {
						resultMap.put(Constants.STATUS, true);
						resultMap.put("panResult", pancard);
					} else {
						resultMap.put(Constants.STATUS, false);
						resultMap.put(Constants.ERROR_MSG, "Internal server Error- data not save in db.");
					}

				} else if (panNumberValidation.contains("validation_error")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_VALUE, "x-client-id is missing in the request :400,validation_error");

				} else if (panNumberValidation.contains("authentication_failed")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_VALUE,
							"Invalid clientId and clientSecret combination :401,authentication_error");

				} else if (panNumberValidation.contains("IP not whitelisted")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_VALUE, "IP not whitelisted :403,authentication_error");

				} else if (panNumberValidation.contains("insufficient_balance")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_VALUE,
							"Insufficient balance to process this request :422,insufficient_balance");
				} else if (panNumberValidation.contains("pan is missing in the request.")
						|| panNumberValidation.contains("internal_error.")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_VALUE,
							"something went wrong, please try after some time :500,insufficient_balance");
				} else if (panNumberValidation.contains(Constants.FAILURE)) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_VALUE, "Invalid PanNumber");
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_VALUE, e1.getMessage());
			}

		} else {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("panresult", "pan card already exists");

		}
		/*
		 * PanCard pancard = new PanCard(); pancard.setId(panNumber);
		 * pancard.setPanNumber(panNumber); pancard.setUserid(userid);
		 * pancard.setFullName(fullName); pancard.setProvince(province);
		 * pancard.setValidationFlag(1); AmazonImage list =
		 * s3imageService.insertImages(panIMage, panNumber);
		 * pancard.setPanIMage(list.getImageUrl());
		 * 
		 * String savePancardDetails = kycService.savePancardDetails(pancard); if
		 * (savePancardDetails.equals(Constants.SUCCESS)) {
		 * resultMap.put(Constants.STATUS, true); resultMap.put("panResult", pancard); }
		 * else { resultMap.put(Constants.STATUS, false);
		 * resultMap.put(Constants.ERROR_MSG,
		 * "Internal server Error- data not save in db."); }
		 */
		return resultMap;

	}

	@PostMapping("/getpanNumberDetailsByUserid/")
	public HashMap<String, Object> getpanNumberDetails(@RequestParam("userid") String userid) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		PanCard pancardByUserId = kycService.getPancardByUserId(userid, null);
		if (!Sharp6Validation.isEmpty(pancardByUserId)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("panResult", pancardByUserId);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "PAN details does not exist for the user.");
		}
		return resultMap;
	}

	@GetMapping("/getAllPancardsList/")
	public HashMap<String, Object> getAllPancards() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<PanCard> panList = kycService.getAllPancards();
		if (!Sharp6Validation.isEmpty(panList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("panResult", panList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "No pancards available");
		}
		return resultMap;
	}

	@GetMapping("/getBankNamesList/")
	public HashMap<String, Object> getBankNamesList() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<BankNames> bankNames = kycService.getBankNamesList();
		if (!Sharp6Validation.isEmpty(bankNames)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("BankNameList", bankNames);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Error occured while fetching the Bank Names List.");
		}
		return resultMap;
	}

	@GetMapping("/getAllProvincesList/")
	public HashMap<String, Object> getAllProvinces() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<States> states = kycService.getAllProvinces();
		if (!Sharp6Validation.isEmpty(states)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("ProvinceList", states);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Error occured while fetching the Provinces List.");
		}
		return resultMap;
	}

	@PostMapping("/validateBankIfsc/")
	public HashMap<String, Object> bankAccountKYC(@RequestBody BankAccount bankaccount) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			bankaccount.setBankAccount(bankaccount.getAccountNumber());

			BankDetailsVivo bankIFSCValidation = KYCVALIDATION.bankIFSCValidation(bankaccount.getIfsc());
			logger.info("bankIFSCValidation response" + bankIFSCValidation);
			if (!Sharp6Validation.isEmpty(bankIFSCValidation)) {
				bankaccount.setBeneId("Benefi" + bankaccount.getUserid());
				bankaccount.setAddress1(bankIFSCValidation.getADDRESS());
				bankaccount.setState(bankIFSCValidation.getSTATE());
				bankaccount.setCity(bankIFSCValidation.getCITY());

				// BankAccount isbenfiExist = kycService.isbenfiExist(bankaccount.getBeneId());
				// if (Sharp6Validation.isEmpty(isbenfiExist)) {
				bankaccount.setValidationFlag(1);
				BankAccount saveBankwithIfsc = kycService.saveBankwithIfsc(bankaccount);
				if (!Sharp6Validation.isEmpty(saveBankwithIfsc)) {
					resultMap.put(Constants.STATUS, true);
					resultMap.put("accountValidation", bankaccount);
				} else {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Internal server error-Bank data not saved in db.");
				}
				/*
				 * // BankAccount addBefi = cashFreeService.addBefi(bankaccount);
				 * logger.info(" cashFreeService response" + addBefi.getResponse()); if (null !=
				 * addBefi && null != addBefi.getResponse() &&
				 * addBefi.getResponse().contains("success")) { BankAccount saveBankwithIfsc =
				 * kycService.saveBankwithIfsc(bankaccount); if
				 * (!Sharp6Validation.isEmpty(saveBankwithIfsc)) {
				 * resultMap.put(Constants.STATUS, true); resultMap.put("accountValidation",
				 * bankaccount); } else { resultMap.put(Constants.STATUS, false);
				 * resultMap.put(Constants.ERROR_MSG,
				 * "Internal server error-Bank data not saved in db."); } } else {
				 * resultMap.put(Constants.STATUS, false); resultMap.put(Constants.ERROR_MSG,
				 * "Error Occured while adding benificiary."); }
				 */
				/*
				 * } else { resultMap.put(Constants.STATUS, false);
				 * resultMap.put(Constants.ERROR_MSG, "Benfitiary alrady exist."); }
				 */
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG, "Invalid ifsc code.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, e.getMessage());
		}

		return resultMap;

	}

	@PostMapping("/getbankAccountdetailsByuserId/")
	public HashMap<String, Object> getbankAccountdetailsByuserId(@RequestParam("userid") String userid) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<BankAccount> bankDetailsByuserId = kycService.getBankDetailsByuserId(userid);
		if (!Sharp6Validation.isEmpty(bankDetailsByuserId)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("BankData", bankDetailsByuserId);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Account details not found.");
		}

		return resultMap;

	}

	@PostMapping("/updatePanCardStatusByAdmin/")
	public HashMap<String, Object> updatePanCardStatusByAdmin(@RequestParam("userid") String userid,
			@RequestParam("validationFlag") int validationFlag) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		PanCard pancardByUserId = kycService.getPancardByUserId(userid, null);
		if (!(validationFlag == 3)) {
			if (!Sharp6Validation.isEmpty(pancardByUserId)) {
				PanCard updatePanStatus = kycService.updatePanStatus(pancardByUserId, validationFlag);
				resultMap.put(Constants.STATUS, true);
				resultMap.put("panResult", updatePanStatus);
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG, "Error occured while updating PAN card status.");
			}

		} else {
			UserMaster blockorActiveUser = userservice.blockorActiveUser(userid);
			PanCard updatePanStatus = kycService.updatePanStatus(pancardByUserId, validationFlag);
			resultMap.put(Constants.STATUS, true);
			resultMap.put("panResult", blockorActiveUser);
			if (blockorActiveUser.isActivestatus() == false)
				resultMap.put("value", "UserBlocked");
			else
				resultMap.put("value", "UserActivated");
		}
		return resultMap;
	}

}
