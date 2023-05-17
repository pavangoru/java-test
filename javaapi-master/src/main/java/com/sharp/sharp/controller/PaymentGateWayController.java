package com.sharp.sharp.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.sharp.sharp.awss3.AmazonS3ImageService;
import com.sharp.sharp.entity.BankAccounts;
import com.sharp.sharp.entity.CreateOrderResponseEntity;
import com.sharp.sharp.entity.Lables;
import com.sharp.sharp.entity.Payments;
import com.sharp.sharp.entity.SettleDto;
import com.sharp.sharp.entity.Sharp6Wallet;
import com.sharp.sharp.entity.ShuffleMoney;
import com.sharp.sharp.entity.ShufflieListVivo;
import com.sharp.sharp.entity.TransferKalesVivo;
import com.sharp.sharp.entity.UserMaster;
import com.sharp.sharp.entity.WithDrawDto;
import com.sharp.sharp.repository.BankAccountRepository;
import com.sharp.sharp.repository.PaymentGatewayRepository;
import com.sharp.sharp.repository.WalletRepository;
import com.sharp.sharp.service.CashFreeINteGrationService;
import com.sharp.sharp.service.PaymentService;
import com.sharp.sharp.service.UserService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.SendSms;
import com.sharp.sharp.util.Sharp6Validation;

@RestController
@RequestMapping("/payment")
@Validated
public class PaymentGateWayController {
	@Value("${cash.X-Client-Id}")
	private String keyId;

	@Value("${cash.X-Client-Secret}")
	private String secretKey;

	@Autowired
	private BankAccountRepository bankRepo;

	@Autowired
	private WalletRepository walletRepo;

	@Autowired
	private PaymentService gatewayService;

	@Autowired
	private PaymentGatewayRepository repo;

	@Autowired
	CashFreeINteGrationService cashfree;

	@Autowired
	UserService userService;
	@Autowired
	private static AmazonS3ImageService s3imageService;

	public static void main(String[] args) {
		// writeDataAtOnce("c://csv//result.csv");

		List<String[]> data = new ArrayList<String[]>();
		data.add(new String[] { "Debit Account Number", "12345" });
		data.add(new String[] { "Debit Account Name", "xxxxxxx" });
		data.add(new String[] { "Cheque Number", "-" });
		data.add(new String[] { "Cheque Amount", "-" });
		data.add(new String[] { "Purpose", "Payment" });
		data.add(new String[] { "SR No", "Txn Type", "Credit Account Number", "Credit Account Name", "IFSC", "Amount",
				"Narration" });
		data.add(new String[] { "1", "2", "3", "4", "5", "6", "7" });
		StringWriter s = new StringWriter();
		CSVWriter writer = new CSVWriter(s);
		writer.writeAll(data);
		try {
			writer.close();
		} catch (IOException e) {
		}
		String finalString = s.toString();

		String encodedString = Base64.getEncoder().encodeToString(finalString.getBytes());

		System.out.println("BASE 64 String: ---->   " + encodedString);

	}

	public static void writeDataAtOnce(String filePath) {

		// first create file object for file placed at location
		// specified by filepath
		File file = new File(filePath);

		try {
			/*
			 * // create FileWriter object with file as parameter FileWriter outputfile =
			 * new FileWriter(file);
			 * 
			 * // create CSVWriter object filewriter object as parameter CSVWriter writer =
			 * new CSVWriter(outputfile);
			 * 
			 * // create a List which contains String array List<String[]> data = new
			 * ArrayList<String[]>(); data.add(new String[] { "Name", "Class", "Marks" });
			 * data.add(new String[] { "Aman", "10", "620" }); data.add(new String[] {
			 * "Suraj", "10", "630" }); writer.writeAll(data);
			 * 
			 * // closing writer connection writer.close();
			 */
			CSVWriter csvWriter = new CSVWriter(new FileWriter("example.csv"));
			List<String[]> rows = new LinkedList<String[]>();
			rows.add(new String[] { "1", "jan", "Male", "20" });
			rows.add(new String[] { "2", "con", "Male", "24" });
			rows.add(new String[] { "3", "jane", "Female", "18" });
			rows.add(new String[] { "4", "ryo", "Male", "28" });

			csvWriter.close();
			System.out.println(csvWriter.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/saveTransaction/")
	public HashMap<String, Object> savePayments(@RequestBody Payments payment) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Payments userIdentifier = new Payments();
		try {
			/*
			 * RazorpayClient razorpay = new RazorpayClient(keyId, secretKey);
			 * payment.setKeyId(keyId); JSONObject orderRequest = new JSONObject();
			 * orderRequest.put("order_amount", payment.getAmount()); // amount in the
			 * smallest currency unit orderRequest.put("order_currency",
			 * payment.getCurrency()); orderRequest.put("order_id", "order_rcptid_fact6_" +
			 * new Date().getTime()); Order order = razorpay.Orders.create(orderRequest);
			 * System.out.println(order.toJson().toString()); Gson gson = new Gson();
			 * userIdentifier = gson.fromJson(order.toJson().toString(), Payments.class);
			 * userIdentifier.setUserid(payment.getUserid());
			 */
			String createOrder = cashfree.createOrder(payment);
			Gson json = new Gson();
			if (createOrder.contains("ACTIVE")) {
				CreateOrderResponseEntity responseObj = json.fromJson(createOrder, CreateOrderResponseEntity.class);

				userIdentifier.setAmount(payment.getAmount());
				userIdentifier.setAttempts(1);
				userIdentifier.setCreated_at(String.valueOf(new Date().getTime()));
				userIdentifier.setCurrency(responseObj.getOrder_currency());
				userIdentifier.setEmailId(payment.getEmailId());
				userIdentifier.setEntity(responseObj.getEntity());
				userIdentifier.setNotes(responseObj.getOrder_note());
				userIdentifier.setUserid(payment.getUserid());
				userIdentifier.setId(responseObj.getPayment_session_id());
				userIdentifier.setReceipt(responseObj.getOrder_id());
				userIdentifier.setStatus("created");
				try {
					Payments savePayment = gatewayService.savePayment(userIdentifier);
					if (savePayment != null)
						resultMap.put("data", savePayment);
					resultMap.put("response", responseObj);
					resultMap.put(Constants.STATUS, true);
					resultMap.put(Constants.ERROR_MSG, "payment submitted successfully");
				} catch (Exception e) {
					// TODO: handle exception
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "data not inserted in db");

				}
			} else if (createOrder.contains("authentication Failed")) {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG, "authentication_error : 401, invalid client Id or secret key");

			} else if (createOrder.contains("Too many requests from IP. Check headers")) {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG, "rate_limit_error : 429, Too many requests from IP.");

			} else if (createOrder.contains("internal Server Error")) {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG, "internal_error : 500, api_error.");
			} else if (createOrder.contains("bad URL, please check API documentation")) {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG,
						"invalid_request_error : Default, bad URL, please check API documentation.");
			}
		} catch (Exception e) {
			// Handle Exception
			System.out.println(e.getMessage());
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", e.getMessage());
			resultMap.put(Constants.ERROR_MSG, "Payment submission failed. " + e.getMessage());
		}

		/*
		 * if (!Sharp6Validation.isEmpty(userIdentifier)) {
		 * 
		 * Payments savePayment = gatewayService.savePayment(userIdentifier);
		 * savePayment.setKeyId(keyId); resultMap.put(Constants.STATUS, true);
		 * resultMap.put("data", savePayment); resultMap.put("value",
		 * "payment submitted successfully"); } else {
		 * 
		 * resultMap.put(Constants.STATUS, false); resultMap.put("value",
		 * "payment submitted failed"); resultMap.put(Constants.ERROR_MSG,
		 * "Payment submission failed."); }
		 */

		return resultMap;

	}

	@PostMapping("/getTransaction/")
	public HashMap<String, Object> getTransactionByid(@RequestBody Payments payment) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Optional<Payments> paymentObj = null;
		if (!Sharp6Validation.isEmpty(paymentObj)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", paymentObj);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Payment details not available. Please try after sometime.");
		}

		return resultMap;

	}

	@PostMapping("/getAllTranssactionsByuserId/")
	public HashMap<String, Object> getUserTransactions(@RequestBody Payments payment) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<Payments> transactionList = gatewayService.getAlTransactionsbyUserId(payment.getUserid());
		if (!Sharp6Validation.isEmpty(transactionList)) {

			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", transactionList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "No payments found.");
		}

		return resultMap;

	}

	@PostMapping("/addToWallet/")
	public HashMap<String, Object> addKalesToWallet(@RequestBody Payments payment) {
		payment.setTranscationtype("add");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Sharp6Wallet mainKales = gatewayService.getMainKales(payment.getUserid());
		if (payment.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
			mainKales.setMainkales(mainKales.getMainkales() + payment.getAmount());

			Sharp6Wallet saveMoneytoWalet = gatewayService.saveMoneytoWalet(mainKales);
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", "Kales added to Wallet to Successfully");
			resultMap.put("data", saveMoneytoWalet);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", "Money added failed to wallet");
			resultMap.put(Constants.ERROR_MSG, "Adding money to wallet failed.");
		}
		Payments savePayment = gatewayService.updatePaymentStatus(payment);
		// List<Payments> transactionList = gatewayService.addKalesToWallet(payment);

		return resultMap;

	}

	@SuppressWarnings("unused")
	@PostMapping("/walletToWallet/")
	public HashMap<String, Object> walletToWalletKalesTransfer(@RequestBody TransferKalesVivo payment) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		/*
		 * String transactionList = gatewayService.walletToWalletKalesTransfer(payment);
		 * if (transactionList.equals("Transfer Succesfull")) {
		 * resultMap.put(Constants.STATUS, true); resultMap.put("value",
		 * transactionList); } else if (transactionList != null) {
		 * resultMap.put(Constants.STATUS, false); resultMap.put(Constants.ERROR_MSG,
		 * transactionList); } else { resultMap.put(Constants.STATUS, false);
		 * resultMap.put(Constants.ERROR_MSG, "Unable to transfer Kales."); }
		 */
		return resultMap;

	}

	@PostMapping("/saveLableShuffleMoney/")
	public HashMap<String, Object> saveLableShuffleMoney(@RequestBody ShufflieListVivo shuffle) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		ShufflieListVivo shuffingMoney = gatewayService.saveLableShuffleMoney(shuffle);
		if (!Sharp6Validation.isEmpty(shuffingMoney)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", "ShuffleMoney with lable Added");
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Failed.");
		}
		return resultMap;

	}

	// remoove game lable by lableid
	@PostMapping("/remooveLable/")
	public HashMap<String, Object> remooveLableShuffleMoney(@RequestParam("lableId") int lableId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String status = gatewayService.remooveLabeShuffleMoney(lableId);
		if (status.equals(Constants.SUCCESS)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", "Lable remooved Successfuly");
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Lable not remooved.");
		}
		return resultMap;

	}

	@PostMapping("/getAllables/")
	public HashMap<String, Object> getAllLables() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<Lables> lablesList = gatewayService.getAllLables();
		if (!Sharp6Validation.isEmpty(lablesList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", lablesList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "No lables available.");
		}
		return resultMap;

	}

	@PostMapping("/getShuffleMoney/")
	public HashMap<String, Object> getShuffleMoney(@RequestParam("shuffleId") String shuffleId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		ShuffleMoney shuffingMoney = gatewayService.getShuffleMoney(shuffleId);
		if (!Sharp6Validation.isEmpty(shuffingMoney)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", shuffingMoney);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Failed.");
		}
		return resultMap;

		/*
		 * @PostMapping("/getRanksWithLeadBoards/") public HashMap<String, Object>
		 * getRanksWithMoney(@RequestParam("contestId") String contestId,
		 * 
		 * @RequestParam("userId") String userId) { HashMap<String, Object> resultMap =
		 * new HashMap<String, Object>(); List<UserAnswerHistory> shuffingMoney =
		 * gatewayService.getRanksWithMoney(contestId,userId); if
		 * (!Sharp6Validation.isEmpty(shuffingMoney)) { resultMap.put(Constants.STATUS,
		 * true); resultMap.put("value", shuffingMoney); } else {
		 * 
		 * 
		 * } resultMap.put(Constants.STATUS, false); resultMap.put("value", "Faied"); }
		 * return resultMap;
		 * 
		 * }
		 */
	}

	@GetMapping("/getAllTransactionsbyUserId/{userId}")
	public HashMap<String, Object> getAllTransactionsbyUserId(@PathVariable("userId") String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<Payments> alTransactionsbyUserId = gatewayService.getAlTransactionsbyUserId(userId);
		if (!Sharp6Validation.isEmpty(alTransactionsbyUserId)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("allTransactions", alTransactionsbyUserId);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Failed to get all transactions by userId.");
		}

		return resultMap;
	}

	@GetMapping("/getAddingTransactionsbyUserId/{userId}")
	public HashMap<String, Object> getAddingTransactionsbyUserId(@PathVariable("userId") String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<Payments> transferTransactions = gatewayService.getAddingTransactionsbyUserId(userId);
		if (!Sharp6Validation.isEmpty(transferTransactions)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("transferList", transferTransactions);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "Failed to get add transactions by userId.");
		}

		return resultMap;
	}

	@GetMapping("/getRazorpaySecretKeyAndKeyId/")
	public HashMap<String, Object> getSecretKeyAndKeyId() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put(Constants.STATUS, true);
		resultMap.put("keyId", keyId);
		resultMap.put("secretKey", secretKey);

		return resultMap;
	}

	@GetMapping("/isWalletExistBYId/{userId}")
	public HashMap<String, Object> isWalletExistBYId(@PathVariable("userId") String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String walletExistBYId = gatewayService.isWalletExistBYId(userId);
		resultMap.put(Constants.STATUS, walletExistBYId);

		return resultMap;
	}

	@GetMapping("/getWithDrawTransactions/{userId}/")
	public HashMap<String, Object> getWithDrawTransactions(@PathVariable("userId") String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<Payments> withdraw = gatewayService.getWithDrawTransactions(userId);
		if (!Sharp6Validation.isEmpty(withdraw)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", withdraw);
			resultMap.put("message", "success");

		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", withdraw);
			resultMap.put(Constants.ERROR_MSG, "No withdrawls fetched.");
		}

		return resultMap;
	}

	@GetMapping("/getTransferTransactionByUserId/{userId}/")
	public HashMap<String, Object> getTransferTransactionByUserId(@PathVariable("userId") String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		List<Payments> withdraw = gatewayService.getTransferTransactionByUserId(userId);
		if (!Sharp6Validation.isEmpty(withdraw)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", withdraw);
			resultMap.put("message", "success");

		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", withdraw);
			resultMap.put(Constants.ERROR_MSG, "No transfers fetched.");
		}

		return resultMap;
	}

	@GetMapping("/getWinningAmountforWithDrwal/{userId}/")
	public HashMap<String, Object> getWinningAmountforWithDrwal(@PathVariable("userId") String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Payments> withdraw = gatewayService.getWinningAmountforWithDrwal(userId);
			if (!Sharp6Validation.isEmpty(withdraw)) {
				Payments payement = withdraw.get(0);
				payement.setAmount(Double.parseDouble(payement.getTotalAmountWon()));
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", String.valueOf(payement.getTotalAmountWon()));

				resultMap.put("message", "success");

			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put("value", "0.0");
				resultMap.put(Constants.ERROR_MSG, "No winnings found");
			}
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", e.getMessage());
			resultMap.put(Constants.ERROR_MSG, "No winnings found");
		}
		return resultMap;
	}

	@PostMapping("/updateWallet/")
	public HashMap<String, Object> saveMoneyToWallet(@RequestBody Sharp6Wallet wallet) {
		try {
			HashMap<String, Object> resultMap = new HashMap<String, Object>();

			Double kales = wallet.getMainkales();

			Integer status = repo.checkTransaction(wallet.getTransactionid());
			if (status > 0) {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG, "transaction id already exists ");

			} else {

				Optional<Sharp6Wallet> optwalletResponse = walletRepo.findById(wallet.getUserid());
				Sharp6Wallet walletResponse = null;
				if (optwalletResponse.isPresent()) {
					walletResponse = optwalletResponse.get();
				} else {
					resultMap.put(Constants.STATUS, false);
					resultMap.put("value", null);
					resultMap.put(Constants.ERROR_MSG, "no walllet found for user ");
					return resultMap;
				}

				Double totalKales = 0.0;
				Sharp6Wallet walletResp = null;
				if (walletResponse != null) {

					totalKales = wallet.getMainkales() + walletResponse.getMainkales();
					Payments payments = new Payments();

					payments.setAmount(wallet.getMainkales());

					wallet.setMainkales(totalKales);
					walletResp = walletRepo.save(wallet);
					payments.setCurrency("INR");
					payments.setStatus("SUCCESS");
					payments.setTransactionid(wallet.getTransactionid());
					payments.setUserid(wallet.getUserid());
					payments.setTranscationtype("add");
					payments.setCreated_at(String.valueOf(new Date().getTime()));
					Payments savePayment = gatewayService.savePayment(payments);
					UserMaster usermaster = userService.getuserById(wallet.getUserid());
					usermaster.setMainkales(wallet.getMainkales());
					userService.newUserRegister(usermaster);

				}

				if (!Sharp6Validation.isEmpty(walletResp)) {
					resultMap.put(Constants.STATUS, true);
					resultMap.put("value", walletResp);
					resultMap.put("message", "amount added");
					String message = "Fact6: Fantasy Quiz Game: Rs. " + kales + " credited to your wallet "
							+ wallet.getUserid() + " on " + LocalDate.now() + " linked to (Ref No "
							+ wallet.getTransactionid() + ").";
					ResponseEntity<String> response = SendSms.sendAmountCredited(wallet.getUserid(), message);

				} else {
					resultMap.put(Constants.STATUS, false);
					resultMap.put("value", walletResp);
					resultMap.put(Constants.ERROR_MSG, "amount addition failed");
				}
			}
			return resultMap;

		} catch (Exception e) {
			return null;
		}
	}

	@PostMapping("/intiateWithDrawTransaction/")
	public HashMap<String, Object> intiateWithDrawTransaction(@Valid @RequestBody WithDrawDto dto) {
		Optional<Sharp6Wallet> optwalletResponse = walletRepo.findById(dto.getUserID());
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (optwalletResponse.isPresent()) {
				if (optwalletResponse.get().getMainkales() < dto.getAmount()) {
					resultMap.put(Constants.STATUS, false);
					resultMap.put("value", null);
					resultMap.put(Constants.ERROR_MSG, "withdraw amount exceeds the available amount");
					return resultMap;
				} else {
					Sharp6Wallet wallet = optwalletResponse.get();
					wallet.setMainkales(wallet.getMainkales() - dto.getAmount());
					walletRepo.save(wallet);
					UserMaster usermaster = userService.getuserById(dto.getUserID());
					usermaster.setMainkales(wallet.getMainkales());
					userService.newUserRegister(usermaster);
					Payments payments = new Payments();
					payments.setAmount(dto.getAmount());
					payments.setUserid(dto.getUserID());
					payments.setTranscationtype("WITHDRAW");
					payments.setCurrency("INR");
					payments.setStatus("SUCCESS");
					payments.setWithdrawStatus("intiated");
					payments.setTransactionNumber(String.valueOf(new Date().getTime()) + "WITHDRAW" + dto.getAmount());
					payments.setCreated_at(String.valueOf(new Date().getTime()));
					/*
					 * BankAccounts banks = new BankAccounts();
					 * banks.setAccountNumber(dto.getAccountNumber());
					 * banks.setBankName(dto.getBankName()); banks.setEmail(dto.getEmail());
					 * banks.setIfsc(dto.getIfsc()); banks.setName(dto.getName());
					 * banks.setPhone(dto.getPhone()); banks.setUserid(dto.getUserID());
					 * bankRepo.save(banks);
					 */

					Payments p = repo.save(payments);

					resultMap.put(Constants.STATUS, true);
					resultMap.put("transactionNumber", payments.getTransactionNumber());
					resultMap.put("value", "withdrawal process intiated , it will be settled in 8 hrs ");
					return resultMap;

				}

			}
		}

		catch (Exception e) {
			Payments payments = new Payments();
			payments.setAmount(dto.getAmount());
			payments.setUserid(dto.getUserID());
			payments.setTranscationtype("WITHDRAW");
			payments.setCurrency("INR");
			payments.setStatus("FAILURE");
			payments.setCreated_at(String.valueOf(new Date().getTime()));
			payments.setTransactionNumber(String.valueOf(new Date().getTime()) + "WITHDRAW" + dto.getAmount());
			repo.save(payments);
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", null);
			resultMap.put("transactionNumber", payments.getTransactionNumber());
			resultMap.put(Constants.SUCCESS, "withdrawal process failed  , please try again after some time  ");
			System.out.println(e.getMessage());
			return resultMap;

		}

		return resultMap;

	}

	@GetMapping("/getWithDrawnPendingAccounts/")
	public Map<String, Object> getWithDrawnPendingAccounts() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Payments> payments = repo.findBywithdrawStatus("intiated");
			for (Payments payment : payments) {
				List<BankAccounts> accounts = bankRepo.findByuserid(payment.getUserid());
				if (!accounts.isEmpty()) {
					payment.setAccountDetails(accounts.get(0));
				}
			}
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", payments);
			resultMap.put(Constants.SUCCESS, "list of accounts that intiated withdrwal ");
			return resultMap;
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", "unable to fetch records , plese try again");
			// resultMap.put(Constants.SUCCESS, "unable to fetch records , plese try again
			// ");
			return resultMap;
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public HashMap<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		HashMap<String, Object> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		errors.put(Constants.STATUS, false);
		errors.put("value", "please provide correct details for the following fields");

		return errors;
	}

	@PostMapping("/settlePayment/")
	public HashMap<String, Object> settlePayment(@RequestBody List<SettleDto> settled) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Payments payment = null;
		try {
			for (SettleDto settle : settled) {
				payment = repo.findbyuseridandpaymentstatus(settle.getUserid(), settle.getPaymentid());

				if (payment != null) {
					payment.setWithdrawStatus("settled");
					Payments returned = repo.save(payment);

				}
			}
			if (payment != null) {

				// create FileWriter object with file as parameter
				/*
				 * String path = "35.170.3.72//settledfiles//result" + new Date().getTime() +
				 * ".csv"; FileWriter outputfile = new FileWriter(path);
				 * 
				 * // create CSVWriter object filewriter object as parameter CSVWriter writer =
				 * new CSVWriter(outputfile);
				 */

				// create a List which contains String array
				List<String[]> data = new ArrayList<String[]>();
				data.add(new String[] { "Debit Account Number", "12345" });
				data.add(new String[] { "Debit Account Name", "xxxxxxx" });
				data.add(new String[] { "Cheque Number", "-" });
				data.add(new String[] { "Cheque Amount", "-" });
				data.add(new String[] { "Purpose", "Payment" });
				data.add(new String[] { "SR No", "Txn Type", "Credit Account Number", "Credit Account Name", "IFSC",
						"Amount", "Narration" });
				for (int i = 0; i < settled.size(); i++) {
					data.add(new String[] { String.valueOf(i + 1), "IMPS",
							settled.get(i).getBankAccount().getAccountNumber(),
							settled.get(i).getBankAccount().getName(), settled.get(i).getBankAccount().getIfsc(),
							settled.get(i).getAmount(), });
				}
				String writeCsvAsString = writeCsvAsString(data);
				resultMap.put("base64Value", writeCsvAsString);

				/*
				 * writer.writeAll(data);
				 * 
				 * // closing writer connection writer.close();
				 */

				resultMap.put(Constants.STATUS, true);
				resultMap.put(Constants.SUCCESS, "payments settled");
				return resultMap;

			} else {
				resultMap.put(Constants.STATUS, true);
				resultMap.put("value", "user not found for the given userid");
				return resultMap;
			}

		} catch (Exception e) {
			return null;
		}

	}

	@GetMapping("/getsettledAccountsList/")
	public Map<String, Object> getsettledAccountsList() {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Payments> payments = repo.findBywithdrawStatus("settled");
			for (Payments payment : payments) {
				List<BankAccounts> accounts = bankRepo.findByuserid(payment.getUserid());
				if (!accounts.isEmpty()) {
					payment.setAccountDetails(accounts.get(0));
				}
			}
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", payments);
			resultMap.put(Constants.SUCCESS, "list of accounts which payments got settled ");
			return resultMap;
		} catch (Exception e) {
			resultMap.put(Constants.STATUS, false);
			resultMap.put("value", "unable to fetch records , plese try again");

			return resultMap;
		}
	}

	public String writeCsvAsString(List<String[]> csvData) {

		StringWriter s = new StringWriter();
		CSVWriter writer = new CSVWriter(s);
		writer.writeAll(csvData);
		try {
			writer.close();
		} catch (IOException e) {
		}
		String finalString = s.toString();// converting into .csv string

		String encodedString = Base64.getEncoder().encodeToString(finalString.getBytes());// converting into bas64
																							// string

		return encodedString;
	}

}
