package com.sharp.sharp.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.sharp.entity.Lables;
import com.sharp.sharp.entity.Payments;
import com.sharp.sharp.entity.Sharp6Wallet;
import com.sharp.sharp.entity.ShuffleMoney;
import com.sharp.sharp.entity.ShufflieListVivo;
import com.sharp.sharp.entity.TransferKalesVivo;
import com.sharp.sharp.repository.LableRepository;
import com.sharp.sharp.repository.PaymentGatewayRepository;
import com.sharp.sharp.service.PaymentService;
import com.sharp.sharp.service.UserService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.Sharp6Validation;

@Component
@Transactional
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentGatewayRepository gatewayDao;
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private LableRepository lableDao;

	@Autowired
	private UserService userService;

	@Override
	public Payments savePayment(Payments payment) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Payments retObj = gatewayDao.save(payment);
			// session.saveOrUpdate(payment);
			return retObj;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Payments> getAllPayments(String userId) {
		// TODO Auto-generated method stub
		try {
			List<Payments> transactionList = gatewayDao.getAllPayments(userId);
			return transactionList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public Optional<Payments> getTransaction(int id) {
		// TODO Auto-generated method stub
		try {
			Optional<Payments> transaction = gatewayDao.findById(id);
			return transaction;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	@Override
	public String walletToWalletKalesTransfer(TransferKalesVivo payment) {
		Session session = entityManager.unwrap(Session.class);

		try {
			Sharp6Wallet mainKales = getMainKales(payment.getSender());
			if (!Sharp6Validation.isEmpty(mainKales) && mainKales.getMainkales() >= payment.getTransferKales()) {
				// checks if reciver fact6 user orot
				Sharp6Wallet reciever = getMainKales(payment.getReciever());
				if (!Sharp6Validation.isEmpty(reciever)) {
					mainKales.setMainkales(mainKales.getMainkales() - payment.getTransferKales());
					userService.updateKalesTOUser(mainKales.getUserid(), mainKales.getMainkales());
					reciever.setMainkales(reciever.getMainkales() + payment.getTransferKales());
					userService.updateKalesTOUser(reciever.getUserid(), reciever.getMainkales());
					Sharp6Wallet saveMoneytoWalet = saveMoneytoWalet(mainKales);
					Sharp6Wallet saveMoneytoWalet1 = saveMoneytoWalet(reciever);
					Payments payments = new Payments();
					// payments.setTransactionid("txn"+String.valueOf(new Date().getTime()));
					payments.setUserid(payment.getSender());
					payments.setReciever(payment.getReciever());
					payments.setAmount(payment.getTransferKales());
					payments.setCurrency("INR");
					payments.setTranscationtype("TRANSFER");
					String valueOf = String.valueOf(new Date().getTime());
					payments.setCreated_at(valueOf.substring(0, valueOf.length() - 3));
					payments.setReceipt("order_rcptid_sharp6_send_money_to_" + payment.getReciever() + "from"
							+ payment.getSender() + "on_" + new Date().getTime());
					payments.setStatus(Constants.SUCCESS);
					savePayment(payments);

					/*
					 * if (!Sharp6Validation.isEmpty(saveMoneytoWalet)) {
					 * reciever.setMainkales(reciever.getMainkales() + payment.getTransferKales());
					 * Sharp6Wallet saveMoneytoWalet1 = saveMoneytoWalet(reciever); Payments
					 * payments1 = new Payments(); payments1.setUserid(payment.getReciever());
					 * payments1.setAmount(payment.getTransferKales());
					 * payments1.setCurrency("INR");
					 * payments1.setReceipt("order_rcptid_sharp6_recieve_money_from_" +
					 * payment.getSender() + "on_" + new Date().getTime());
					 * payments1.setStatus(Constants.SUCCESS); savePayment(payments1); if
					 * (Sharp6Validation.isEmpty(saveMoneytoWalet1)) {
					 * mainKales.setMainkales(mainKales.getMainkales() +
					 * payment.getTransferKales()); Sharp6Wallet saveMoneytoWalet3 =
					 * saveMoneytoWalet(reciever); Payments payments2 = new Payments();
					 * payments2.setUserid(payment.getSender());
					 * payments2.setAmount(payment.getTransferKales());
					 * payments2.setCurrency("INR");
					 * payments2.setReceipt("order_rcptid_sharp6_send_monyt_to_" +
					 * payment.getReciever() + "on_" + new Date().getTime());
					 * payments2.setStatus(Constants.SUCCESS); savePayment(payments2); return
					 * "Kales Not Transferred"; } } else {
					 * 
					 * return "Kales Not Transferred"; }
					 */ } else {
					return "Invalid Reciever MobileNumber";
				}

				return "Transfer Succesfull";
			} else {
				return "Insufficient Kales";
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Payments> addKalesToWallet(Payments payment) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ShufflieListVivo saveLableShuffleMoney(ShufflieListVivo shuffle) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Lables lable = new Lables();
			lable.setLablename(shuffle.getLablename());
			lable.setPoolamount(shuffle.getPoolamount());
			lable.setGameAmount(shuffle.getGameamount());
			lable.setTotalAmount(shuffle.getTotalamount());
			lable.setTotalPlayers(shuffle.getTotalplayers());
			lable.setCraeteddate(new Timestamp(new Date().getTime()));
			int saveLabe = saveLabe(lable);
			if (saveLabe != 0) {

				String str = "INSERT INTO shufflemoney (rankstart,rankend,percent,sharedamount,remaining,lableid)"
						+ "VALUES ";
				String str2 = "";

				for (int i = 0; i < shuffle.getListvivo().size(); i++) {
					shuffle.getListvivo().get(i).setLableid(saveLabe);
					str2 = str2 + "(" + shuffle.getListvivo().get(i).getRankstart() + ","
							+ shuffle.getListvivo().get(i).getRankend() + ","
							+ shuffle.getListvivo().get(i).getPercent() + ","
							+ shuffle.getListvivo().get(i).getSharedamount() + ","
							+ shuffle.getListvivo().get(i).getRemaining() + "," + saveLabe + "),";

				}
				StringBuffer sb = new StringBuffer(str2);
				// invoking the method
				sb.deleteCharAt(sb.length() - 1);
				str2 = sb.toString();
				SQLQuery query = session.createSQLQuery(str + str2);
				query.executeUpdate();
				// query.setResultTransformer(Transformers.aliasToBean(ShufflieListVivo.class));
				session.close();
				return shuffle;
			} else {
				return null;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	private int saveLabe(Lables lable) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Lables lables = lableDao.save(lable);

			return lables.getLableId();
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		} finally {
			session.close();
		}
	}

	@Override
	public ShuffleMoney getShuffleMoney(String shuffleId) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		try {
			ShuffleMoney shuffeMoney = session.get(ShuffleMoney.class, shuffleId);
			return shuffeMoney;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<Lables> getAllLables() {
		try {
			List<Lables> list = lableDao.findAll();
			return list;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	@Override
	public String remooveLabeShuffleMoney(int lableId) {
		Session session = entityManager.unwrap(Session.class);

		try {
			SQLQuery query = session.createSQLQuery("DELETE from lables where lableid =" + lableId);
			query.executeUpdate();
			return Constants.SUCCESS;

		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Sharp6Wallet getMainKales(String userId) {
		Session session = entityManager.unwrap(Session.class);

		try {
			Query query = session.createQuery("from Sharp6Wallet where userid =" + userId);
			List<Sharp6Wallet> list = query.list();

			return list.get(0);

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public Sharp6Wallet saveMoneytoWalet(Sharp6Wallet wallet) {
		Session session = entityManager.unwrap(Session.class);

		try {
			session.saveOrUpdate(wallet);
			userService.updateKalesTOUser(wallet.getUserid(), wallet.getMainkales());

			session.flush();
			return wallet;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public Payments updatePaymentStatus(Payments payment) {
		Session session = entityManager.unwrap(Session.class);

		try {
			payment.setUpdated_at(String.valueOf(new Date().getTime()));
			session.saveOrUpdate(payment);
			session.flush();
			return payment;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public String remooveEntryAmountFromWallet(int lableId, String contestId, String userId) {

		Session session = entityManager.unwrap(Session.class);
		String success = Constants.FAILURE;
		try {
			Criteria criteria = session.createCriteria(Lables.class);
			criteria.add(Restrictions.eq("lableId", lableId));
			Lables lable = (Lables) criteria.uniqueResult();
			session.close();

			Criteria cri = session.createCriteria(Sharp6Wallet.class);
			cri.add(Restrictions.eq("userid", userId));
			Sharp6Wallet walletList = (Sharp6Wallet) cri.uniqueResult();
			session.close();

			Long longobj = Long.parseLong(String.valueOf(lable.getGameAmount()));
			Payments payment = new Payments();

			payment.setAmount(longobj.doubleValue());
			payment.setUserid(walletList.getUserid());
			payment.setStatus(Constants.SUCCESS);
			payment.setCurrency("INR");
			payment.setReceipt("order_rcptid_sharp6_" + new Date().getTime());
			payment.setCreated_at(String.valueOf(new Date().getTime()));
			payment.setId(userId + "_joined_in_" + contestId + "with_" + lableId + "@" + new Date().getTime());
			payment.setUpdated_at(String.valueOf(new Date().getTime()));
			payment.setTranscationtype("JOIN");
			payment.setAmount(longobj.doubleValue());

			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			Sharp6Wallet mainKales = getMainKales(payment.getUserid());
			if (payment.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
				mainKales.setMainkales(mainKales.getMainkales() - payment.getAmount());
				Sharp6Wallet saveMoneytoWalet = saveMoneytoWalet(mainKales);
				if (saveMoneytoWalet != null) {
					success = Constants.SUCCESS;
					payment.setStatus(success);
				} else {
					success = Constants.FAILURE;
					payment.setStatus(success);
				}
			} else {
				success = Constants.FAILURE;
				payment.setStatus(success);

			}
			Payments savePayment = updatePaymentStatus(payment);

			return success;
		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public String isWalletExistBYId(String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria cri = session.createCriteria(Sharp6Wallet.class);
			cri.add(Restrictions.eq("userid", userId));
			List<Sharp6Wallet> list = cri.list();
			// session.saveOrUpdate(payment);
			if (list.isEmpty())
				return Constants.SUCCESS;
			else
				return Constants.FAILURE;

		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Payments> getAlTransactionsbyUserId(String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria cri = session.createCriteria(Payments.class);

			cri.add(Restrictions.disjunction().add(Restrictions.eq("userid", userId))
					.add(Restrictions.eq("reciever", userId)));
			cri.add(Restrictions.ne("status", "created"));
			cri.addOrder(Order.desc("created_at"));

			List<Payments> list = cri.list();
			// session.saveOrUpdate(payment);
			if (!list.isEmpty())
				return list;
			else
				return null;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Payments> getTransferTransactionsbyUserId(String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {

			Criteria cri = session.createCriteria(Payments.class);

			cri.add(Restrictions.conjunction().add(Restrictions.eq("userid", userId))
					.add(Restrictions.isNotNull("reciever")));
			cri.add(Restrictions.ne("status", "created"));
			cri.addOrder(Order.desc("paymentid"));
			List<Payments> list = cri.list();
			// session.saveOrUpdate(payment);

			if (!list.isEmpty())
				return list;
			else
				return null;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Payments> getWithDrawTransactions(String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(Payments.class);
			criteria.add(Restrictions.conjunction().add(Restrictions.eq("userid", userId))
					.add(Restrictions.eq("transcationtype", "WITHDRAW")));
			criteria.addOrder(Order.desc("paymentid"));
			List<Payments> list = criteria.list();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<Payments> getTransferTransactionByUserId(String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(Payments.class);
			criteria.add(Restrictions.conjunction().add(Restrictions.eq("userid", userId))
					.add(Restrictions.eq("transcationtype", "add")));
			criteria.addOrder(Order.desc("paymentid"));
			List<Payments> list = criteria.list();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<Payments> getAddingTransactionsbyUserId(String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(Payments.class);
			criteria.add(Restrictions.conjunction().add(Restrictions.eq("userid", userId))
					.add(Restrictions.eq("transcationtype", "add")));
			criteria.addOrder(Order.desc("paymentid"));
			List<Payments> list = criteria.list();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public String getLatestWithdrawalTime(String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			String retObj = gatewayDao.getLatestWithdrawalTime(userId);
			return retObj;

		} catch (Exception e) {
			System.out.println("Error while making DB call to getLatestWithdrawalTime: " + e);
			return null;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isMoneyAddedToWallet(String contestId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(Payments.class);
			List<Payments> list = criteria.add(Restrictions.eq("contestId", contestId)).list();
			if (!list.isEmpty())
				return false;
			else
				return true;
		} catch (Exception e) {
			System.out.println("Error while making DB call to getLatestWithdrawalTime: " + e);
			return true;
		} finally {
			session.close();
		}

	}

	@Override
	public List<Payments> getWinningAmountforWithDrwal(String userId) throws Exception {
		try {

			String latestWithdrawalTime = gatewayDao.getLatestWithdrawalTime(userId);
			List<Payments> list = null;
			if (latestWithdrawalTime != null) {
				list = gatewayDao.getWinningsAmount(userId, latestWithdrawalTime);
			} else {
				list = gatewayDao.getWinningsAmount(userId);

			}
			Double amount = 0.0;
			String totalWithdrawnAmount = null;

			if (!list.isEmpty()) {

				Integer paymentID = list.get(0).getPaymentid();

				totalWithdrawnAmount = gatewayDao.getWithDrawnAmount(userId, paymentID);
			}

			for (Payments payments : list) {
				amount = amount + payments.getAmount();
			}
			if (totalWithdrawnAmount != null) {
				if (Double.parseDouble(totalWithdrawnAmount) < amount) {
					amount = amount - Double.parseDouble(totalWithdrawnAmount);

					for (Payments payment : list) {

						payment.setTotalAmountWon(amount.toString());

					}
				} else {
					/*
					 * amount = Double.parseDouble(totalWithdrawnAmount)-amount; for (Payments
					 * payment : list) {
					 * 
					 * payment.setTotalAmountWon(amount.toString()); payment.setAmount(amount);
					 * 
					 * }
					 */ // if (amount < 0) {
					throw new Exception("no amount to withdraw");
					// }
					// nothing to withdraw
				}
			} else {

				for (Payments payment : list) {

					payment.setTotalAmountWon(amount.toString());

				}

			}
			
			return list;

		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}

	}

	

	@Override
	public Sharp6Wallet savetoWalet(Sharp6Wallet wallet) {
		Session session = entityManager.unwrap(Session.class);

		try {
			session.saveOrUpdate(wallet);

			session.flush();
			return wallet;
		} catch (Exception e) {
			return null;
		} finally {
			session.close();
		}

	}

}
