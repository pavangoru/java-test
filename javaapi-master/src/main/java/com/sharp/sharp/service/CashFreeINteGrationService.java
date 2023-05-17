package com.sharp.sharp.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.sharp.controller.BankAccount;
import com.sharp.sharp.entity.BankTransferPojo;
import com.sharp.sharp.entity.BenefitiaryDetails;
import com.sharp.sharp.entity.Payments;
import com.sharp.sharp.entity.TokenData;

@Service
@Transactional
public interface CashFreeINteGrationService {
	TokenData GenerateToken(String clientId, String clientSecret);

	BenefitiaryDetails getBenefitiaryDetails(String token, String benId);

	// void directTransferToBenefitiary(BenefitiaryDetails benefitiaryDetails,
	// String token);

	String directTransferToBenefitiary(BankTransferPojo transferPojo, String token);

	BankAccount addBefi(BankAccount bank);

	String getBenIdByUserId(String userid);

	public String createOrder(Payments payment) throws IOException, InterruptedException;

	BankAccount saveBenfitiary(BankAccount addBefi);

}
