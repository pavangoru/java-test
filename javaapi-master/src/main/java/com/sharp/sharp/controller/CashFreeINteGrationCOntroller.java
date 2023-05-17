package com.sharp.sharp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharp.sharp.entity.BankTransferPojo;
import com.sharp.sharp.entity.BeneInfo;
import com.sharp.sharp.entity.BenefitiaryDetails;
import com.sharp.sharp.entity.CashFreeKeys;
import com.sharp.sharp.entity.Payments;
import com.sharp.sharp.entity.TokenData;
import com.sharp.sharp.service.CashFreeINteGrationService;
import com.sharp.sharp.service.PaymentService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.Sharp6Validation;

@RestController
@RequestMapping("/cashfree")
public class CashFreeINteGrationCOntroller {

	@Autowired
	private CashFreeINteGrationService cashFreeService;
	@Value("${cash.X-Client-Id}")
	private String clientId;

	@Value("${cash.X-Client-Secret}")
	private String clientSecret;
	@Autowired
	private PaymentService gatewayService;

	@PostMapping("/tokenGeneration/")
	public ResponseEntity<Map<String, Object>> GenerateToken(@RequestParam("X-Client-Id") String clientId,
			@RequestParam("X-Client-Secret") String clientSecret) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			TokenData result = cashFreeService.GenerateToken(CashFreeKeys.PaymentGateway_clientId,
					CashFreeKeys.PaymentGateway_clientsecret);
			if (!Sharp6Validation.isEmpty(result)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", result);
			} else {
				resultMap.put(Constants.STATUS, Constants.FAILURE);
				resultMap.put("errorvalue", "client data insufficient");
				resultMap.put(Constants.ERROR_MSG, "Client data insufficient.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.STATUS, Constants.FAILURE);
			resultMap.put("errorvalue", "client data insufficient");
			resultMap.put(Constants.ERROR_MSG, "Client data insufficient.");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@GetMapping("/getBenfitiary/{benId}")
	public ResponseEntity<Map<String, Object>> getBenfitiaryDetails(@PathVariable("benId") String benId) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			TokenData result = cashFreeService.GenerateToken(CashFreeKeys.PaymentGateway_clientId,
					CashFreeKeys.PaymentGateway_clientsecret);
			BenefitiaryDetails benefitiaryDetails = cashFreeService.getBenefitiaryDetails(result.getData().getToken(),
					benId);
			if (!Sharp6Validation.isEmpty(benefitiaryDetails)) {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", benefitiaryDetails);
			} else {
				resultMap.put(Constants.STATUS, Constants.FAILURE);
				resultMap.put("errorvalue", "benefitiaryDetails data not available");
				resultMap.put(Constants.ERROR_MSG, "BenefitiaryDetails data not available.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.STATUS, Constants.FAILURE);
			resultMap.put("errorvalue", "benefitiaryDetails data not avaiable");
			resultMap.put(Constants.ERROR_MSG, "BenefitiaryDetails data not available.");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@PostMapping("/directTransferToBenefitiary/")
	public ResponseEntity<Map<String, Object>> directTransferToBenefitiary(@RequestParam("benId") String benId,
			@RequestParam("amount") double amount) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			TokenData result = cashFreeService.GenerateToken(CashFreeKeys.PaymentGateway_clientId,
					CashFreeKeys.PaymentGateway_clientsecret);
			BenefitiaryDetails benefitiaryDetails = cashFreeService.getBenefitiaryDetails(result.getData().getToken(),
					benId);

			if (!Sharp6Validation.isEmpty(benefitiaryDetails)) {
				if (benefitiaryDetails.getMessage().contains("Beneficiary does not exist")) {
					resultMap.put(Constants.STATUS, "ERROR");
					resultMap.put(Constants.ERROR_MSG, "Beneficiary does not exist :404,ERROR");
				} else {
					BankTransferPojo transferPojo = new BankTransferPojo();
					transferPojo.setAmount(amount);

					transferPojo.setTransferId(String.valueOf(new Date().getTime()));
					BeneInfo benefitiaryDetails2 = new BeneInfo();
					transferPojo.setTransferMode("banktransfer");

					benefitiaryDetails2.setBankAccount(benefitiaryDetails.getData().getBankAccount());
					benefitiaryDetails2.setIfsc(benefitiaryDetails.getData().getIfsc());
					benefitiaryDetails2.setName(benefitiaryDetails.getData().getName());
					benefitiaryDetails2.setEmail(benefitiaryDetails.getData().getEmail());
					benefitiaryDetails2.setPhone(benefitiaryDetails.getData().getPhone());
					benefitiaryDetails2.setAddress1(benefitiaryDetails.getData().getAddress1());
					transferPojo.setBeneDetails(benefitiaryDetails2);
					String transferResponse = cashFreeService.directTransferToBenefitiary(transferPojo,
							result.getData().getToken());
					if (transferResponse.contains("Transfer completed successfully.")) {
						resultMap.put(Constants.STATUS, "SUCCESS");
						resultMap.put("value", benefitiaryDetails);
						resultMap.put(Constants.ERROR_MSG, "Transfer completed successfully. :200,SUCCESS");
					} else if (transferResponse.contains("Transfer Scheduled for next working day.")) {
						resultMap.put(Constants.STATUS, "SUCCESS");
						resultMap.put(Constants.ERROR_MSG, "Transfer Scheduled for next working day. :201,SUCCESS");
					} else if (transferResponse.contains("Awaiting confirmation from beneficiary bank.")) {
						resultMap.put(Constants.STATUS, "PENDING");
						resultMap.put(Constants.ERROR_MSG, "Awaiting confirmation from beneficiary bank. :201,PENDING");
					} else if (transferResponse.contains("Transfer request pending at the bank.")) {
						resultMap.put(Constants.STATUS, "PENDING");
						resultMap.put(Constants.ERROR_MSG, "Transfer request pending at the bank. :201,PENDING");
					} else if (transferResponse.contains("Request received. Please check status after some time")) {
						resultMap.put(Constants.STATUS, "PENDING");
						resultMap.put(Constants.ERROR_MSG,
								"Request received. Please check status after some time :202,PENDING");
					} else if (transferResponse.contains("Transfer attempt failed at the bank.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Transfer attempt failed at the bank. :400/520,ERROR");
					} else if (transferResponse.contains("Token is not valid")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Token is not valid :403,ERROR");
					} else if (transferResponse.contains("Token is not valid")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Token is not valid :403,ERROR");
					} else if (transferResponse.contains("IP not whitelisted")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "IP not whitelisted :403,ERROR");
					} else if (transferResponse.contains("IP not whitelisted")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "IP not whitelisted :403,ERROR");
					} else if (transferResponse.contains("This feature is not available for your account.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"This feature is not available for your account. :403,ERROR");
					} else if (transferResponse.contains("Transfer to this beneficiary not allowed.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Transfer to this beneficiary not allowed. :403,ERROR");
					} else if (transferResponse.contains("Transfer mode is not available for your account.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"Transfer mode is not available for your account. :403,ERROR");
					} else if (transferResponse.contains("Beneficiary does not exist.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Beneficiary does not exist. :404,ERROR");
					} else if (transferResponse.contains("Transfer Id already exists.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Transfer Id already exists. :409,ERROR");
					} else if (transferResponse.contains("Token missing in the request.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Token missing in the request. :412,ERROR");
					} else if (transferResponse.contains("BeneId missing in the request.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "BeneId missing in the request. :412,ERROR");
					} else if (transferResponse.contains("Amount missing in the request.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Amount missing in the request. :412,ERROR");
					} else if (transferResponse.contains("TransferId missing in the request.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "TransferId missing in the request. :412,ERROR");
					} else if (transferResponse.contains("Not enough available balance in the account.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Not enough available balance in the account. :412,ERROR");
					} else if (transferResponse.contains("Please wait 30 minutes after adding the beneficiary.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"Please wait 30 minutes after adding the beneficiary. :412,ERROR");
					} else if (transferResponse.contains("Transfer amount is less than minimum amount of Rs. 100.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"Transfer amount is less than minimum amount of Rs. 100. :412,ERROR");
					} else if (transferResponse
							.contains("Transfer amount is greater than the maximum amount of Rs.100000.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"Transfer amount is greater than the maximum amount of Rs.100000. :412,ERROR");
					} else if (transferResponse.contains("Invalid Tag passed in the request.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Invalid Tag passed in the request. :412,ERROR");
					} else if (transferResponse.contains("Transfer mode not enabled for the account.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Transfer mode not enabled for the account. :412,ERROR");
					} else if (transferResponse.contains("Invalid transfer mode passed in the request.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Invalid transfer mode passed in the request. :412,ERROR");
					} else if (transferResponse.contains("Transfer limit for your account exceeded.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Transfer limit for your account exceeded. :412,ERROR");
					} else if (transferResponse.contains("Transfer limit for beneficiary exceeded.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Transfer limit for beneficiary exceeded. :412,ERROR");
					} else if (transferResponse.contains("Invalid amount passed.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Invalid amount passed. :422,ERROR");
					} else if (transferResponse.contains("Invalid transferId passed.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Invalid transferId passed. :422,ERROR");
					} else if (transferResponse.contains("No Payee Virtual Address associated with the beneficiary.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"No Payee Virtual Address associated with the beneficiary. :422,ERROR");
					} else if (transferResponse.contains("Remarks can have only numbers, alphabets and whitespaces.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"Remarks can have only numbers, alphabets and whitespaces. :422,ERROR");
					} else if (transferResponse.contains("Beneficiary details are missing in the request.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"Beneficiary details are missing in the request. :422,ERROR");
					} else if (transferResponse.contains("Beneficiary details not valid.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Beneficiary details not valid. :422,ERROR");
					} else if (transferResponse.contains("Invalid amount passed.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Invalid amount passed. :422,ERROR");
					} else if (transferResponse.contains("No Bank account or IFSC associated with the beneficiary.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"No Bank account or IFSC associated with the beneficiary. :422,ERROR");
					} else if (transferResponse.contains("Invalid IFSC code provided for bank account.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Invalid IFSC code provided for bank account. :422,ERROR");
					} else if (transferResponse.contains("Invalid bank account number or IFSC provided.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Invalid bank account number or IFSC provided. :422,ERROR");
					} else if (transferResponse.contains("Transfer request to paytm wallet failed.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG, "Transfer request to paytm wallet failed. :422,ERROR");
					} else if (transferResponse
							.contains("transferId can contain only alphabets , numbers , hyphen and underscore")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"transferId can contain only alphabets , numbers , hyphen and underscore :422,ERROR");
					} else if (transferResponse.contains("Transfer request triggered.No response from bank.")) {
						resultMap.put(Constants.STATUS, "ERROR");
						resultMap.put(Constants.ERROR_MSG,
								"Transfer request triggered.No response from bank. :520,ERROR");
					}
				}

			} else {
				resultMap.put(Constants.STATUS, Constants.FAILURE);
				resultMap.put("errorvalue", "benefitiaryDetails data not avaiable");
				resultMap.put(Constants.ERROR_MSG, "BenefitiaryDetails data not available.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.STATUS, Constants.FAILURE);
			resultMap.put("errorvalue", "benefitiaryDetails data not avaiable");
			resultMap.put(Constants.ERROR_MSG, "BenefitiaryDetails data not available.");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@PostMapping("/addBenfitiary/")
	public ResponseEntity<Map<String, Object>> addBenfi(@RequestBody BankAccount bank) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			bank.setAccountNumber(bank.getBankAccount());
			BankAccount saveBenfitiary = cashFreeService.saveBenfitiary(bank);
			saveBenfitiary.setValidationFlag(1);
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", saveBenfitiary);
			boolean l = false;
			if (l == true) {// to avoid cashfree insertion
				BankAccount addBefi = cashFreeService.addBefi(bank);
				if (addBefi.getResponse().contains("Beneficiary added successfully")) {
					saveBenfitiary.setValidationFlag(1);
					resultMap.put(Constants.STATUS, true);
					resultMap.put("value", saveBenfitiary);
					resultMap.put(Constants.ERROR_MSG, "Beneficiary added successfully: 200");
				} else if (addBefi.getResponse().contains("Token is not valid")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Token is not valid: 403");
				} else if (addBefi.getResponse().contains("IP not whitelisted")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "IP not whitelisted: 403");
				} else if (addBefi.getResponse().contains("Beneficiary Id already exists")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Beneficiary Id already exists: 409");
				} else if (addBefi.getResponse().contains("Entered bank Account is already registered")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Entered bank Account is already registered: 409");
				} else if (addBefi.getResponse().contains("Beneficiary group is not an active group")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Beneficiary group is not an active group: 412");
				} else if (addBefi.getResponse().contains("Cannot add yourself as a beneficiary")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Cannot add yourself as a beneficiary: 412");
				} else if (addBefi.getResponse().contains("Please provide a valid Beneficiary Id")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid Beneficiary Id: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid email")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid email: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid name")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid name: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid Phone Number")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid Phone Number: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid Bank Account")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid Bank Account: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid Bank IFSC code")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid Bank IFSC code: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid Virtual Payee Address")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid Virtual Payee Address: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid Address")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid Address: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid City Name")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid City Name: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid State Name")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid State Name: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid Pin code")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid Pin code: 422");
				} else if (addBefi.getResponse().contains("Cashfree VBA/VPA cannot be added as beneficiary")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Cashfree VBA/VPA cannot be added as beneficiary: 422");
				} else if (addBefi.getResponse().contains("Please provide a valid MasterCard or Visa card number")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Please provide a valid MasterCard or Visa card number: 422");
				} else if (addBefi.getResponse()
						.contains("Please provide a masked card number of a valid MasterCard or Visa card")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG,
							"Please provide a masked card number of a valid MasterCard or Visa card: 422");
				} else if (addBefi.getResponse().contains("Invalid details provided")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Invalid details provided: 422");
				} else if (addBefi.getResponse().contains("Adding beneficiary Failed")) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Adding beneficiary Failed: 520");
				} else if (addBefi.getResponse() == null) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "Authourization not success");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, e.getLocalizedMessage());
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@PostMapping("/directTransferToUserId/")
	public ResponseEntity<Map<String, Object>> directTransferToUserId(@RequestParam("userid") String userid,
			@RequestParam("benId") String benId, @RequestParam("amount") double amount) {
		Map<String, Object> resultMap = new HashMap<>();
		// String benId = cashFreeService.getBenIdByUserId(userid);

		Date withdrawalDate = null;
		Long diffHours = null;
		Boolean newSuccessfulWithdrawal = false;
		try {
			String latestWithdrawalTime = gatewayService.getLatestWithdrawalTime(userid);
			withdrawalDate = new Date(Long.parseLong(latestWithdrawalTime));
			System.out.println("Last Withdrawal Date: " + withdrawalDate);
			diffHours = Math.abs(new Date().getTime() - withdrawalDate.getTime()) / (60 * 60 * 1000);
			System.out.println("Timestamp " + diffHours);
		} catch (Exception e1) {
			System.out.println("New Successful withdrawal");
			resultMap = formResultMap(userid, benId, amount);
			newSuccessfulWithdrawal = true;
		}

		if (null != diffHours && diffHours > 24) {
			resultMap = formResultMap(userid, benId, amount);
		} else if (!newSuccessfulWithdrawal) {
			resultMap.put(Constants.STATUS, Constants.FAILURE);
			resultMap.put(Constants.VALUE, null);
			resultMap.put(Constants.ERROR_MSG, "Your last withdrawal was done on " + withdrawalDate
					+ ". Withdrawals not allowed within 24 hours from your last withdrawal.");
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	public Map<String, Object> formResultMap(String userid, String benId, double amount) {
		Map<String, Object> resultMap = new HashMap<>();
		TokenData result = cashFreeService.GenerateToken(clientId, clientSecret);
		BenefitiaryDetails benefitiaryDetails = cashFreeService.getBenefitiaryDetails(result.getData().getToken(),
				benId);
		try {
			if (!benefitiaryDetails.getStatus().equalsIgnoreCase("ERROR")) {
				BankTransferPojo transferPojo = new BankTransferPojo();
				transferPojo.setAmount(amount);

				System.out.println("direct transfer commited");
				transferPojo.setTransferId("txn" + "draw" + new Date().getTime());
				BeneInfo benefitiaryDetails2 = new BeneInfo();
				transferPojo.setTransferMode("banktransfer");
				if (null != benefitiaryDetails.getData()) {
					benefitiaryDetails2.setBankAccount(benefitiaryDetails.getData().getBankAccount());
					benefitiaryDetails2.setIfsc(benefitiaryDetails.getData().getIfsc());
					benefitiaryDetails2.setName(benefitiaryDetails.getData().getName());
					benefitiaryDetails2.setEmail(benefitiaryDetails.getData().getEmail());
					benefitiaryDetails2.setPhone(benefitiaryDetails.getData().getPhone());
					benefitiaryDetails2.setAddress1(benefitiaryDetails.getData().getAddress1());
				}
				transferPojo.setBeneDetails(benefitiaryDetails2);
				String directTransferToBenefitiary = cashFreeService.directTransferToBenefitiary(transferPojo,
						result.getData().getToken());
				Payments payments = new Payments();
				if (StringUtils.isNotBlank(directTransferToBenefitiary)
						&& directTransferToBenefitiary.contains("success")) {
					resultMap.put(Constants.STATUS, true);
					resultMap.put("value", benefitiaryDetails);
					resultMap.put("message", "withdraw successfull");
					payments.setAmount(amount);
					payments.setCurrency("INR");
					payments.setStatus("SUCCESS");
					payments.setTransactionid(transferPojo.getTransferId());
					payments.setUserid(userid);
					payments.setTranscationtype("WITHDRAW");
					payments.setCreated_at(String.valueOf(new Date().getTime()));
					Payments savePayment = gatewayService.savePayment(payments);
				} else {
					resultMap.put(Constants.STATUS, false);
					resultMap.put("value", null);
					resultMap.put("message", "withdraw failure");
					resultMap.put(Constants.STATUS, true);
					resultMap.put("value", benefitiaryDetails);
					resultMap.put("message", "withdraw successfull");
					payments.setAmount(amount);
					payments.setCurrency("INR");
					payments.setStatus("FAILURE");
					payments.setTransactionid(transferPojo.getTransferId());
					payments.setUserid(userid);
					payments.setTranscationtype("WITHDRAW");
					payments.setCreated_at(String.valueOf(new Date().getTime()));
					Payments savePayment = gatewayService.savePayment(payments);
				}
			} else {
				resultMap.put(Constants.STATUS, Constants.FAILURE);
				resultMap.put("value", null);
				resultMap.put(Constants.ERROR_MSG, "BenefitiaryDetails data not available.");
			}
		} catch (Exception e) {
			System.out.println("Error occured while withdrwal: " + e);
			resultMap.put(Constants.STATUS, Constants.FAILURE);
			resultMap.put("errorvalue", "benefitiaryDetails data not avaiable");
			resultMap.put(Constants.ERROR_MSG, "BenefitiaryDetails data not available.");
		}
		return resultMap;

	}

}
