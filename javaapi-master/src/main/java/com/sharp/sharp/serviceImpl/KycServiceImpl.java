package com.sharp.sharp.serviceImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharp.sharp.controller.BankAccount;
import com.sharp.sharp.entity.BankNames;
import com.sharp.sharp.entity.PanCard;
import com.sharp.sharp.entity.States;
import com.sharp.sharp.service.KycService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.Sharp6Validation;

@Service
@Transactional
public class KycServiceImpl implements KycService {
	@Autowired
	private EntityManager entityManager;

	public String savePancardDetails(PanCard pancard) {
		Session session = entityManager.unwrap(Session.class);
		try {
			session.save(pancard);
			return Constants.SUCCESS;

		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}

	}

	@SuppressWarnings("deprecation")
	public PanCard getPancardByUserId(String userId,String panNumber) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(PanCard.class);
			criteria.add(Restrictions.eq("userid", userId));
			if(panNumber!=null) {
			criteria.add(Restrictions.eq("panNumber", panNumber));
			}
			PanCard uniqueResult = (PanCard) criteria.uniqueResult();

			return uniqueResult;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public BankAccount saveBankwithIfsc(BankAccount bankaccount) {
		Session session = entityManager.unwrap(Session.class);
		try {
			session.save(bankaccount);
			return bankaccount;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<BankAccount> getBankDetailsByuserId(String userid) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Query query = session.createQuery("from BankAccount where userid = :userid");
			query.setParameter("userid", userid);

			List<BankAccount> list = query.list();
			if (!Sharp6Validation.isEmpty(list))
				return list;
			else
				return null;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<States> getAllProvinces() {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		try {
			SQLQuery query = session.createSQLQuery("select stateid,statename from states");
			List<States> list2 = query.setResultTransformer(Transformers.aliasToBean(States.class)).list();

			return list2;
		} catch (Exception e) {
			return null;

		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BankNames> getBankNamesList() {
		Session session = entityManager.unwrap(Session.class);
		try {
			SQLQuery query = session.createSQLQuery("select bankno as bankNo,bankname as bankName from banknames");
			List<BankNames> list2 = query.setResultTransformer(Transformers.aliasToBean(BankNames.class)).list();

			return list2;
		} catch (Exception e) {
			return null;

		} finally {
			session.close();
		}
	}

	@Override
	public PanCard updatePanStatus(PanCard pancard, int validationFlag) {
		Session session = entityManager.unwrap(Session.class);
		pancard.setValidationFlag(validationFlag);
		session.saveOrUpdate(pancard);
		session.flush();
		session.close();
		return pancard;

	}

	@SuppressWarnings("deprecation")
	@Override
	public BankAccount isbenfiExist(String beneId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(BankAccount.class);
			criteria.add(Restrictions.eq("beneId", beneId));
			List<BankAccount> list = criteria.list();
			if(list.isEmpty())
				return null;
			else
			return list.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<PanCard> getAllPancards() {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(PanCard.class);
			
			List<PanCard> listObj =  criteria.list();

			return listObj;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}
}
