package com.sharp.sharp.serviceImpl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.EntityManager;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.sharp.entity.LoginSession;
import com.sharp.sharp.entity.ModelQuestions;
import com.sharp.sharp.entity.Questions;
import com.sharp.sharp.entity.ReviewQuestions;
import com.sharp.sharp.entity.UserAnswerHistory;
import com.sharp.sharp.entity.UserMaster;
import com.sharp.sharp.repository.LoginRepository;
import com.sharp.sharp.repository.QeryRepository;
import com.sharp.sharp.repository.UserRepository;
import com.sharp.sharp.service.UserService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.MyBusinessException;
import com.sharp.sharp.util.Sharp6Validation;

@Component
@Transactional
public class UserserviceImplemantation implements UserService {
	private static final Logger logger = Logger.getLogger(UserserviceImplemantation.class);
	@Autowired
	private UserRepository userdao;
	@Autowired
	private LoginRepository loginDao;
	@Autowired
	private QeryRepository queryDao;
	@Autowired
	private EntityManager entityManager;

	@Override
	public UserMaster newUserRegister(UserMaster user) {

		UserMaster userObj = new UserMaster();
		try {
			userObj = userdao.save(user);

			return userObj;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	@Override
	public UserMaster userLogin(LoginSession entity) {
		try {

			UserMaster userLogin = getUser(entity);
			// if(!Sharp6Validation.isEmpty(userLogin.getUserlname()))
			// userLogin.setUserName(userLogin.getUserfname() + " " +
			// userLogin.getUserlname());
			System.out.println(userLogin.getUserid());
			return userLogin;

		} catch (Exception e) {
			return null;
		}

	}

	public UserMaster getUser(LoginSession entity) {
		try {
			UserMaster user = userdao.getUser(entity.getMobileNumber(), entity.getPassword());
			if (!Sharp6Validation.isEmpty(user) && user.isActivestatus()) {
				// user.setUserName(user.getUserfname() + " " + user.getUserlname());
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public String changePassword(UserMaster user) {
		try {
			UserMaster byId = userdao.getById(user.getUserid());
			byId.setConfirmPassword(user.getConfirmPassword());
			UserMaster save = userdao.save(byId);
			return Constants.SUCCESS;
		} catch (Exception e) {
			return Constants.FAILURE;
		}

	}

	@Override
	public List<Object[]> getALLLanguages() {
		return userdao.getALLLanguages();
	}

	@Override
	public List<Questions> inserttQueries(List<Questions> question) {
		List<Questions> list = new ArrayList<Questions>();
		Session session = entityManager.unwrap(Session.class);
		try {
			for (Questions questions : question) {
				questions.setCreateddate(String.valueOf(new Timestamp(new Date().getTime())));
				questions.setAnswer(questions.getOption1());
				JSONObject obj = new JSONObject();
				obj.put("option1", questions.getOption1());
				obj.put("option2", questions.getOption2());
				obj.put("option3", questions.getOption3());
				obj.put("option4", questions.getOption4());
				questions.setOptions(obj.toString());
				// Questions q = queryDao.save(questions);
				list.add(questions);
			}
			String query = "INSERT INTO questions (qname,createddate,contestid,options,answer,bannervalue,qreviwstatusflag) VALUES";
			String s1 = "";
			for (int i = 0; i < list.size(); i++) {
				s1 = s1 + "('" + list.get(i).getQname() + "','" + list.get(i).getCreateddate() + "','"
						+ list.get(i).getContestid() + "','" + list.get(i).getOptions() + "','"
						+ list.get(i).getAnswer() + "'," +

						+list.get(i).getBannerValue() + "," + list.get(i).getQreviwStatusFlag() + "),";

			}
			String query2 = s1.substring(0, s1.length() - 1);
			System.out.println(query + query2);
			Query createQuery = session.createSQLQuery(query + query2);
			int executeUpdate = createQuery.executeUpdate();

			return list;
		} catch (Exception e) {
			e.getMessage();
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Questions> getAllquestions() {
		try {
			HashMap<String, List<Questions>> map = new HashMap<String, List<Questions>>();
			List<Questions> questions = queryDao.findAll();

//			for (Questions questions2 : questions) {
//
//				if (questions2.getContestid().equals(questions2.getContestid())) {
//
//				}
//			}
			return questions;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<UserMaster> getAllUserlist() {
		// TODO Auto-generated method stub

		try {
			List<UserMaster> userList = userdao.findAll();
			return userList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public UserMaster loginAdmin(String userName, String password) {
		// TODO Auto-generated method stub
		try {
			UserMaster userObj = userdao.adminLogin(userName, password);
			if (/*
				 * !Sharp6Validation.isEmpty(userObj) && userObj.isActivestatus() &&
				 */ userObj.getRole().equalsIgnoreCase("ADMIN")) {
				// user.setUserName(userObj.getUserfname() + " " + userObj.getUserlname());
				return userObj;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	private UserMaster adminLogin(String userName, String password) {
		Session session = entityManager.unwrap(Session.class);
		try {
			/*
			 * Query query = session.
			 * createQuery("from UserMaster where userName = :userName and password = :password"
			 * ); query.setParameter("userName", userName); query.setParameter("password",
			 * password); UserMaster uniqueResult = (UserMaster) query.uniqueResult();
			 */

			Criteria createCriteria = session.createCriteria(UserMaster.class);
			createCriteria.add(Restrictions.conjunction().add(Restrictions.eq("userName", userName))
					.add(Restrictions.eq("password", password)));
			UserMaster uniqueResult = (UserMaster) createCriteria.uniqueResult();
			return uniqueResult;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public UserMaster isMobileNumberExist(String mobileNumber) {
		// TODO Auto-generated method stub
		try {
			UserMaster mobileNumberExist = userdao.isMobileNumberExist(mobileNumber);
			if (!Sharp6Validation.isEmpty(mobileNumberExist)) {
				return mobileNumberExist;
			} else {
				return null;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<ReviewQuestions> reviewQuestion(List<ReviewQuestions> review) {
		try {

			Session session = entityManager.unwrap(Session.class);

			for (ReviewQuestions reviewQuestions : review) {
				session.save(reviewQuestions);
			}
			session.close();
			return review;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}

	}

	@Override
	public List<Questions> getQuestionsBycontestId(String contestId) {
		// Session session = entityManager.unwrap(Session.class);
		try {

//			Query query = session.createQuery("from Questions where contestid = :contestid");
//			query.setParameter("contestid", contestId);
			List<Questions> qlist = queryDao.getAllQuestionsByContestId(contestId);
			for (Questions questions : qlist) {
				questions.setAnswer(null);
			}

			return qlist;
		} catch (Exception e) {
			return null;
		} finally {
			// session.close();
		}
	}

	@Override
	public UserAnswerHistory submitAnswers(UserAnswerHistory userAnswers) throws MyBusinessException {
		Session session = entityManager.unwrap(Session.class);
		try {

			JSONObject obj = new JSONObject();
			if (userAnswers.getBannervalue() == 1)
				obj.put("q1", userAnswers.getQ1id());
			if (userAnswers.getBannervalue() == 2) {
				obj.put("q1", userAnswers.getQ1id());
				obj.put("q2", userAnswers.getQ2id());
			}
			if (userAnswers.getBannervalue() == 3) {
				obj.put("q1", userAnswers.getQ1id());
				obj.put("q2", userAnswers.getQ2id());
				obj.put("q3", userAnswers.getQ3id());
			}
			if (userAnswers.getBannervalue() == 4) {
				obj.put("q1", userAnswers.getQ1id());
				obj.put("q2", userAnswers.getQ2id());
				obj.put("q3", userAnswers.getQ3id());
				obj.put("q4", userAnswers.getQ4id());
			}
			if (userAnswers.getBannervalue() == 5) {
				obj.put("q1", userAnswers.getQ1id());
				obj.put("q2", userAnswers.getQ2id());
				obj.put("q3", userAnswers.getQ3id());
				obj.put("q4", userAnswers.getQ4id());
				obj.put("q5", userAnswers.getQ5id());
			}
			if (userAnswers.getBannervalue() == 6) {
				obj.put("q1", userAnswers.getQ1id());
				obj.put("q2", userAnswers.getQ2id());
				obj.put("q3", userAnswers.getQ3id());
				obj.put("q4", userAnswers.getQ4id());
				obj.put("q5", userAnswers.getQ5id());
				obj.put("q6", userAnswers.getQ6id());
			}
			userAnswers.setqIds(obj.toString());
			JSONObject obj1 = new JSONObject();
			if (userAnswers.getBannervalue() == 1)
				obj1.put("answer1", userAnswers.getQ1answer());
			if (userAnswers.getBannervalue() == 2) {
				obj1.put("answer1", userAnswers.getQ1answer());
				obj1.put("answer2", userAnswers.getQ2answer());
			}
			if (userAnswers.getBannervalue() == 3) {
				obj1.put("answer1", userAnswers.getQ1answer());
				obj1.put("answer2", userAnswers.getQ2answer());
				obj1.put("answer3", userAnswers.getQ3answer());
			}
			if (userAnswers.getBannervalue() == 4) {
				obj1.put("answer1", userAnswers.getQ1answer());
				obj1.put("answer2", userAnswers.getQ2answer());
				obj1.put("answer3", userAnswers.getQ3answer());
				obj1.put("answer4", userAnswers.getQ4answer());
			}
			if (userAnswers.getBannervalue() == 5) {
				obj1.put("answer1", userAnswers.getQ1answer());
				obj1.put("answer2", userAnswers.getQ2answer());
				obj1.put("answer3", userAnswers.getQ3answer());
				obj1.put("answer4", userAnswers.getQ4answer());
				obj1.put("answer5", userAnswers.getQ5answer());
			}
			if (userAnswers.getBannervalue() == 6) {
				obj1.put("answer1", userAnswers.getQ1answer());
				obj1.put("answer2", userAnswers.getQ2answer());
				obj1.put("answer3", userAnswers.getQ3answer());
				obj1.put("answer4", userAnswers.getQ4answer());
				obj1.put("answer5", userAnswers.getQ5answer());
				obj1.put("answer6", userAnswers.getQ6answer());
			}

			userAnswers.setChoosenAnswers(obj1.toString());
			JSONObject obj2 = new JSONObject();
			if (userAnswers.getBannervalue() == 1)
				obj2.put("q1answertime", userAnswers.getQ1answertime());
			if (userAnswers.getBannervalue() == 2) {
				obj2.put("q1answertime", userAnswers.getQ1answertime());
				obj2.put("q2answertime", userAnswers.getQ2answertime());
			}
			if (userAnswers.getBannervalue() == 3) {
				obj2.put("q1answertime", userAnswers.getQ1answertime());
				obj2.put("q2answertime", userAnswers.getQ2answertime());
				obj2.put("q3answertime", userAnswers.getQ3answertime());

			}
			if (userAnswers.getBannervalue() == 4) {
				obj2.put("q1answertime", userAnswers.getQ1answertime());
				obj2.put("q2answertime", userAnswers.getQ2answertime());
				obj2.put("q3answertime", userAnswers.getQ3answertime());
				obj2.put("q4answertime", userAnswers.getQ4answertime());
			}
			if (userAnswers.getBannervalue() == 5) {
				obj2.put("q1answertime", userAnswers.getQ1answertime());
				obj2.put("q2answertime", userAnswers.getQ2answertime());
				obj2.put("q3answertime", userAnswers.getQ3answertime());
				obj2.put("q4answertime", userAnswers.getQ4answertime());
				obj2.put("q5answertime", userAnswers.getQ5answertime());

			}
			if (userAnswers.getBannervalue() == 6) {
				obj2.put("q1answertime", userAnswers.getQ1answertime());
				obj2.put("q2answertime", userAnswers.getQ2answertime());
				obj2.put("q3answertime", userAnswers.getQ3answertime());
				obj2.put("q4answertime", userAnswers.getQ4answertime());
				obj2.put("q5answertime", userAnswers.getQ5answertime());
				obj2.put("q6answertime", userAnswers.getQ6answertime());
			}

			userAnswers.setEachAnswerTime(obj2.toString());

			Serializable save = session.save(userAnswers);
			session.close();
			List<Questions> listOfQuestions = getquestionsHistoryByContestId(userAnswers.getContestId(),
					userAnswers.getUserId());
			int correctCount = 0;
			for (Questions questions : listOfQuestions) {
				if (questions.isAnswerStatus() == true) {
					correctCount = correctCount + 1;
				}
			}
			SQLQuery query = session.createSQLQuery(
					"update contest_and_userwise_answers set correctanswerscount = :correctCount where contestid = :contestId and userid = :userId and totalquestions = :totalquestions");
			query.setParameter("correctCount", correctCount);
			query.setParameter("contestId", userAnswers.getContestId());
			query.setParameter("userId", userAnswers.getUserId());
			query.setParameter("totalquestions", listOfQuestions.size());
			query.executeUpdate();
			session.close();
			userAnswers.setCorrectanswerscount(correctCount);
			return userAnswers;
		} catch (Exception e) {
			throw new MyBusinessException("Error while submitting the answers", e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
		} finally {
			session.close();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public List<UserAnswerHistory> getQuestionshistorybyuserId(String userId) throws MyBusinessException {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(UserAnswerHistory.class);
			criteria.add(Restrictions.eq("userId", userId));
			List<UserAnswerHistory> list = criteria.list();

			return list;
		} catch (Exception e) {
			throw new MyBusinessException(e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
		} finally {
			session.close();
		}
	}

	@Override
	public List<Questions> getquestionsHistoryByContestId(String contestId, String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Query query = session
					.createQuery("from UserAnswerHistory where contestId = :contestid and userId = :userId");
			query.setParameter("contestid", contestId);
			query.setParameter("userId", userId);
			List<UserAnswerHistory> list = query.list();
			session.close();
			if (!list.isEmpty()) {
				String getqIds = list.get(0).getqIds();
				JSONObject json = new JSONObject(getqIds);
				System.out.println(json.toString());
				ArrayList<Integer> qids = new ArrayList<Integer>();
				if (json.has("q1")) {
					qids.add(json.getInt("q1"));
				}
				if (json.has("q2")) {

					qids.add(json.getInt("q2"));
				}
				if (json.has("q3")) {

					qids.add(json.getInt("q3"));
				}
				if (json.has("q4")) {
					qids.add(json.getInt("q4"));
				}
				if (json.has("q5")) {
					qids.add(json.getInt("q5"));
				}
				if (json.has("q6")) {
					qids.add(json.getInt("q6"));
				}
				String answer = list.get(0).getChoosenAnswers();
				JSONObject json1 = new JSONObject(answer);
				System.out.println(json.toString());
				ArrayList<String> answers = new ArrayList<String>();
				if (json1.has("answer1")) {

					answers.add(json1.getString("answer1"));

				}
				if (json1.has("answer2")) {

					answers.add(json1.getString("answer2"));
				}
				if (json1.has("answer3")) {

					answers.add(json1.getString("answer3"));
				}
				if (json1.has("answer4")) {

					answers.add(json1.getString("answer4"));
				}
				if (json1.has("answer5")) {

					answers.add(json1.getString("answer5"));
				}
				if (json1.has("answer6")) {

					answers.add(json1.getString("answer6"));
				}

				List<Questions> getquestionsbyQids = getquestionsbyQidswithAnswers(qids);
				System.out.println(getquestionsbyQids.size() + "===========>");
				for (int i = 0; i < getquestionsbyQids.size(); i++) {

					if (getquestionsbyQids.get(i).getAnswer().equals(answers.get(i))) {
						getquestionsbyQids.get(i).setAnswerStatus(true);
					}
					getquestionsbyQids.get(i).setUserAnswer(answers.get(i));

				}
				return getquestionsbyQids;
			} else {

				return null;
			}

		} catch (Exception e) {
			e.getMessage();
			return null;
		} finally {
			session.close();
		}
	}

	private List<Questions> getquestionsbyQidswithAnswers(ArrayList<Integer> qids) {
		Session session = entityManager.unwrap(Session.class);
		try {
			String s = "select qid,qname,qdesc,createddate,contestid,options,answer from questions where qid in (";
			for (Integer integer : qids) {
				s = s + integer + ",";
			}
			StringBuffer sb = new StringBuffer(s); // invoking the method
			sb.deleteCharAt(sb.length() - 1);
			s = sb.toString() + ")";

			Query query = session.createSQLQuery(s);

			List<Questions> list = query.setResultTransformer(Transformers.aliasToBean(Questions.class)).list();
			return list;
		} catch (Exception e) {
			e.getMessage();
			return null;
		} finally {
			session.close();

		}
	}

	private List<Questions> getquestionsbyQids(ArrayList<Integer> qids) {

		Session session = entityManager.unwrap(Session.class);
		try {
			String s = "select qid,qname,qdesc,createddate,contestid,options,answer from questions where qid in (";
			for (Integer integer : qids) {
				s = s + integer + ",";
			}
			StringBuffer sb = new StringBuffer(s); // invoking the method
			sb.deleteCharAt(sb.length() - 1);
			s = sb.toString() + ")";

			Query query = session.createSQLQuery(s);

			List<Questions> list = query.setResultTransformer(Transformers.aliasToBean(Questions.class)).list();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();

		}
	}

	private List<Questions> getContestQuestionsandAnswersBYContestId(String contestId) {

		Session session = entityManager.unwrap(Session.class);
		try {
			String s = "select qid,qname,qdesc,createddate,contestid,options,answer from questions where contestid = "
					+ contestId;

			Query query = session.createSQLQuery(s);

			List<Questions> list = query.setResultTransformer(Transformers.aliasToBean(Questions.class)).list();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();

		}
	}

	@SuppressWarnings("deprecation")
	public List<UserMaster> getuserNameByList(List<String> users) {
		Session session = entityManager.unwrap(Session.class);
		try {
			users.removeAll(Collections.singleton(null));
			Criteria criteria = session.createCriteria(UserMaster.class);
			criteria.add(Restrictions.in("userid", users));
			List<UserMaster> list = criteria.list();

			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public ModelQuestions insertModelQuestions(ModelQuestions question) {

		Session session = entityManager.unwrap(Session.class);
		try {
			for (int i = 0; i < question.getListOfModelQuestions().size(); i++) {
				ModelQuestions modelQuestions = new ModelQuestions();
				modelQuestions.setContestDate(question.getContestDate());
				modelQuestions.setContestTime(question.getContestTime());
				modelQuestions.setQuestion(question.getListOfModelQuestions().get(i));
				modelQuestions.setContestId(question.getContestId());
				modelQuestions.setContestName(question.getContestName());
				modelQuestions.setCreatedDate(new Timestamp(new Date().getTime()));
				session.save(modelQuestions);
				session.close();
			}

			return question;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<ModelQuestions> getmodelQuestionsBycontestId(String contestId) {
		Session session = entityManager.unwrap(Session.class);
		try {

			Query query = session.createQuery("from ModelQuestions where contestId = :contestid");
			query.setParameter("contestid", contestId);
			List<ModelQuestions> qlist = query.list();

			return qlist;
		} catch (Exception e) {
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public String removeModelQuestionsByCOntestId(String contestId) {
		List<ModelQuestions> getmodelQuestionsBycontestId = getmodelQuestionsBycontestId(contestId);

		Session session = entityManager.unwrap(Session.class);
		try {
			for (ModelQuestions modelQuestions : getmodelQuestionsBycontestId) {
				session.delete(modelQuestions);
				// session.beginTransaction().commit();
				session.close();
			}
			return Constants.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ModelQuestions> getAllInprogressedcontestsModelquestions() {
		Session session = entityManager.unwrap(Session.class);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		try {
			Criteria criteria = session.createCriteria(ModelQuestions.class);
			List<ModelQuestions> list = criteria.list();
			ArrayList<ModelQuestions> list2 = new ArrayList<ModelQuestions>();
			for (ModelQuestions modelQuestions : list) {

				LocalDate date = LocalDate.parse(modelQuestions.getContestDate(), format);

				LocalTime lt = LocalTime.parse(modelQuestions.getContestTime());
				LocalDateTime ldt = LocalDateTime.of(date, lt);
				if (LocalDateTime.now().isBefore(((ldt)))) {
					// modelQuestions.setStatusFlag(true);
					list2.add(modelQuestions);

				}

			}
			return list2;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public UserMaster blockorActiveUser(String userId) {
		Session session = entityManager.unwrap(Session.class);
		UserMaster user = getUser(userId);
		try {
			if (user.isActivestatus() == true) {
				user.setActivestatus(false);
				session.saveOrUpdate(user);

			} else {
				user.setActivestatus(true);
				session.saveOrUpdate(user);
				/*
				 * SQLQuery query = session
				 * .createSQLQuery("UPDATE UserMaster SET activestatus = " + 1 +
				 * " WHERE userid = " + userId); List<UserMaster> list = query.list();
				 */

			}
			// session.flush();

			return user;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("deprecation")
	private UserMaster getUser(String userId) {
		Session session = entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(UserMaster.class);
		criteria.add(Restrictions.eq("userid", userId));
		UserMaster result = (UserMaster) criteria.uniqueResult();
		return result;

	}

	@Override
	public UserMaster getuserById(String userId) {
		UserMaster user = getUser(userId);
		return user;
	}

	@Override
	public String updateUserDeviceToken(String userId, String token) {
		Session session = entityManager.unwrap(Session.class);
		Query<?> query = session
				.createQuery("update UserMaster set user_dvice_tkn =:user_dvice_tkn where userid =:userid");
		query.setParameter("user_dvice_tkn", token);
		query.setParameter("userid", userId);
		int executeUpdate = query.executeUpdate();
		if (executeUpdate == 1)
			return Constants.SUCCESS;
		else
			return Constants.FAILURE;

	}

	@Override
	@Transactional
	public String updateKalesTOUser(String userId, double mainkales) {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("update UserMaster set mainkales =:mainkales where userid =:userid");
		query.setParameter("mainkales", mainkales);
		query.setParameter("userid", userId);
		int executeUpdate = query.executeUpdate();

		if (executeUpdate == 1)
			return Constants.SUCCESS;
		else
			return Constants.FAILURE;

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isUserSubmitAnswers(String userId, String contestId) {
		Session session = entityManager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(UserAnswerHistory.class);
		Criteria add = criteria.add(Restrictions.conjunction().add(Restrictions.eq("userId", userId))
				.add(Restrictions.eq("contestId", contestId)));
		List<UserAnswerHistory> list = add.list();
		if (list.isEmpty())
			return false;
		else
			return true;

	}

}
