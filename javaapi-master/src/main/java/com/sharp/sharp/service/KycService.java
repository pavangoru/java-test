package com.sharp.sharp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sharp.sharp.controller.BankAccount;
import com.sharp.sharp.entity.BankNames;
import com.sharp.sharp.entity.PanCard;
import com.sharp.sharp.entity.States;

@Service
public interface KycService {

	

	public String savePancardDetails(PanCard pancard);

	public PanCard getPancardByUserId(String userId, String panNumber);

	public BankAccount saveBankwithIfsc(BankAccount bankaccount);

	public List<BankAccount> getBankDetailsByuserId(String userid);

	public List<States> getAllProvinces();

	public List<BankNames> getBankNamesList();

	public PanCard updatePanStatus(PanCard pancard, int validationFlag);

	public BankAccount isbenfiExist(String beneId);

	public List<PanCard> getAllPancards();
}
