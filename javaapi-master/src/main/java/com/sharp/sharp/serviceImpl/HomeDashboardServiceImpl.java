package com.sharp.sharp.serviceImpl;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.sharp.sharp.entity.Category;
import com.sharp.sharp.entity.Channel;
import com.sharp.sharp.entity.ContestJoined;
import com.sharp.sharp.entity.ContestVivo;
import com.sharp.sharp.entity.Contestdetails;
import com.sharp.sharp.entity.DistributeMoney;
import com.sharp.sharp.entity.ImagesEntity;
import com.sharp.sharp.entity.Lables;
import com.sharp.sharp.entity.LeadBoardContest;
import com.sharp.sharp.entity.LeadBoards;
import com.sharp.sharp.entity.Payments;
import com.sharp.sharp.entity.RankVivo;
import com.sharp.sharp.entity.Sharp6Wallet;
import com.sharp.sharp.entity.ShowDetails;
import com.sharp.sharp.entity.ShowDetailsVivo;
import com.sharp.sharp.entity.ShowsVivo;
import com.sharp.sharp.entity.ShuffleMoney;
import com.sharp.sharp.entity.Status;
import com.sharp.sharp.entity.Teams;
import com.sharp.sharp.entity.UserAnswerHistory;
import com.sharp.sharp.entity.UserMaster;
import com.sharp.sharp.repository.CategoryRepository;
import com.sharp.sharp.repository.ChannelRepository;
import com.sharp.sharp.repository.ContestRepository;
import com.sharp.sharp.repository.ShowRepository;
import com.sharp.sharp.repository.StatusRepository;
import com.sharp.sharp.repository.TeamRepository;
import com.sharp.sharp.service.FCMNotificationService;
import com.sharp.sharp.service.HomeDashBoardService;
import com.sharp.sharp.service.PaymentService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.Sharp6Validation;

@Component
@Transactional
@PropertySource(value = { "classpath:application.properties" })
public class HomeDashboardServiceImpl implements HomeDashBoardService {
	private static final Logger logger = Logger.getLogger(HomeDashboardServiceImpl.class);
	@Autowired
	private Environment environment;
	@Autowired
	private ShowRepository showDao;
	@Autowired
	private CategoryRepository categoryDao;
	@Autowired
	private ChannelRepository channelDao;
	@Autowired
	private ContestRepository contestDao;
	@Autowired
	private TeamRepository teamDao;
	@Autowired
	private StatusRepository statusDao;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private UserserviceImplemantation userservice;
	@Autowired
	private PaymentService gatewayService;
	@Autowired
	private FCMNotificationService fCMNotificationService;
	@Autowired
	private PaymentService payment;

	@Override
	public Category addCategory(Category cateory) {
		// TODO Auto-generated method stub
		try {

			logger.info("=============>");
			// cateory.setCategoryiconurl(environment.getRequiredProperty("imagepath") +
			// cateory.getCategoryiconId());

			cateory.setCreateddate(new Timestamp(new Date().getTime()));
			for (int i = 0; i < cateory.getSubcategory().size(); i++) {

				cateory.getSubcategory().get(i).setCreateddate(new Timestamp(new Date().getTime()));

			}

			Category obj = categoryDao.save(cateory);
			logger.info("success");
			return obj;
		} catch (Exception e) {
			logger.info("error at service implmentation");
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Category> getAllCategory() {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(Category.class);
			List<Category> list = criteria.list();
			// List<Category> findAll = categoryDao.findAll();
			// logger.info("success");
			for (Category category : list) {

				ImagesEntity categoryurlBYId = getCategoryurlBYId(category.getCategoryid());

				/*
				 * List<Subcategory> collect = category.getSubcategory().stream() .sorted((s1,
				 * s2) -> s1.getSubcategoryname().compareTo(s2.getSubcategoryname()))
				 * .collect(Collectors.toList());
				 */
				category.setSubcategory(category.getSubcategory());

				if (!Sharp6Validation.isEmpty(categoryurlBYId)) {
					category.setCategoryImageUrl(categoryurlBYId.getFileDownloadUri());
				}
			}
			return list;
		} catch (Exception e) {

			// logger.info("error at service implmentation");
			return null;

		}
	}

	@SuppressWarnings("deprecation")
	private ImagesEntity getCategoryurlBYId(int categoryid) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(ImagesEntity.class);
			criteria.add(Restrictions.conjunction().add(Restrictions.eq("categoryid", categoryid))
					.add(Restrictions.eq("defaultImage", true)));
			ImagesEntity uniqueResult = (ImagesEntity) criteria.uniqueResult();
			return uniqueResult;
		} catch (Exception e) {
			return null;

		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public Optional<Category> getCategoryBYId(Category category) {
		try {

			Optional<Category> findById = categoryDao.findById(category.getCategoryid());

			logger.info("success");
			return findById;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("error at service implmentation");
			return null;

		}
	}

	@Override
	public Channel createChannel(Channel channel) {
		// TODO Auto-generated method stub
		try {
			Channel channelObj = channelDao.save(channel);
			return channelObj;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<Channel> getAllChannels() {
		// TODO Auto-generated method stub
		try {
			List<Channel> findAll = channelDao.findAll();
			logger.info("success");
			return findAll;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("error at service implmentation");
			return null;

		}

	}

	@Override
	public Optional<Channel> getChannelById(Channel channel) {
		// TODO Auto-generated method stub
		try {

			Optional<Channel> findById = channelDao.findById(channel.getChannelid());

			logger.info("success");
			return findById;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("error at service implmentation");
			return null;

		}
	}

	@Override
	public ShowDetails crateShow(ShowDetails show) {
		// TODO Auto-generated method stub
		try {
			ShowDetails showObj = showDao.save(show);
			return showObj;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ShowsVivo> getAllShows() {

		Session session = entityManager.unwrap(Session.class);
		SessionFactory sessionFactory = session.getSessionFactory();
//				"SELECT c.categoryid ,c.categoryname ,c.categorydesc ,l.languageid ,l.languagename ,l.description ,ch.channelid ,ch.channelname ,ch.channeldesc FROM showdetails s JOIN category c ON s.categorieid = c.categoryid JOIN language l ON s.languageid = l.languageid JOIN channel ch ON s.channelid = ch.channelid ");

		Query query = session.createSQLQuery(
				"SELECT s.showid,s.show_name as showname,l.languagename as language,l.languageid,c.categoryname,c.categoryid,sc.subcategoryid,sc.subcategoryname,ch.channelid,ch.channelname FROM showdetails s JOIN category c ON s.categoryid = c.categoryid  JOIN subcategory sc ON s.subcategoryid = sc.subcategoryid JOIN language l ON s.languageid = l.languageid JOIN channel ch ON s.channelid = ch.channelid ");
		// TODO Auto-generated method stub
		List<ShowsVivo> getallshows = query.setResultTransformer(Transformers.aliasToBean(ShowsVivo.class)).list();
		// showDao.getallshows();
		return getallshows;
	}

	@Override
	public List<ShowDetailsVivo> getShowById(String showId) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		SessionFactory sessionFactory = session.getSessionFactory();
//				"SELECT c.categoryid ,c.categoryname ,c.categorydesc ,l.languageid ,l.languagename ,l.description ,ch.channelid ,ch.channelname ,ch.channeldesc FROM showdetails s JOIN category c ON s.categorieid = c.categoryid JOIN language l ON s.languageid = l.languageid JOIN channel ch ON s.channelid = ch.channelid ");

		Query query = session.createSQLQuery(
				"SELECT s.showid,s.show_name as showname,c.categoryname ,l.languagename ,ch.channelname , sy.subcategoryname FROM showdetails s JOIN category c ON s.categoryid = c.categoryid JOIN language l ON s.languageid = l.languageid JOIN channel ch ON s.channelid = ch.channelid JOIN subcategory sy ON s.subcategoryid = sy.subcategoryid and s.showid = '"
						+ showId + "'");

		List<ShowDetailsVivo> showdetailsById = query
				.setResultTransformer(Transformers.aliasToBean(ShowDetailsVivo.class)).list();

		return showdetailsById;
	}

	@Override
	public String deleteShow(String show) {
		Session session = entityManager.unwrap(Session.class);

		try {
			SQLQuery query = session.createSQLQuery("DELETE from showdetails where showid =" + show);
			query.executeUpdate();
			return Constants.SUCCESS;

		} catch (Exception e) {
			return Constants.FAILURE;
		}
	}

	@Override
	public Contestdetails createCOntest(Contestdetails contest) {
		try {
			Contestdetails contestobj = contestDao.save(contest);
			return contestobj;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Contestdetails updateContest(Contestdetails contest) {
		// TODO Auto-generated method stub
		try {
			Contestdetails contestobj = contestDao.saveAndFlush(contest);
			return contestobj;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<Contestdetails> getContestById(String contestid, String userId)

	{

		Session session = entityManager.unwrap(Session.class);

		try {
			Query query = session.createSQLQuery(
					"SELECT contestid,categoryid,channelid,contestdate,contesttime,createddate,language,languageid,showid,teamname1_shortname,teamname2_shortname,Contestname1,Contestname2,subcategoryid,channelname,categoryname,subcategoryname,team1imageid,team2imageid,show_name as showname,lableidList,bannerimage as bannerImage,bannervalue as bannerValue FROM contestdetails where contestid = '"
							+ contestid + "'");

			@SuppressWarnings("deprecation")
			List<Contestdetails> contest = query.setResultTransformer(Transformers.aliasToBean(Contestdetails.class))
					.list();
			session.close();
			List<Lables> alllablesbycontestId = getAlllablesbycontestId(contest.get(0).getLableidList());
			List<Lables> checkUserJOined = checkUserJOined(contest.get(0), userId, alllablesbycontestId);

			if (!Sharp6Validation.isEmpty(checkUserJOined)) {

				for (Lables integer : checkUserJOined) {

					for (Lables lable : alllablesbycontestId) {
						List<Integer> joinedPlayers = getjoinedPlayers(contest.get(0).getContestid(),
								lable.getLableId());
						lable.setJoinedplayers(joinedPlayers.size());
						lable.setPlayersLeft(lable.getTotalPlayers() - joinedPlayers.size());
						if (lable.getLableId() == integer.getLableId()) {

							lable.setJoinedStatus(integer.getJoinedStatus());
						}
						if (lable.getJoinedStatus() == 0 && lable.getJoinedplayers() == lable.getPlayersLeft()) {
							lable.setJoinedStatus(3);
						}

					}
				}
			} else {
				for (Lables lable : alllablesbycontestId) {
					List<Integer> joinedPlayers = getjoinedPlayers(contest.get(0).getContestid(), lable.getLableId());
					lable.setJoinedplayers(joinedPlayers.size());
					lable.setPlayersLeft(lable.getTotalPlayers() - joinedPlayers.size());

				}
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");

			LocalTime lt = LocalTime.parse(contest.get(0).getContesttime());
			System.out.println(df.format(lt.plusMinutes(3)));

			if (LocalTime.now().isBefore(lt.plusMinutes(3))) {
				if (lt.isAfter(LocalTime.now()))
					contest.get(0).setInReview(true);

			}

			CopyOnWriteArrayList<Lables> list = new CopyOnWriteArrayList<Lables>();
			list.addAll(alllablesbycontestId);
			for (Lables lables : list) {
				if (lables.getTotalPlayers() == 0 || lables.getLableStatus().equals("inactive")) {
					alllablesbycontestId.remove(lables);
				}
			}
			if (alllablesbycontestId.stream().anyMatch(e -> e.getJoinedStatus() == 1))
				contest.get(0).setJoinedInLables(true);
			contest.get(0).setUserid(userId);
			contest.get(0).setLableList(alllablesbycontestId);

			return contest;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private List<Integer> getjoinedPlayers(String contestId, int checkUserJOined) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		try {

			String query = "SELECT lableid  FROM ContestJoined where lableid = " + checkUserJOined
					+ " and joinedstatus=1 and Contestid ='" + contestId + "'";
			/*
			 * for (Integer integer : checkUserJOined) { query = query + "" + integer + ",";
			 * } StringBuffer sb = new StringBuffer(query); // invoking the method
			 * sb.deleteCharAt(sb.length() - 1); query = sb.toString() + ")";
			 */
			SQLQuery sqlQuery = session.createSQLQuery(query);
			List<Integer> list = sqlQuery.list();

			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}

	}

	@SuppressWarnings("deprecation")
	private List<Lables> checkUserJOined(Contestdetails contestdetails, String userId,
			List<Lables> alllablesbycontestId) {

		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		try {

			String query = "SELECT lableid as lableId, joinedstatus as joinedStatus FROM ContestJoined where Contestid ='"
					+ contestdetails.getContestid() + "' and userId = '" + userId + "' and lableid in (";
			for (Lables integer : alllablesbycontestId) {
				query = query + "" + integer.getLableId() + ",";
			}
			// query = query + " and joinedstatus = 1";
			StringBuffer sb = new StringBuffer(query);
			// invoking the method
			sb.deleteCharAt(sb.length() - 1);
			query = sb.toString() + ")";
			SQLQuery sqlQuery = session.createSQLQuery(query);
			List<Lables> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(Lables.class)).list();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public List<Contestdetails> getAllContests() {
		try {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			List<Contestdetails> totalcontests = contestDao.findAll();
			ArrayList<Contestdetails> list = new ArrayList<Contestdetails>();
			for (Contestdetails contestdetails : totalcontests) {

				LocalDate date = LocalDate.parse(contestdetails.getContestdate(), format);

				LocalTime lt = LocalTime.parse(contestdetails.getContesttime());
				LocalDateTime ldt = LocalDateTime.of(date, lt);
				if (LocalDateTime.now().isBefore(((ldt)))) {
					// modelQuestions.setStatusFlag(true);
					list.add(contestdetails);

				}
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public Status saveStatus(Status status) {
		// TODO Auto-generated method stub
		try {

			Status retObj = statusDao.save(status);
			return retObj;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Teams saveTeam(Teams teams) {
		// TODO Auto-generated method stub
		try {
			Teams team = teamDao.save(teams);
			return team;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<Teams> getAllTeams() {
		// TODO Auto-generated method stub
		try {
			List<Teams> teamList = teamDao.findAll();
			return teamList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<ContestVivo> getAllShowByUserId(String userId) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		SessionFactory sessionFactory = session.getSessionFactory();
		try {
			Query query = session.createSQLQuery(
					"SELECT c.categoryid ,c.categoryname ,c.categorydesc ,l.languageid ,l.languagename ,sy.subcategoryid,sy.subcategoryname,l.description , ch.channelid ,ch.channelname ,ch.channeldesc FROM showdetails s JOIN category c ON s.categoryid = c.categoryid JOIN language l ON s.languageid = l.languageid JOIN channel ch ON s.channelid = ch.channelid JOIN subcategory sy ON s.subcategoryid = sy.subcategoryid where userID = '"
							+ userId + "'");

			List<ContestVivo> contest = query.setResultTransformer(Transformers.aliasToBean(Contestdetails.class))
					.list();
			return contest;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		// showDao.getAllShowByUserId(userId);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Set<Contestdetails> getContestsByUserId(String userId) {
		Session session = entityManager.unwrap(Session.class);
		SessionFactory sessionFactory = session.getSessionFactory();
		// TODO Auto-generated method stub
		try {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			ArrayList<Contestdetails> list = new ArrayList<Contestdetails>();

			Query query = session.createSQLQuery(
					"SELECT c.contestid,c.show_name as showname,c.channelname,c.contestdate,c.contesttime,c.Contestname1,c.Contestname2,c.teamname1_shortname,c.teamname2_shortname,c.team1imageid,c.team2imageid FROM contestdetails c JOIN  ContestJoined cj ON cj.Contestid = c.Contestid\r\n"
							+ " and cj.userid ='" + userId
							+ "'and TIMESTAMPDIFF(MINUTE, contest_dateand_time, convert_tz(now(),@@session.time_zone,'+05:30')) <= 2");
			List<Contestdetails> contest = query.setResultTransformer(Transformers.aliasToBean(Contestdetails.class))
					.list();
			/*
			 * for (Contestdetails contestdetails : contest) {
			 * 
			 * LocalDate date = LocalDate.parse(contestdetails.getContestdate(), format);
			 * 
			 * LocalTime lt = LocalTime.parse(contestdetails.getContesttime());
			 * LocalDateTime ldt = LocalDateTime.of(date, lt); // this if co stops if cotest
			 * expires if (LocalDateTime.now().isBefore(((ldt)))) { //
			 * modelQuestions.setStatusFlag(true); list.add(contestdetails);
			 * 
			 * } }
			 */
			Set<Contestdetails> set = contest.stream().collect(Collectors.toCollection(HashSet::new));

			return set;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public String userjoinContest(String userId, String contestId, int lableid) {
		Session session = entityManager.unwrap(Session.class);

		try {
			Query query = session.createSQLQuery("INSERT INTO ContestJoined (userid, Contestid,lableid,joinedstatus)"
					+ "VALUES ('" + userId + "','" + contestId + "','" + lableid + "'," + 1 + ")");
			query.executeUpdate();
			return Constants.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Lables> getAlllables() {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(Lables.class);
			List<Lables> list = criteria.list();

			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public List<Lables> getAlllablesbycontestId(String lableIdList) {
 		Session session = entityManager.unwrap(Session.class);
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
		try {
			String[] split = lableIdList.split(",");
			/*
			 * for (String string : split) { if (!string.contains("inactive") ||
			 * !string.contains("active")) list2.add(Integer.parseInt(string)); }
			 */
			for (String part : split) {

				// split the student data by colon to get the
				// name and roll number
				String stuData[] = part.split(":");

				Integer lableId = Integer.parseInt(stuData[0]);
				String activeOrInactive = stuData[1].trim();
				list2.add(lableId);
				// Add to map
				hashMap.put(lableId, activeOrInactive);
			}

			Criteria criteria = session.createCriteria(Lables.class);
			criteria.add(Restrictions.in("lableId", list2));
			List<Lables> list = criteria.list();

			session.close();
			for (Lables lables : list) {
				for (Integer lables2 : hashMap.keySet()) {

					if (lables.getLableId() == lables2) {
						lables.setLableStatus(hashMap.get(lables2));

					}
				}
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Contestdetails> getContestsBySubCatagoryId(String subCatagoryId, String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			/*
			 * DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
			 * ArrayList<Contestdetails> list = new ArrayList<Contestdetails>();
			 */
			String sql = "SELECT  co.show_name as showname,co.contestid, co.subcategoryname,co.contestdate,co.contesttime,co.language,co.team1imageid,"
					+ "co.team2imageid,co.teamname1_shortname ,co.teamname2_shortname,co.Contestname2,co.Contestname1,co.lableidList, co.contest_dateand_time as contestDateandTime,bannerimage as bannerImage,bannervalue as bannerValue   FROM"
					+ " contestdetails co   where " + " co.subcategoryid= '" + subCatagoryId
					+ "' and TIMESTAMPDIFF(MINUTE, contest_dateand_time, convert_tz(now(),@@session.time_zone,'+05:30')) <= 2";
			if (userId.equals("1233377999"))
				sql = sql + " or co.contestid='9a58dfd0-6e67-429a-baa9-2449e735c9f3'";

			SQLQuery query = session.createSQLQuery(sql);
			List<Contestdetails> contestList = query
					.setResultTransformer(Transformers.aliasToBean(Contestdetails.class)).list();
			session.close();
			for (int i = 0; i < contestList.size(); i++) {
				String lableidList = contestList.get(i).getLableidList();
				String[] split = lableidList.split(",");
				BigInteger poolAmount = getMaxPoolAmount(split);
				contestList.get(i).setPoolamount(poolAmount);
			}

			/*
			 * Date date1 = new Date(); Timestamp ts = new Timestamp(date1.getTime()); for
			 * (Contestdetails contestdetails : contestList) {
			 * 
			 * if (contestdetails.getContestDateandTime().after(ts)) {
			 * list.add(contestdetails); }
			 * 
			 * LocalDate date = LocalDate.parse(contestdetails.getContestdate(), format);
			 * 
			 * LocalTime lt = LocalTime.parse(contestdetails.getContesttime());
			 * LocalDateTime ldt = LocalDateTime.of(date, lt); if
			 * (LocalDateTime.now().isAfter(((ldt)))) { //
			 * modelQuestions.setStatusFlag(true); list.add(contestdetails);
			 * 
			 * }
			 * 
			 * }
			 */

			return contestList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	private BigInteger getMaxPoolAmount(String[] split) {
		Session session = entityManager.unwrap(Session.class);

		try {

			String query = "select max(poolamount) as poolamount from lables where lableid in (";
			for (String string : split) {
				String[] split2 = string.split(":");

				int lId = Integer.parseInt(split2[0]);
				query = query + lId + ",";

			}

			query = query.substring(0, query.length() - 1) + ")";
			SQLQuery sql = session.createSQLQuery(query);
			List<Lables> list2 = sql.setResultTransformer(Transformers.aliasToBean(Lables.class)).list();

			return list2.get(0).getPoolamount();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public String updatelables(String contestid, ContestVivo vivo) {
		Session session = entityManager.unwrap(Session.class);
		try {
			for (int i = 0; i < vivo.getLableList().size(); i++) {

				session.saveOrUpdate(vivo.getLableList().get(i));
			}
			return Constants.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		} finally {
			session.close();
		}

	}

	public Map<String, Object> winnings(int lableId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<ShuffleMoney> shuffleList = getShuffleList(lableId);
		if (!Sharp6Validation.isEmpty(shuffleList)) {
			map.put(Constants.STATUS, true);

			map.put("winnings", shuffleList);
		} else {
			map.put(Constants.STATUS, false);

			map.put("winnings", "invalid ShuffleId");
		}
		return map;
	}

	@SuppressWarnings({ "deprecation", "unused", "unlikely-arg-type" })
	@Override
	public Map<String, Object> shuffleRankings(String contestId, String userId, int lableId) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		Session session = entityManager.unwrap(Session.class);
		// List<Questions> listOfQuestions =
		// userservice.getquestionsHistoryByContestId(contestId, userId);
		try {
			if (userId.equals("1233377999")) {
				ArrayList<LeadBoards> list = new ArrayList<LeadBoards>();

				Random random = new Random();

				int x = random.nextInt(2);
				int y;
				if (x == 0) {
					x = 2;
					y = 1;
				} else
					y = 2;
				List<LeadBoardContest> contestdetailsById = getContestBylableId(contestId, lableId);
				List<ShuffleMoney> shuffleListbyLableId = getShuffleList(lableId);
				ArrayList<LeadBoards> rankList = new ArrayList<LeadBoards>();

				rankList.add(new LeadBoards(0, x, 0.0, userId, "Sri Krishna", lableId, contestId, null, 0));
				rankList.add(new LeadBoards(0, y, 0.0, "6677889900", "factboat", lableId, contestId, null, 0));
				rankList.stream().sorted(Comparator.comparing(LeadBoards::getRank));

				map.put(Constants.STATUS, true);
				map.put("myRankBoard", new LeadBoards(0, x, 0.0, userId, "Sri Krishna", lableId, contestId, null, 0));
				map.put("contestList", contestdetailsById);
				map.put("winnings", shuffleListbyLableId);
				map.put("LeadBoards", rankList);
				return map;
			}
			List<LeadBoardContest> contestById = getContestBylableId(contestId, lableId);
			Query query = session.createQuery(
					"from UserAnswerHistory where contestId = :contestid order by correctanswerscount desc,totalTime asc");
			query.setParameter("contestid", contestId);
			List<UserAnswerHistory> list = query.list();
			List<ShuffleMoney> shuffleList = getShuffleList(lableId);

			int rankend = shuffleList.get(shuffleList.size() - 1).getRankend();

			session.close();
			Comparator<UserAnswerHistory> compareByCOrrect = (UserAnswerHistory o1,
					UserAnswerHistory o2) -> o2.getCorrectanswerscount() - o1.getCorrectanswerscount();
			Collections.sort(list, compareByCOrrect);
			CopyOnWriteArrayList<LeadBoards> leadList = new CopyOnWriteArrayList<LeadBoards>();

			List<String> joinedUsersList = getJoinedUsersAndNotPlayedpayers(contestId, lableId);

			CopyOnWriteArrayList<String> userIds = new CopyOnWriteArrayList();
			list.stream().forEach(s -> userIds.add(s.getUserId()));

			userIds.addAll(joinedUsersList);

			List<String> users = userIds.stream().distinct().collect(Collectors.toList());
			List<UserMaster> nameByList = userservice.getuserNameByList(users);
			int count = 0;
			CopyOnWriteArrayList<LeadBoards> arrayList = new CopyOnWriteArrayList<>();
			for (UserMaster userMaster : nameByList) {
				LeadBoards leadBoards = new LeadBoards();
				leadBoards.setUserId(userMaster.getUserid());
				leadBoards.setUserName(userMaster.getUserName());
				arrayList.add(leadBoards);
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < arrayList.size(); j++) {
					if (list.get(i).getUserId().equals(arrayList.get(j).getUserId())) {
						arrayList.get(j).setContestId(list.get(i).getContestId());
						arrayList.get(j).setTotalTime(list.get(i).getTotalTime());
						arrayList.get(j).setLableId(lableId);
						arrayList.get(j).setTotalCorrectAnswers(list.get(i).getCorrectanswerscount());

					}
				}
			}
			Comparator<LeadBoards> leadUsers = (LeadBoards o1, LeadBoards o2) -> o2.getTotalCorrectAnswers()
					- o1.getTotalCorrectAnswers();
			Collections.sort(arrayList, leadUsers);
			int rank = 0;
			for (int i = 0; i < list.size(); i++) {

				for (int j = 0; j < arrayList.size(); j++) {

					if (list.get(i).getUserId().equals(arrayList.get(j).getUserId())) {
						rank = rank + 1;
						if (i != 0 && list.get(i - 1).getCorrectanswerscount() == list.get(i).getCorrectanswerscount()
								&& list.get(i - 1).getTotalTime().equals(list.get(i).getTotalTime()))
							arrayList.get(j).setRank(arrayList.get(j - 1).getRank());
						else
							arrayList.get(j).setRank(rank);
					}
				}
			}

//shuffling money
			ArrayList<RankVivo> list2 = new ArrayList<RankVivo>();

			for (ShuffleMoney s1 : shuffleList) {

				if (s1.getRankend() != 0) {

					for (int i = s1.getRankstart(); i <= s1.getRankend(); i++) {
						double l = Double.valueOf(s1.getSharedamount());
						if (s1.getRankend() != 1) {
							int x = s1.getRankend() - s1.getRankstart();
							l = l / (x + 1);
						}
						list2.add(new RankVivo(i, l, s1.getPercent()));
					}
				} else
					list2.add(new RankVivo(s1.getRankstart(), Double.valueOf(s1.getSharedamount()), s1.getPercent()));

			}
			HashMap<Integer, String> leadMpa = new HashMap<Integer, String>();
			for (RankVivo s : list2) {

				int c = 0;
				double d = 0;
				for (LeadBoards l : arrayList) {
					if (l.getRank() == s.getRank()) {
						c = c + 1;
						d = d + s.getSharedAmount();
						leadMpa.put(l.getRank(), String.valueOf(d) + "," + String.valueOf(c));
					}
				}

			}
			ArrayList<DistributeMoney> list3 = new ArrayList<DistributeMoney>();
			for (Entry<Integer, String> entry : leadMpa.entrySet()) {
				String value = entry.getValue();

				DistributeMoney money = new DistributeMoney();
				money.setRank(entry.getKey());
				if (value.contains(",")) {
					String[] split = value.split(",");

					double damount = Double.parseDouble(split[0]) / Long.parseLong(split[1]);
					money.setMoney(damount);

				} else {
					money.setMoney(Double.parseDouble(value));
				}
				list3.add(money);
			}

			for (DistributeMoney distributeMoney : list3) {
				for (LeadBoards lead : arrayList) {
					if (lead.getRank() == distributeMoney.getRank()) {

						lead.setMyMoney(distributeMoney.getMoney());

					} else if (lead.getRank() == 0) {
						// lead.setRank((leadList.get(leadList.size() - 1).getRank()) + 1);
						lead.setRank(rank + 1);
						lead.setContestId(contestId);
						lead.setLableId(lableId);
						lead.setMyMoney(0);

					}
					if (lead.getUserId().equals(userId)) {
						map.put("myRankBoard", lead);
					}
				}
			}
			Comparator<LeadBoards> lead = (LeadBoards o1, LeadBoards o2) -> o1.getRank() - o2.getRank();
			Collections.sort(arrayList, lead);

			boolean moneyAddedToWallet = payment.isMoneyAddedToWallet(contestId);
			// tds -30% if mymoney >10000
			List<LeadBoards> finalLeadList = arrayList.stream().map(l -> {
				if (l.getMyMoney() > 10000.00)
					l.setMyMoney(l.getMyMoney() - (l.getMyMoney() * 30 / 100));
				return l;

			}).collect(Collectors.toList());
			if (moneyAddedToWallet == true) {
				for (LeadBoards distributeMoney : finalLeadList) {

					Payments payments = new Payments();
					payments.setUserid(distributeMoney.getUserId());
					payments.setAmount(distributeMoney.getMyMoney());
					payments.setContestId(contestId);
					payments.setStatus(Constants.SUCCESS);
					payments.setReceipt("order_rcptid_sharp6_win_money_in_" + contestById.get(0).getContestname1()
							+ "on_" + new Date().getTime());
					payments.setCurrency("INR");
					payments.setTranscationtype("WINNINGS");
					payments.setCreated_at(String.valueOf(new Date().getTime()));
					Payments savePayment = payment.savePayment(payments);
					Sharp6Wallet sharp6Wallet = new Sharp6Wallet();
					Sharp6Wallet reciever = payment.getMainKales(distributeMoney.getUserId());

					reciever.setMainkales(reciever.getMainkales() + distributeMoney.getMyMoney());
					Sharp6Wallet saveMoneytoWalet = payment.saveMoneytoWalet(reciever);

				}
			}

			// String saveRankings = saveRankings(finalLeadList);

			map.put(Constants.STATUS, true);
			map.put("contestList", contestById);
			map.put("winnings", shuffleList);
			map.put("LeadBoards", finalLeadList);
			// map.put("l", leadList);
		} catch (

		Exception e) {
			// TODO: handle exception
			map.put(Constants.STATUS, false);
			map.put("errorVaue", e.getMessage());
		}

		return map;
	}

	@SuppressWarnings("deprecation")
	private List<String> getJoinedUsersAndNotPlayedpayers(String contestId, int lableId) {
		Session session = entityManager.unwrap(Session.class);

		SQLQuery query = session.createSQLQuery(
				"select * from ContestJoined where (Contestid= :Contestid and lableid = :lableid) and joinedstatus =1");

		query.setParameter("Contestid", contestId);
		query.setParameter("lableid", lableId);
		List<ContestJoined> list = query.setResultTransformer(Transformers.aliasToBean(ContestJoined.class)).list();
		session.close();
		ArrayList<String> list2 = new ArrayList<String>();
		for (ContestJoined contestJoined : list) {
			list2.add(contestJoined.getUserid());
		}
		Criteria criteria = session.createCriteria(UserAnswerHistory.class);
		List<UserAnswerHistory> list3 = criteria.add(Restrictions.conjunction()
				.add(Restrictions.eq("contestId", contestId)).add(Restrictions.in("userId", list2))).list();
		ArrayList<String> list4 = new ArrayList<String>();
		for (UserAnswerHistory userAnswerHistory : list3) {
			for (String l : list2) {

				if (!userAnswerHistory.getUserId().equals(l)) {
					list4.add(l);
				}
			}
		}
		return list4;
	}

	@SuppressWarnings("deprecation")
	private List<ContestJoined> getJoinedUsersList(String contestId, int lableId) {
		Session session = entityManager.unwrap(Session.class);

		SQLQuery query = session.createSQLQuery(
				"select * from ContestJoined where (Contestid= :Contestid and lableid = :lableid) and joinedstatus =1");

		query.setParameter("Contestid", contestId);
		query.setParameter("lableid", lableId);
		List<ContestJoined> list = query.setResultTransformer(Transformers.aliasToBean(ContestJoined.class)).list();
		session.close();
		ArrayList<String> list2 = new ArrayList<String>();
		for (ContestJoined contestJoined : list) {
			list2.add(contestJoined.getUserid());
		}
		Criteria criteria = session.createCriteria(UserAnswerHistory.class);
		List<UserAnswerHistory> list3 = criteria.add(Restrictions.conjunction()
				.add(Restrictions.eq("contestId", contestId)).add(Restrictions.in("userId", list2))).list();
		ArrayList<String> list4 = new ArrayList<String>();
		for (UserAnswerHistory userAnswerHistory : list3) {
			for (String l : list2) {

				if (userAnswerHistory.getUserId().equals(l)) {
					list4.add(l);
				}
			}
		}
		return list;
	}

	private String saveRankings(ArrayList<LeadBoards> leadList) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		String status = null;
		try {
			for (LeadBoards leadBoards : leadList) {

				session.saveOrUpdate(leadBoards);
				session.flush();
				session.clear();
			}
			status = Constants.SUCCESS;
		} catch (Exception e) {
			status = Constants.FAILURE;
		}
		return status;
	}

	@SuppressWarnings("deprecation")
	private List<LeadBoardContest> getContestBylableId(String contestId, int lableId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Query query = session
					.createSQLQuery("SELECT e.contestid,e.show_name as showname,e.team1imageid,e.team2imageid,"
							+ "e.Contestname1,e.Contestname2,e.channelname,t.gameamount,t.totalamount,"
							+ "t.totalplayers,t.poolamount,count(co.lableid) as joinedplayers,co.joinedstatus FROM contestdetails e, "
							+ "lables t,ContestJoined co WHERE 1=1 and (t.lableid = :lableid and e.contestid=:contestId)");

			query.setParameter("lableid", lableId);
			query.setParameter("contestId", contestId);

			List<LeadBoardContest> list = query.setResultTransformer(Transformers.aliasToBean(LeadBoardContest.class))
					.list();
			session.close();
			BigInteger getjoinedplayersCount = getjoinedplayersCount(contestId, lableId);
			list.get(0).setJoinedplayers(getjoinedplayersCount);

			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private BigInteger getjoinedplayersCount(String contestId, int lableId) {
		Session session = entityManager.unwrap(Session.class);

		SQLQuery query = session.createSQLQuery("select count(co.lableid) as joinedplayers from ContestJoined"
				+ " co where co.Contestid='" + contestId + "' and co.lableid=" + lableId + " and co.joinedstatus=1");
		List<LeadBoardContest> list1 = query.setResultTransformer(Transformers.aliasToBean(LeadBoardContest.class))
				.list();

		session.close();
		return list1.get(0).getJoinedplayers();

	}

	/**/

	@Override
	@SuppressWarnings("deprecation")
	public List<ShuffleMoney> getShuffleList(int lableId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(ShuffleMoney.class);
			criteria.add(Restrictions.eq("lableid", lableId));
			List<ShuffleMoney> list = criteria.list();

			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public String userwithdrawLable(String userId, String contestId, int lableId) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		try {
			Query query = session.createSQLQuery("UPDATE ContestJoined " + "SET joinedstatus=2" + " WHERE (userid='"
					+ userId + "' and Contestid='" + contestId + "') and lableid=" + lableId);
			query.executeUpdate();
			return Constants.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		} finally {
			session.close();
		}
	}

	@Override
	public ArrayList<Object> getuserJoinedLables(String contestId, String userId) {
		Session session = entityManager.unwrap(Session.class);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {
			SQLQuery query = session.createSQLQuery("select lableid as lableId  from ContestJoined"
					+ " co where co.contestid='" + contestId + "' and co.userid= '" + userId + "'");
			List<Lables> list1 = query.setResultTransformer(Transformers.aliasToBean(Lables.class)).list();

			session.close();
			ArrayList<Object> arrayList = new ArrayList<Object>();
			for (Lables lead : list1) {
				String labeName = getLabeName(lead.getLableId());

				if (!Sharp6Validation.isEmpty(labeName)) {
					lead.setLablename(labeName);
					Map<String, Object> mylableRanks = shuffleRankings(contestId, userId, lead.getLableId(), labeName);
					for (Entry<String, Object> myboards : mylableRanks.entrySet()) {
						if (myboards.getKey().equals("myRankBoard")) {
							arrayList.add(myboards.getValue());

							hashMap.put(String.valueOf(lead.getLableId()), myboards.getValue());
						}
					}
				}

			}
			return arrayList;
		} catch (Exception e) {
			// TODO: handle exception
			hashMap.put("0", false);
			return null;
		}

	}

	public HashMap<String, Object> getuserLables(String contestId, String userId) {
		Session session = entityManager.unwrap(Session.class);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {
			SQLQuery query = session.createSQLQuery("select lableid as lableId  from ContestJoined"
					+ " co where co.contestid='" + contestId + "' and co.userid= '" + userId + "'");
			List<Lables> list1 = query.setResultTransformer(Transformers.aliasToBean(Lables.class)).list();
			double yourEarnings = 0;
			session.close();
			ArrayList<LeadBoards> arrayList = new ArrayList<LeadBoards>();
			for (Lables lead : list1) {
				String labeName = getLabeName(lead.getLableId());

				if (!Sharp6Validation.isEmpty(labeName)) {
					lead.setLablename(labeName);
					Map<String, LeadBoards> mylableRanks = shuffleRankingswithtotalREarnings(contestId, userId,
							lead.getLableId(), labeName);
					for (Entry<String, LeadBoards> myboards : mylableRanks.entrySet()) {
						if (myboards.getKey().equals("LeadBoards")) {
							arrayList.add(myboards.getValue());
							yourEarnings = yourEarnings + myboards.getValue().getMyMoney();
						}
					}
				}

			}
			hashMap.put(Constants.STATUS, true);
			hashMap.put("LableList", arrayList);
			hashMap.put("yourEarnings", yourEarnings);
			return hashMap;
		} catch (Exception e) {
			// TODO: handle exception
			hashMap.put(Constants.STATUS, false);
			hashMap.put("LableList", null);
			hashMap.put("yourEarnings", null);
			return hashMap;
		}

	}

	private Map<String, Object> shuffleRankings(String contestId, String userId, int lableId, String lableName) {

		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		Session session = entityManager.unwrap(Session.class);
		// List<Questions> listOfQuestions =
		// userservice.getquestionsHistoryByContestId(contestId, userId);
		try {

			List<LeadBoardContest> contestById = getContestBylableId(contestId, lableId);
			Query query = session
					.createQuery("from UserAnswerHistory where contestId = :contestid order by totalTime asc");
			query.setParameter("contestid", contestId);
			List<UserAnswerHistory> list = query.list();
			List<ShuffleMoney> shuffleList = getShuffleList(lableId);

			int rankend = shuffleList.get(shuffleList.size() - 1).getRankend();

			session.close();
			Comparator<UserAnswerHistory> compareByCOrrect = (UserAnswerHistory o1,
					UserAnswerHistory o2) -> o2.getCorrectanswerscount() - o1.getCorrectanswerscount();
			Collections.sort(list, compareByCOrrect);
			CopyOnWriteArrayList<LeadBoards> leadList = new CopyOnWriteArrayList<LeadBoards>();

			List<String> joinedUsersList = getJoinedUsersAndNotPlayedpayers(contestId, lableId);

			CopyOnWriteArrayList<String> userIds = new CopyOnWriteArrayList();
			list.stream().forEach(s -> userIds.add(s.getUserId()));

			userIds.addAll(joinedUsersList);

			List<String> users = userIds.stream().distinct().collect(Collectors.toList());
			List<UserMaster> nameByList = userservice.getuserNameByList(users);
			int count = 0;
			CopyOnWriteArrayList<LeadBoards> arrayList = new CopyOnWriteArrayList<>();
			for (UserMaster userMaster : nameByList) {
				LeadBoards leadBoards = new LeadBoards();
				leadBoards.setUserId(userMaster.getUserid());
				leadBoards.setUserName(userMaster.getUserName());
				arrayList.add(leadBoards);
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < arrayList.size(); j++) {
					if (list.get(i).getUserId().equals(arrayList.get(j).getUserId())) {
						arrayList.get(j).setContestId(list.get(i).getContestId());
						arrayList.get(j).setTotalTime(list.get(i).getTotalTime());
						arrayList.get(j).setLableId(lableId);
						arrayList.get(j).setTotalCorrectAnswers(list.get(i).getCorrectanswerscount());
						arrayList.get(j).setLableName(lableName);
						arrayList.get(j).setGameAmount(contestById.get(0).getGameamount());
						arrayList.get(j).setTotalPlayers(contestById.get(0).getTotalplayers());
						arrayList.get(j).setJoinedplayers(contestById.get(0).getJoinedplayers());
						arrayList.get(j).setPoolamount(contestById.get(0).getPoolamount());
						arrayList.get(j).setTotalamount(contestById.get(0).getTotalamount());
						arrayList.get(j).setPlayersLeft(contestById.get(0).getTotalplayers()
								- contestById.get(0).getJoinedplayers().intValueExact());

					}
				}
			}
			Comparator<LeadBoards> leadUsers = (LeadBoards o1, LeadBoards o2) -> o2.getTotalCorrectAnswers()
					- o1.getTotalCorrectAnswers();
			Collections.sort(arrayList, leadUsers);
			int rank = 0;
			for (int i = 0; i < list.size(); i++) {

				for (int j = 0; j < arrayList.size(); j++) {

					if (list.get(i).getUserId().equals(arrayList.get(j).getUserId())) {
						rank = rank + 1;
						if (i != 0 && list.get(i - 1).getCorrectanswerscount() == list.get(i).getCorrectanswerscount()
								&& list.get(i - 1).getTotalTime().equals(list.get(i).getTotalTime()))
							arrayList.get(j).setRank(arrayList.get(j - 1).getRank());

						else
							arrayList.get(j).setRank(rank);
					}
				}
			}

			// shuffling money
			ArrayList<RankVivo> list2 = new ArrayList<RankVivo>();

			for (ShuffleMoney s1 : shuffleList) {

				if (s1.getRankend() != 0) {

					for (int i = s1.getRankstart(); i <= s1.getRankend(); i++) {
						double l = Double.valueOf(s1.getSharedamount());
						if (s1.getRankend() != 1) {
							int x = s1.getRankend() - s1.getRankstart();
							l = l / (x + 1);
						}
						list2.add(new RankVivo(i, l, s1.getPercent()));
					}
				} else
					list2.add(new RankVivo(s1.getRankstart(), Double.valueOf(s1.getSharedamount()), s1.getPercent()));

			}
			HashMap<Integer, String> leadMpa = new HashMap<Integer, String>();
			for (RankVivo s : list2) {

				int c = 0;
				double d = 0;
				for (LeadBoards l : arrayList) {
					if (l.getRank() == s.getRank()) {
						c = c + 1;
						d = d + s.getSharedAmount();
						leadMpa.put(l.getRank(), String.valueOf(d) + "," + String.valueOf(c));
					}
				}

			}
			ArrayList<DistributeMoney> list3 = new ArrayList<DistributeMoney>();
			for (Entry<Integer, String> entry : leadMpa.entrySet()) {
				String value = entry.getValue();

				DistributeMoney money = new DistributeMoney();
				money.setRank(entry.getKey());
				if (value.contains(",")) {
					String[] split = value.split(",");

					double damount = Double.parseDouble(split[0]) / Long.parseLong(split[1]);
					money.setMoney(damount);

				} else {
					money.setMoney(Double.parseDouble(value));
				}
				list3.add(money);
			}

			for (DistributeMoney distributeMoney : list3) {
				for (LeadBoards lead : arrayList) {
					if (lead.getRank() == distributeMoney.getRank()) {
						lead.setMyMoney(distributeMoney.getMoney());

					} else if (lead.getRank() == 0) {
						// lead.setRank((leadList.get((leadList.size() - 1)).getRank()) + 1);
						lead.setRank(rank + 1);
						lead.setContestId(contestId);
						lead.setLableId(lableId);
						lead.setMyMoney(0);

					}
					if (lead.getUserId().equals(userId)) {
						map.put("myRankBoard", lead);

					}
				}
			}
			// tds -30% if mymoney >10000
			List<LeadBoards> finalLeadList = arrayList.stream().map(l -> {
				if (l.getMyMoney() > 10000.00)
					l.setMyMoney(l.getMyMoney() - (l.getMyMoney() * 30 / 100));
				return l;

			}).collect(Collectors.toList());
			Comparator<LeadBoards> lead = (LeadBoards o1, LeadBoards o2) -> o1.getRank() - o2.getRank();
			Collections.sort(finalLeadList, lead);
			// String saveRankings = saveRankings(leadList);

			map.put(Constants.STATUS, true);
			map.put("contestList", contestById);
			map.put("winnings", shuffleList);
			map.put("LeadBoards", finalLeadList);
			// map.put("l", leadList);
		} catch (

		Exception e) {
			// TODO: handle exception
			map.put(Constants.STATUS, false);
			map.put("errorVaue", e.getMessage());
		}

		return map;

	}

	@SuppressWarnings("deprecation")
	private String getLabeName(int lableId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Criteria criteria = session.createCriteria(Lables.class);
			criteria.add(Restrictions.eq("lableId", lableId));
			Lables uniqueResult = (Lables) criteria.uniqueResult();
			return uniqueResult.getLablename();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Contestdetails> gameHistoryByUserId(String userId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Query query = session
					.createSQLQuery("SELECT e.contestid,e.show_name as showname,e.contestdate,e.contesttime,"
							+ " e.Contestname1,e.Contestname2,e.channelname" + " FROM contestdetails e, "
							+ " ContestJoined co WHERE e.contestid= co.contestid and co.userid = :userid and co.joinedstatus=1  and TIMESTAMPDIFF(MINUTE, contest_dateand_time, convert_tz(now(),@@session.time_zone,'+05:30')) > 2 order by contest_dateand_time desc");

			query.setParameter("userid", userId);
			List<Contestdetails> list = query.setResultTransformer(Transformers.aliasToBean(Contestdetails.class))
					.list();

			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<Contestdetails> completedContest(String mobileNumber) {
		Session session = entityManager.unwrap(Session.class);
		try {
			Query query = session.createSQLQuery("SELECT e.contestid,e.show_name as showname,"
					+ " e.Contestname1,e.Contestname2,e.channelname,e.categoryname" + " FROM contestdetails e, "
					+ " ContestJoined co WHERE e.contestid= co.contestid and co.userid = :userid and co.joinedstatus=1");

			query.setParameter("userid", mobileNumber);
			List<Contestdetails> list = query.setResultTransformer(Transformers.aliasToBean(Contestdetails.class))
					.list();

			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Contestdetails> inProgessContestList() {
		Session session = entityManager.unwrap(Session.class);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		try {

			Criteria criteria = session.createCriteria(Contestdetails.class);

			List<Contestdetails> list = criteria.list();
			ArrayList<Contestdetails> list2 = new ArrayList<Contestdetails>();
			for (Contestdetails contest : list) {

				LocalDate date = LocalDate.parse(contest.getContestdate(), format);

				LocalTime lt = LocalTime.parse(contest.getContesttime());
				LocalDateTime ldt = LocalDateTime.of(date, lt);
				if (LocalDateTime.now().isBefore(((ldt)))) {
					// modelQuestions.setStatusFlag(true);
					list2.add(contest);

				}

			}
			return list2;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

//getlable List with total earnings of contest
	private Map<String, LeadBoards> shuffleRankingswithtotalREarnings(String contestId, String userId, int lableId,
			String lableName) {

		Map<String, LeadBoards> map = new HashMap<String, LeadBoards>();
		Session session = entityManager.unwrap(Session.class);
		// List<Questions> listOfQuestions =
		// userservice.getquestionsHistoryByContestId(contestId, userId);
		try {

			List<LeadBoardContest> contestById = getContestBylableId(contestId, lableId);
			Query query = session
					.createQuery("from UserAnswerHistory where contestId = :contestid order by totalTime asc");
			query.setParameter("contestid", contestId);

			List<UserAnswerHistory> list = query.list();
			List<ShuffleMoney> shuffleList = getShuffleList(lableId);

			int rankend = shuffleList.get(shuffleList.size() - 1).getRankend();

			session.close();
			Comparator<UserAnswerHistory> compareByCOrrect = (UserAnswerHistory o1,
					UserAnswerHistory o2) -> o2.getCorrectanswerscount() - o1.getCorrectanswerscount();
			Collections.sort(list, compareByCOrrect);
			CopyOnWriteArrayList<LeadBoards> leadList = new CopyOnWriteArrayList<LeadBoards>();

			List<String> joinedUsersList = getJoinedUsersAndNotPlayedpayers(contestId, lableId);

			CopyOnWriteArrayList<String> userIds = new CopyOnWriteArrayList();
			list.stream().forEach(s -> userIds.add(s.getUserId()));

			userIds.addAll(joinedUsersList);

			List<String> users = userIds.stream().distinct().collect(Collectors.toList());

			List<UserMaster> nameByList = userservice.getuserNameByList(users);
			int count = 0;
			CopyOnWriteArrayList<LeadBoards> arrayList = new CopyOnWriteArrayList<>();
			for (UserMaster userMaster : nameByList) {
				LeadBoards leadBoards = new LeadBoards();
				leadBoards.setUserId(userMaster.getUserid());
				leadBoards.setUserName(userMaster.getUserName());
				leadBoards.setLableName(lableName);
				leadBoards.setGameAmount(contestById.get(0).getGameamount());
				leadBoards.setTotalPlayers(contestById.get(0).getTotalplayers());
				leadBoards.setJoinedplayers(contestById.get(0).getJoinedplayers());
				leadBoards.setPoolamount(contestById.get(0).getPoolamount());
				leadBoards.setTotalamount(contestById.get(0).getTotalamount());
				leadBoards.setPlayersLeft(
						contestById.get(0).getTotalplayers() - contestById.get(0).getJoinedplayers().intValueExact());
				arrayList.add(leadBoards);
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < arrayList.size(); j++) {
					if (list.get(i).getUserId().equals(arrayList.get(j).getUserId())) {
						arrayList.get(j).setContestId(list.get(i).getContestId());
						arrayList.get(j).setTotalTime(list.get(i).getTotalTime());
						arrayList.get(j).setLableId(lableId);
						arrayList.get(j).setTotalCorrectAnswers(list.get(i).getCorrectanswerscount());

					}
				}
			}
			Comparator<LeadBoards> leadUsers = (LeadBoards o1, LeadBoards o2) -> o2.getTotalCorrectAnswers()
					- o1.getTotalCorrectAnswers();
			Collections.sort(arrayList, leadUsers);
			int rank = 0;
			for (int i = 0; i < list.size(); i++) {

				for (int j = 0; j < arrayList.size(); j++) {

					if (list.get(i).getUserId().equals(arrayList.get(j).getUserId())) {
						rank = rank + 1;
						if (i != 0 && list.get(i - 1).getCorrectanswerscount() == list.get(i).getCorrectanswerscount()
								&& list.get(i - 1).getTotalTime().equals(list.get(i).getTotalTime()))
							arrayList.get(j).setRank(arrayList.get(j - 1).getRank());

						else
							arrayList.get(j).setRank(rank);
					}
				}
			}

			// shuffling money
			ArrayList<RankVivo> list2 = new ArrayList<RankVivo>();

			for (ShuffleMoney s1 : shuffleList) {

				if (s1.getRankend() != 0) {
					for (int i = s1.getRankstart(); i <= s1.getRankend(); i++) {
						double l = Double.valueOf(s1.getSharedamount());
						if (s1.getRankend() != 1) {
							int x = s1.getRankend() - s1.getRankstart();
							l = l / (x + 1);
						}
						list2.add(new RankVivo(i, l, s1.getPercent()));
					}
				} else
					list2.add(new RankVivo(s1.getRankstart(), Double.valueOf(s1.getSharedamount()), s1.getPercent()));

			}
			HashMap<Integer, String> leadMpa = new HashMap<Integer, String>();
			for (RankVivo s : list2) {

				int c = 0;
				double d = 0;
				for (LeadBoards l : arrayList) {
					if (l.getRank() == s.getRank()) {
						c = c + 1;
						d = d + s.getSharedAmount();
						leadMpa.put(l.getRank(), String.valueOf(d) + "," + String.valueOf(c));
					}
				}

			}
			ArrayList<DistributeMoney> list3 = new ArrayList<DistributeMoney>();
			for (Entry<Integer, String> entry : leadMpa.entrySet()) {
				String value = entry.getValue();

				DistributeMoney money = new DistributeMoney();
				money.setRank(entry.getKey());
				if (value.contains(",")) {
					String[] split = value.split(",");

					double damount = Double.parseDouble(split[0]) / Long.parseLong(split[1]);
					money.setMoney(damount);

				} else {
					money.setMoney(Double.parseDouble(value));
				}
				list3.add(money);
			}

			for (DistributeMoney distributeMoney : list3) {
				for (LeadBoards lead : arrayList) {
					if (lead.getRank() == distributeMoney.getRank()) {
						lead.setMyMoney(distributeMoney.getMoney());

					} else if (lead.getRank() == 0) {
						// lead.setRank((leadList.get((leadList.size() - 1)).getRank()) + 1);
						lead.setRank(rank + 1);
						lead.setContestId(contestId);
						lead.setLableId(lableId);
						lead.setMyMoney(0);

					}
					if (lead.getUserId().equals(userId)) {
						/* list4.add(lead); */

						map.put("LeadBoards", lead);
					}
				}
			}
			// tds -30% if mymoney >10000
			List<LeadBoards> finalLeadList = arrayList.stream().map(l -> {
				if (l.getMyMoney() > 10000.00)
					l.setMyMoney(l.getMyMoney() - (l.getMyMoney() * 30 / 100));
				return l;

			}).collect(Collectors.toList());
			Comparator<LeadBoards> lead = (LeadBoards o1, LeadBoards o2) -> o1.getRank() - o2.getRank();
			Collections.sort(finalLeadList, lead);
			// String saveRankings = saveRankings(leadList);

			/*
			 * map.put(Constants.STATUS, true); map.put("contestList", contestById);
			 * map.put("winnings", shuffleList);
			 */
		} catch (

		Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			map.put("errorVaue", null);
		}

		return map;

	}

	@Override
	public String remooveCategorybyId(String categoryid) {
		Session session = entityManager.unwrap(Session.class);

		try {
			Category find = session.find(Category.class, Integer.parseInt(categoryid));
			session.remove(find);
			/*
			 * SQLQuery query =
			 * session.createSQLQuery("DELETE from category where categoryid =" +
			 * categoryid); query.executeUpdate();
			 */
			session.flush();
			return Constants.SUCCESS;

		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}
	}

	@Override
	public String remooveChannel(String channelId) {
		Session session = entityManager.unwrap(Session.class);

		try {

			SQLQuery query = session.createSQLQuery("DELETE from channel where channelid ='" + channelId + "'");
			query.executeUpdate();

			return Constants.SUCCESS;

		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}
	}

	@Override
	public String remoovecontestById(String contestid) {
		Session session = entityManager.unwrap(Session.class);

		try {

			SQLQuery query = session.createSQLQuery("DELETE from contestdetails where contestid ='" + contestid + "'");
			query.executeUpdate();

			return Constants.SUCCESS;

		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}
	}

	@Override
	public List<Contestdetails> inprogesscontestById(String contestid) {

		// TODO Auto-generated method stub

		Session session = entityManager.unwrap(Session.class);

		try {
			Query query = session.createSQLQuery(
					"SELECT contestid,categoryid,channelid,contestdate,contesttime,createddate,language,languageid,showid,teamname1_shortname,teamname2_shortname,Contestname1,Contestname2,subcategoryid,channelname,categoryname,subcategoryname,team1imageid,team2imageid,show_name as showname,lableidList,bannerimage as bannerImage,bannervalue as bannerValue FROM contestdetails where contestid = '"
							+ contestid + "'");

			@SuppressWarnings("deprecation")
			List<Contestdetails> contest = query.setResultTransformer(Transformers.aliasToBean(Contestdetails.class))
					.list();
			session.close();
			List<Lables> alllablesbycontestId = getAlllablesbycontestId(contest.get(0).getLableidList());
			List<Lables> checkUserJOined = checkUserJOined1(contestid, alllablesbycontestId);

			if (!Sharp6Validation.isEmpty(checkUserJOined)) {

				for (Lables integer : checkUserJOined) {

					for (Lables lable : alllablesbycontestId) {
						List<Integer> joinedPlayers = getjoinedPlayers(contest.get(0).getContestid(),
								lable.getLableId());
						lable.setJoinedplayers(joinedPlayers.size());
						lable.setPlayersLeft(lable.getTotalPlayers() - joinedPlayers.size());
						if (lable.getLableId() == integer.getLableId()) {

							lable.setJoinedStatus(integer.getJoinedStatus());
						}
						if (lable.getJoinedStatus() == 0 && lable.getJoinedplayers() == lable.getPlayersLeft()) {
							lable.setJoinedStatus(3);
						}

					}
				}
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");

			LocalTime lt = LocalTime.parse(contest.get(0).getContesttime());
			System.out.println(df.format(lt.plusMinutes(3)));

			if (LocalTime.now().isBefore(lt.plusMinutes(3))) {
				if (lt.isAfter(LocalTime.now()))
					contest.get(0).setInReview(true);

			}
			/*
			 * CopyOnWriteArrayList<Lables> list = new CopyOnWriteArrayList<Lables>();
			 * list.addAll(alllablesbycontestId); for (Lables lables : list) { if
			 * (lables.getJoinedStatus() != 1 && lables.getPlayersLeft() == 0) {
			 * alllablesbycontestId.remove(lables); } }
			 */

			// contest.get(0).setUserid(userId);
			contest.get(0).setLableList(alllablesbycontestId);

			return contest;
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private List<Lables> checkUserJOined1(String contestId, List<Lables> alllablesbycontestId) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		try {

			String query = "SELECT lableid as lableId, userid as userid FROM Contest_Joined where Contestid = '"
					+ contestId + "' and lableid in (";
			for (Lables integer : alllablesbycontestId) {
				query = query + "" + integer.getLableId() + ",";
			}
			// query = query + " and joinedstatus = 1";
			StringBuffer sb = new StringBuffer(query);
			// invoking the method
			sb.deleteCharAt(sb.length() - 1);
			query = sb.toString() + ")";
			SQLQuery sqlQuery = session.createSQLQuery(query);
			List<Lables> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(Lables.class)).list();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public String inactiveContestById(String contestid, int lableId) {
		Session session = entityManager.unwrap(Session.class);

		try {
			Query query = session.createSQLQuery(
					"SELECT contestid,categoryid,channelid,contestdate,contesttime,createddate,language,languageid,showid,teamname1_shortname,teamname2_shortname,Contestname1,Contestname2,subcategoryid,channelname,categoryname,subcategoryname,team1imageid,team2imageid,show_name as showname,lableidList FROM contestdetails where contestid = '"
							+ contestid + "'");

			@SuppressWarnings("deprecation")
			List<Contestdetails> contest = query.setResultTransformer(Transformers.aliasToBean(Contestdetails.class))
					.list();
			session.close();
			String lableidList = contest.get(0).getLableidList();
			String[] split = lableidList.split(",");
			String s = "";
			HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
			/*
			 * for (String string : split) { if (!string.contains("inactive") ||
			 * !string.contains("active")) list2.add(Integer.parseInt(string)); }
			 */
			for (String part : split) {

				// split the student data by colon to get the
				// name and roll number

				String stuData[] = part.split(":");
				Integer l = Integer.parseInt(stuData[0]);
				String activeOrInactive = stuData[1].trim();
				if (l == lableId) {
					activeOrInactive = "inactive";

				}
				// list2.add(lableId);
				// Add to map

				hashMap.put(l, activeOrInactive);
			}
			StringBuilder mapAsString = new StringBuilder();
			for (Integer key : hashMap.keySet()) {
				mapAsString.append(key + ":" + hashMap.get(key) + ",");
			}
			mapAsString.deleteCharAt(mapAsString.length() - 1);
			// mapAsString.length());

			String updateStatus = updateContestLableactivestatus(mapAsString, contestid);
			String sendMOneyToUserWallet = sendMOneyToUserWallet(lableId, contest);
			System.out.println(updateStatus);
			System.out.println(mapAsString.toString());
			// contest.get(0).setLableList(lableList);
			return sendMOneyToUserWallet;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private String sendMOneyToUserWallet(int lableId, List<Contestdetails> contest) {
		Session session = entityManager.unwrap(Session.class);
		String success = Constants.FAILURE;
		try {
			Criteria criteria = session.createCriteria(Lables.class);
			criteria.add(Restrictions.eq("lableId", lableId));
			Lables lable = (Lables) criteria.uniqueResult();
			session.close();
			ArrayList<Lables> list = new ArrayList<Lables>();
			list.add(lable);
			ArrayList<String> userList = new ArrayList<String>();
			List<Lables> checkUserJOined = checkUserJOined1(contest.get(0).getContestid(), list);
			for (Lables lables : checkUserJOined) {
				userList.add(lables.getUserid());
			}
			Criteria cri = session.createCriteria(Sharp6Wallet.class);
			cri.add(Restrictions.in("userid", userList));
			List<Sharp6Wallet> walletList = cri.list();
			session.close();
			for (Sharp6Wallet sharp6Wallet : walletList) {
				Long longobj = Long.parseLong(String.valueOf(lable.getGameAmount()));
				Payments payment = new Payments();

				payment.setAmount(longobj.doubleValue());
				payment.setUserid(sharp6Wallet.getUserid());
				payment.setStatus(Constants.SUCCESS);
				payment.setCurrency("INR");
				payment.setReceipt("order_rcptid_sharp6_" + new Date().getTime());
				payment.setCreated_at(String.valueOf(new Date().getTime()));
				payment.setId("order_return_gameinactive");
				payment.setUpdated_at(String.valueOf(new Date().getTime()));
				payment.setAmount(longobj.doubleValue());

				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				Sharp6Wallet mainKales = gatewayService.getMainKales(payment.getUserid());
				if (payment.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
					mainKales.setMainkales(mainKales.getMainkales() + payment.getAmount());
					Sharp6Wallet saveMoneytoWalet = gatewayService.saveMoneytoWalet(mainKales);
					if (saveMoneytoWalet != null)
						success = Constants.SUCCESS;
					else
						success = Constants.FAILURE;
					success = Constants.SUCCESS;
				} else {
					success = Constants.FAILURE;

				}
				Payments savePayment = gatewayService.updatePaymentStatus(payment);
			}

			return success;
		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}
	}

	private String updateContestLableactivestatus(StringBuilder mapAsString, String contestid) {
		Session session = entityManager.unwrap(Session.class);
		try {
			SQLQuery sql = session.createSQLQuery(
					"UPDATE contestdetails SET lableidlist = '" + mapAsString + "'where contestid='" + contestid + "'");
			sql.executeUpdate();
			return Constants.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}

	}

	@Override
	public Long joinedUserCount(String contestId, int lableId) {
		Session session = entityManager.unwrap(Session.class);
		try {

			/*
			 * Criteria criteria = session.createCriteria(ContestJoined.class);
			 * criteria.add(Restrictions.conjunction().add(Restrictions.eq("lableid",
			 * lableId)) .add(Restrictions.eq("Contestid",
			 * contestId)).add(Restrictions.eq("joinedstatus", 1))); List<ContestJoined>
			 * list = criteria.list();
			 */
			String sql = "SELECT count(*) as size FROM ContestJoined where Contestid='" + contestId + "' and lableid ="
					+ lableId + " and joinedstatus=1";

			SQLQuery query = session.createSQLQuery(sql);
			ContestJoined list = (ContestJoined) query
					.setResultTransformer(Transformers.aliasToBean(ContestJoined.class)).uniqueResult();

			/*
			 * List<ContestJoined> list = criteria
			 * .add(Restrictions.conjunction().add(Restrictions.eq("lableid", lableId))
			 * .add(Restrictions.eq("Contestid",
			 * contestId)).add(Restrictions.eq("joinedstatus", 1))) .list();
			 */

			/*
			 * org.hibernate.query.Query query = session .createQuery(
			 */
			/*
			 * String hql
			 * ="select * FROM ContestJoined where lableid="+lableId+" and Contestid="
			 * +contestId+" and joinedstatus=1"; Query query = session.createQuery(hql);
			 * 
			 * query.setParameter("lableid", lableId); query.setParameter("Contestid",
			 * contestId);
			 * 
			 * 
			 * List<ContestJoined> list = query.list();
			 */
			BigInteger size = list.getSize();
			return size.longValue();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public String isUserJoinedInLable(String userId, String contestId, int lableId) {
		Session session = entityManager.unwrap(Session.class);
		try {
			SQLQuery query = session.createSQLQuery("SELECT * FROM ContestJoined where (userid = '" + userId
					+ "' and Contestid = '" + contestId + "') and lableid = " + lableId);
			// SELECT * FROM myapp.ContestJoined where userid='9393333577'and
			// Contestid='0dbc9a51-bbe2-4d5d-a9e2-52dc95f3779d' and lableid=1928;

			List<ContestJoined> list = query.list();

			if (Sharp6Validation.isEmpty(list)) {
				return "true";
			} else {
				return "false";
			}
		} catch (Exception e) {
			return "false";
		}

	}

	public List<JSONObject> getAfterThreeMinutes() {
		Session session = entityManager.unwrap(Session.class);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		try {
			Criteria criteria = session.createCriteria(Contestdetails.class);
			List<Contestdetails> contestObj = criteria.list();
			System.out.println("List of contests :: " + contestObj);
			List<JSONObject> resultList = new ArrayList<>();
			for (int i = 0; i <= contestObj.size(); i++) {
				Contestdetails cObj = contestObj.get(i);
				System.out.println(
						"contest date :: " + new Timestamp(Long.valueOf(cObj.getCreateddate())).toLocalDateTime());
				long difference = ChronoUnit.MINUTES.between(
						new Timestamp(Long.valueOf(cObj.getCreateddate())).toLocalDateTime(), LocalDateTime.now());
				System.out.println("contest Id: " + cObj.getContestid() + " contest date : " + cObj.getCreateddate()
						+ " difference :: " + difference);
				if (difference <= 3) {
					JSONObject result = null;
					SQLQuery query = session
							.createSQLQuery("SELECT userid FROM ContestJoined where  Contestid = '"
									+ cObj.getContestid() + "' and joinedstatus = 1;");
					// SELECT * FROM ContestJoined where userid='9393333577'and
					// Contestid='0dbc9a51-bbe2-4d5d-a9e2-52dc95f3779d' and lableid=1928;
					List<String> list = query.list();

					System.out.println("UserId List :: " + list);
					session.close();
					if (!CollectionUtils.isEmpty(list)) {
						try {

							// result = fCMNotificationService.sendFCMNotifications(list, cObj, difference);
							result = fCMNotificationService.sendFCMNotifications(list, cObj, difference);
							resultList.add(result);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

			return resultList;

		} catch (

		Exception e) {

			return null;
		}

	}
}
