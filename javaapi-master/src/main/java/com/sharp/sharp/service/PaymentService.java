package com.sharp.sharp.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sharp.sharp.entity.Lables;
import com.sharp.sharp.entity.Payments;
import com.sharp.sharp.entity.Sharp6Wallet;
import com.sharp.sharp.entity.ShuffleMoney;
import com.sharp.sharp.entity.ShufflieListVivo;
import com.sharp.sharp.entity.TransferKalesVivo;

@Service
@Transactional
public interface PaymentService {

	

	Payments savePayment(Payments payment);

	List<Payments> getAllPayments(String userId);

	Optional<Payments> getTransaction(int id);

	String walletToWalletKalesTransfer(TransferKalesVivo payment);

	List<Payments> addKalesToWallet(Payments payment);

	ShufflieListVivo saveLableShuffleMoney(ShufflieListVivo shuffle);

	ShuffleMoney getShuffleMoney(String shuffleId);

	List<Lables> getAllLables();

	String remooveLabeShuffleMoney(int lableId);

	Sharp6Wallet getMainKales(String userId);

	Sharp6Wallet saveMoneytoWalet(Sharp6Wallet wallet);

	Payments updatePaymentStatus(Payments payment);

	String remooveEntryAmountFromWallet(int lableId, String contestId, String userId);

	String isWalletExistBYId(String userId);

	List<Payments> getAlTransactionsbyUserId(String userId);

	List<Payments> getTransferTransactionsbyUserId(String userId);

	List<Payments> getWithDrawTransactions(String userId);

	List<Payments> getTransferTransactionByUserId(String userId);

	List<Payments> getAddingTransactionsbyUserId(String userId);

	String getLatestWithdrawalTime(String userId);

	boolean isMoneyAddedToWallet(String contestId);

	List<Payments> getWinningAmountforWithDrwal(String userId) throws Exception;

	Sharp6Wallet savetoWalet(Sharp6Wallet wallet);

}
