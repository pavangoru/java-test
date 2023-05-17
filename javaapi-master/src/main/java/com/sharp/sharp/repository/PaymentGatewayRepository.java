package com.sharp.sharp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.Payments;

@Repository
public interface PaymentGatewayRepository extends JpaRepository<Payments, Integer> {
	@Query(value = "select * payments  WHERE userid =?1 ", nativeQuery = true)
	List<Payments> getAllPayments(String userId);

	@Query(value = "select created_at from payments  where userid = ?1 and status = 'SUCCESS' and transcationtype = 'WITHDRAW' and (withdraw_status='intiated' or withdraw_status='settled') order by created_at desc limit 1;", nativeQuery = true)
	String getLatestWithdrawalTime(String userId);

	// and paymentID > ?2
	@Query(value = "SELECT sum(amount) FROM fact6.payments where status = 'SUCCESS' and transcationtype ='WITHDRAW' and (withdraw_status='intiated' or withdraw_status='settled')  and userID=?1 ;", nativeQuery = true)
	String getWithDrawnAmount(String userId, Integer paymentID);

	List<Payments> findBywithdrawStatus(String status);

	@Query(value = "select * from payments where paymentid=?2 and userid=?1 and withdraw_status='intiated';", nativeQuery = true)
	Payments findbyuseridandpaymentstatus(String userid, Integer paymentid);

	@Query(value = "select * from payments where transcationtype='WINNINGS' and status='SUCCESS' and userid=?1 and created_at >?2 order by paymentid DESC;", nativeQuery = true)
	List<Payments> getWinningsAmount(String userId, String latestWithdrawalTime);
	
	@Query(value = "select * from payments where transcationtype='WINNINGS' and status='SUCCESS' and userid=?1 order by paymentid DESC;", nativeQuery = true)
	List<Payments> getWinningsAmount(String userId);
	@Query(value="select count(*) from payments where transactionid=?1",nativeQuery = true)
	Integer checkTransaction(String transactionid);
}
