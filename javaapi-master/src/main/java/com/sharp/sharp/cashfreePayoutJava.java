package com.sharp.sharp;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import com.cashfree.lib.constants.Constants.Environment;
import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.logger.VerySimpleFormatter;
import com.cashfree.lib.payout.clients.Beneficiary;
import com.cashfree.lib.payout.clients.Payouts;
import com.cashfree.lib.payout.clients.Transfers;
import com.cashfree.lib.payout.domains.BeneficiaryDetails;
import com.cashfree.lib.payout.domains.request.RequestTransferRequest;

public class cashfreePayoutJava {

	private static final Logger log = Logger.getLogger(cashfreePayoutJava.class.getName());

	public cashfreePayoutJava() {
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new VerySimpleFormatter());
		log.addHandler(consoleHandler);
	}

	public static void main(String[] args) { Payouts payouts =
	  Payouts.getInstance(Environment.PRODUCTION,
	  "CF191251C943M362HA3KP9U0GIDG","7f3dc594e6decdc7f836dd61e9bdb622a95f4c38");
	  System.out.println("" + payouts.init());
	  System.out.println("payouts initialized"); boolean isTokenValid =
	  payouts.verifyToken(); if (isTokenValid) return;
	  
	  System.out.println("Token is valid");
	  
	  Beneficiary beneficiary = new Beneficiary(payouts); BeneficiaryDetails
	  beneficiaryDetails = new BeneficiaryDetails(); boolean flag = false;
	  
	  try { System.out.println("Trying to fetch beneficiary based on beneId");
	  System.out.println("" +
	  beneficiary.getBeneficiaryDetails("SanthoshReddy1993")); beneficiaryDetails =
	  beneficiary.getBeneficiaryDetails("SanthoshReddy1993"); } catch
	  (ResourceDoesntExistException x) {
	  
	  log.warning(x.getMessage());
	  System.out.println("Trying to fetch beneficiary based on account details");
	  try { System.out.println("" + beneficiary.getBeneficiaryId("111901517737",
	  "ICIC0001119")); String beneId = beneficiary.getBeneficiaryId("111901517737",
	  "ICIC0001119"); beneficiaryDetails =
	  beneficiary.getBeneficiaryDetails(beneId);
	  System.out.println(beneficiaryDetails.toString()); } catch
	  (ResourceDoesntExistException y) { log.warning(y.getMessage()); flag = true;
	  } }
	  
	  if (flag == true) {
	  System.out.println("Beneficiary not found so Adding Beneficiary details");
	  
	  beneficiaryDetails = new
	  BeneficiaryDetails().setBeneId("bank_success").setName("bsuccess")
	  .setEmail("info@sharp6.com").setPhone("9393333577").setBankAccount(
	  "111901517737")
	  .setIfsc("ICIC0001119").setAddress1("HYDERABAD").setCity("HYDERABAD").
	  setState("TELANGANA") .setPincode("500081");
	  
	 /* beneId=bank_success, name=bsuccess, email=suneel@cashfree.com,
	  phone=7709736537, bankAccount=000100289877623, ifsc=SBIN0008752,
	  address1=Bangalore, city=, state=, pincode=0)*/ try { System.out.println("" +
	  beneficiary.addBeneficiary(beneficiaryDetails));
	  System.out.println("Beneficiary added"); } catch
	  (ResourceAlreadyExistsException x) { log.warning(x.getMessage()); } }
	  
	  System.out.println("initiating Transfer Request"); Transfers transfers = new
	  Transfers(payouts); String transferId = "javasdktesdtransferid" +
	  ThreadLocalRandom.current().nextInt(0, 1000000);
	  
	  RequestTransferRequest request = new
	  RequestTransferRequest().setBeneId(beneficiaryDetails.getBeneId())
	  .setAmount(new BigDecimal("1.00")).setTransferId(transferId); try {
	  System.out.println("" + transfers.requestTransfer(request)); } catch
	  (IllegalPayloadException x) { System.out.println(x.getMessage()); }
	  
	  System.out.println("Getting Transfer Status for the transaction"); try {
	  System.out.println("" + transfers.getTransferStatus(null, transferId)); }
	  catch (ResourceDoesntExistException x) { log.warning(x.getMessage()); }
	  
	  System.out.println("Getting the Transfer Details"); System.out.println("" +
	  transfers.getTransfers(10, null, null));
	  
	  // beneficiary.removeBeneficiary(beneficiaryDetails.getBeneId()); }
	  
	 
	/*
	 * public static void main(String[] args) { Payouts payouts =
	 * Payouts.getInstance(Environment.PRODUCTION, "CF191251C943M362HA3KP9U0GIDG",
	 * "7f3dc594e6decdc7f836dd61e9bdb622a95f4c38"); payouts.init(); Beneficiary
	 * beneficiary = new Beneficiary(payouts);
	 * 
	 * BeneficiaryDetails beneficiaryDetails = new
	 * BeneficiaryDetails().setBeneId("CHARY123")
	 * .setName("uppunutnala prashanth").setEmail("INFO@SHARP6.com").setPhone(
	 * "7680006755")
	 * .setBankAccount("111401532066").setIfsc("ICIC0001114").setAddress1(
	 * "GACHIBOWLI").setCity("Hyderabad")
	 * .setState("Telangana").setPincode("500032");
	 * 
	 * boolean addBeneficiary = beneficiary.addBeneficiary(beneficiaryDetails);
	 * System.out.println("addBeneficiary "+addBeneficiary);
	 * 
	 * }
	 */
	}
}
