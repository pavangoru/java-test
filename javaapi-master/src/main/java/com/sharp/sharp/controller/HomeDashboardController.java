package com.sharp.sharp.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.websocket.server.PathParam;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sharp.sharp.awss3.AmazonClientService;
import com.sharp.sharp.awss3.AmazonImage;
import com.sharp.sharp.awss3.AmazonS3ImageService;
import com.sharp.sharp.entity.Category;
import com.sharp.sharp.entity.Channel;
import com.sharp.sharp.entity.ContestVivo;
import com.sharp.sharp.entity.Contestdetails;
import com.sharp.sharp.entity.Lables;
import com.sharp.sharp.entity.ShowDetails;
import com.sharp.sharp.entity.ShowDetailsVivo;
import com.sharp.sharp.entity.ShowsVivo;
import com.sharp.sharp.entity.ShuffleMoney;
import com.sharp.sharp.entity.Teams;
import com.sharp.sharp.service.HomeDashBoardService;
import com.sharp.sharp.service.PaymentService;
import com.sharp.sharp.service.PushNotificationService;
import com.sharp.sharp.service.UserService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.Sharp6Validation;

@RestController
@RequestMapping("/dashboard")
public class HomeDashboardController {
	private static final Logger logger = Logger.getLogger(HomeDashboardController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private HomeDashBoardService dashBoardService;
	@Autowired
	private PaymentService gatewayService;
	@Autowired
	private PushNotificationService pushNotificationService;
	@Autowired
	private AmazonS3ImageService s3imageService;

	/**
	 * create categories
	 */
	@PostMapping("/addcategory/")
	public Map<String, Object> createCategory(@RequestBody Category category) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Category resultObj = dashBoardService.addCategory(category);
		if (!Sharp6Validation.isEmpty(resultObj)) {
			logger.info("successs");
			resultMap.put(Constants.STATUS, true);
			resultMap.put("category", resultObj);

		} else {
			logger.error("error");
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "category adding failed");

		}
		return resultMap;

	}

	@PostMapping("/remooveCategory/")
	public Map<String, Object> remooveCategorybyId(@RequestParam("categoryid") String categoryid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultObj = dashBoardService.remooveCategorybyId(categoryid);
		if (resultObj.equals(Constants.SUCCESS)) {
			logger.info("successs");
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", "category remooved successfully");

		} else {
			logger.error("error");
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "category remooved failed");

		}
		return resultMap;

	}

	/**
	 * get categories
	 */
	@GetMapping("/GetAllcategory/")
	public Map<String, Object> getCategary() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Category> resultList = dashBoardService.getAllCategory();
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("categoriesList", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to fetch categories");
		}

		return resultMap;

	}

	/**
	 * get category by Id
	 */
	@PostMapping("/GetcategoryById/")
	public Map<String, Object> getCategaryById(@RequestBody Category category) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Optional<Category> resultList = dashBoardService.getCategoryBYId(category);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("category", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to fetch category");
		}

		return resultMap;

	}

	@PostMapping("/createChannel/")
	public Map<String, Object> createChannel(@RequestBody Channel channel) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		channel.setChannelid(channel.getChannelname().toUpperCase());
		channel.setCreateddate(new Timestamp(System.currentTimeMillis()));
		Channel resultList = dashBoardService.createChannel(channel);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("channel", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to Create Channel");
		}

		return resultMap;

	}

	@PostMapping("/remooveChannel/")
	public Map<String, Object> remooveChannel(@RequestParam("channelId") String channelId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String resultList = dashBoardService.remooveChannel(channelId);
		if (resultList.equals(Constants.SUCCESS)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("Value", "Channel remooved successfully");
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to remoove Channel");
		}

		return resultMap;

	}

	/**
	 * get categories
	 */
	@GetMapping("/GetChannels/")
	public Map<String, Object> getAllChannels() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Channel> resultList = dashBoardService.getAllChannels();
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("channeList", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to fetch Channels");
		}

		return resultMap;

	}

	/**
	 * get Channel by Id
	 */
	@PostMapping("/GetChannelById/")
	public Map<String, Object> getChannelById(@RequestBody Channel category) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Optional<Channel> resultList = dashBoardService.getChannelById(category);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("channel", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to fetch Channel");
		}

		return resultMap;

	}

	/**
	 * add show
	 */
	@PostMapping("/addShow/")
	public Map<String, Object> addShow(@RequestBody ShowDetails show) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ShowDetails resultList = dashBoardService.crateShow(show);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("show", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to create Show");
		}

		return resultMap;

	}

	/**
	 * get show by Id
	 */
	@PostMapping("/getAllShowByUserId/")
	public Map<String, Object> getAllShowByUserId(@PathParam("userId") String userId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ContestVivo> resultList = dashBoardService.getAllShowByUserId(userId);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("showslist", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to fetch Show");
		}

		return resultMap;
	}

	/**
	 * get Shows
	 */
	@GetMapping("/GetALLShows/")
	public Map<String, Object> getAllShows() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ShowsVivo> resultList = dashBoardService.getAllShows();
		// LinkedList<ContestVivo> list = new LinkedList<ContestVivo>();
		if (!Sharp6Validation.isEmpty(resultList)) {

			resultMap.put(Constants.STATUS, true);
			resultMap.put("showslist", resultList);

		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to fetch Shows");
		}

		return resultMap;

	}

	/**
	 * get show by Id
	 */
	@PostMapping("/GetShowById/")
	public Map<String, Object> getShowById(@RequestParam("showId") String showId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ShowDetailsVivo> resultList = dashBoardService.getShowById(showId);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("show", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to fetch Show");
		}

		return resultMap;
	}

	/**
	 * delete show by Id
	 */
	@PostMapping("/removeshowbyId/")
	public Map<String, Object> remooveShow(@RequestParam("showId") String showId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultList = dashBoardService.deleteShow(showId);
		if (resultList.equals(Constants.SUCCESS)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", "Show Remooved Successfully");
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to Remove Show");
		}

		return resultMap;

	}

	/**
	 * create contest
	 */
	@SuppressWarnings("static-access")
	@PostMapping(value = "/createContest/")
	public Map<String, Object> createContest(@RequestPart(value = "bannerImage",required=false) MultipartFile bannerImage,
			@RequestParam("highilight") String highilight) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(highilight);// response will be the json String
		ContestVivo vivo = gson.fromJson(object, ContestVivo.class);
		// Status status = dashBoardService.saveStatus(contest);
		Contestdetails contest = new Contestdetails();
		contest.setCategoryid(vivo.getCategoryid());
		contest.setSubcategoryid(vivo.getSubcategoryid());
		contest.setChannelid(vivo.getChannelid());
		contest.setContestdate(vivo.getContestdate());
		// contest.setLables(vivo.getLableList());
		contest.setShowid(vivo.getShowid());
		contest.setShowname(vivo.getShowName());
		contest.setCategoryname(vivo.getCategoryname());
		contest.setSubcategoryname(vivo.getSubcategoryname());
		contest.setChannelname(vivo.getChannelname());
		contest.setTeam1imageid(vivo.getTeam1ImageId());
		contest.setTeam2imageid(vivo.getTeam2ImageId());
		contest.setBannerValue(vivo.getBannerValue());
		if (bannerImage != null) {
			AmazonClientService as3Img = new AmazonClientService();

			AmazonImage list = s3imageService.insertImages(bannerImage, vivo.getContestname1() + vivo.getBannerValue());
			contest.setBannerImage(list.getImageUrl());
		}
		if (!Sharp6Validation.isEmpty(vivo.getContestname1()))
			contest.setContestname1(vivo.getContestname1());
		if (!Sharp6Validation.isEmpty(vivo.getContestname2()))
			contest.setContestname2(vivo.getContestname2());
		contest.setLanguageid(vivo.getLanguageid());
		contest.setLanguage(vivo.getLanguagename());

		contest.setContestdate(vivo.getContestdate());
		contest.setLableidList(vivo.getLableidList());

		contest.setContesttime(vivo.getContesttime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String dateTimeString = vivo.getContestdate() + " " + vivo.getContesttime();
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
		contest.setContestDateandTime(Timestamp.valueOf(dateTime));
		if (!Sharp6Validation.isEmpty(vivo.getTeam1shortName()))
			contest.setTeamname1_shortname(vivo.getTeam1shortName());
		if (!Sharp6Validation.isEmpty(vivo.getTeam2shortName()))
			contest.setTeamname2_shortname(vivo.getTeam2shortName());
		// contest.setShare_money(vivo.getShareMoney());
		contest.setCreateddate(String.valueOf(new Date().getTime()));

		Contestdetails resultList = dashBoardService.createCOntest(contest);
		/*
		 * dashBoardService.updatelables(resultList.getContestid(), vivo); Status status
		 * = new Status(); status.setContestid(resultList.getContestid());
		 * status.setStatusname("inprogress"); status.setStatusdesc("inprogress");
		 * status.setCreateddate(String.valueOf(new Date().getTime())); Status
		 * statusResult = dashBoardService.saveStatus(status);
		 * System.out.println("statusResult=====" + statusResult);
		 */
		if (!Sharp6Validation.isEmpty(resultList)) {
			// pushNotificationService.sendPushNotificationToToken(request);
			resultMap.put(Constants.STATUS, true);
			resultMap.put("contest", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to Create Contest");
		}

		return resultMap;

	}

	/**
	 * 
	 * remoovecontest
	 */
	@PostMapping("/remoovecontestById/")
	public Map<String, Object> remoovecontestById(@RequestParam("contestid") String contestid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultList = dashBoardService.remoovecontestById(contestid);
		if (resultList.equals(Constants.SUCCESS)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("value", "COntest remooved successfully");
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "contest not remooved");
		}

		return resultMap;

	}

	/**
	 * 
	 * getContestById
	 */
	@PostMapping("/contestById/")
	public Map<String, Object> getContestById(@RequestParam("contestid") String contestid,
			@RequestParam("userId") String userId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Contestdetails> resultList = dashBoardService.getContestById(contestid, userId);
		if (!Sharp6Validation.isEmpty(resultList)) {
			boolean result = userService.isUserSubmitAnswers(userId, contestid);
			resultMap.put(Constants.STATUS, true);
			resultMap.put("isUserJOinedContest", resultList.get(0).isJoinedInLables());

			resultMap.put("contest", resultList);
			resultMap.put("answerSubmit", result);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "contest not Exist");
		}

		return resultMap;

	}

	/**
	 * 
	 * getContestsByuserId My joins
	 */
	@PostMapping("/contestsByUserId/")
	public Map<String, Object> getContestsByUserId(@RequestParam("userId") String userId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Set<Contestdetails> resultList = dashBoardService.getContestsByUserId(userId);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("mycontestlist", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "contests not Exist");
		}

		return resultMap;

	}

	/**
	 * 
	 * joinCOntest
	 */
	@PostMapping("/userjoinContest/")
	public Map<String, Object> userjoinContest(@RequestParam("userId") String userId,
			@RequestParam("contestId") String contestId, @RequestParam("lableId") int lableId,
			@RequestParam("totalPlayers") int totalPlayers) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		isUserJoinedInLable(userId, contestId, lableId);
		String isLableJoined = dashBoardService.isUserJoinedInLable(userId, contestId, lableId);
		if (isLableJoined.equals("true")) {
			List<ShuffleMoney> shuffleList = dashBoardService.getShuffleList(lableId);
			// int rankend = shuffleList.get(shuffleList.size() - 1).getRankend();
			Long joinedUserCount = dashBoardService.joinedUserCount(contestId, lableId);
			if (joinedUserCount < totalPlayers) {
				String remooveEntryAmountFromWallet = gatewayService.remooveEntryAmountFromWallet(lableId, contestId,
						userId);
				String result = dashBoardService.userjoinContest(userId, contestId, lableId);
				if (result.equalsIgnoreCase(Constants.SUCCESS)) {

					resultMap.put(Constants.STATUS, true);
					resultMap.put("joincontest", result);
				} else {
					resultMap.put(Constants.STATUS, false);
					resultMap.put(Constants.ERROR_MSG, "contests not Exist");
				}
			} else {
				resultMap.put(Constants.STATUS, false);
				resultMap.put(Constants.ERROR_MSG, "contest full");
			}
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "already joined in this game");
		}
		return resultMap;

	}

	private void isUserJoinedInLable(String userId, String contestId, int lableId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * withDrawCOntest
	 */
	@PostMapping("/userwithdrawContest/")
	public Map<String, Object> userwithdrawLable(@RequestParam("userId") String userId,
			@RequestParam("contestId") String contestId, @RequestParam("lableId") int lableId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = dashBoardService.userwithdrawLable(userId, contestId, lableId);
		long l = 0;
		if (result.equalsIgnoreCase(Constants.SUCCESS)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("withdrawAmount", l);
			resultMap.put("withdrawContest", result);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "contests not Exist");
		}

		return resultMap;

	}

	/**
	 * 
	 * getAllcontests
	 */
	@GetMapping("/getAllContests/")
	public Map<String, Object> getAllContests() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Contestdetails> resultList = dashBoardService.getAllContests();
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("contetstslist", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "contest not Exist");
		}

		return resultMap;

	}

	@PostMapping("/completedContestbyMobileNumber/")
	public Map<String, Object> completedContest(@RequestParam("mobileNumber") String mobileNumber) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Contestdetails> resultList = dashBoardService.completedContest(mobileNumber);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("completeList", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "no contests found");
		}

		return resultMap;

	}

	/**
	 * create team
	 */
	@PostMapping("/createTeam/")
	public Map<String, Object> createTeams(@RequestBody Teams teams) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		teams.setCreateddate(new Timestamp(System.currentTimeMillis()));
		Teams teamList = dashBoardService.saveTeam(teams);

		if (!Sharp6Validation.isEmpty(teamList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("team", teamList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to Create Team");
		}

		return resultMap;

	}

	@GetMapping("/getAllTeams/")
	public Map<String, Object> fetchTeams() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<Teams> teamList = dashBoardService.getAllTeams();

		if (!Sharp6Validation.isEmpty(teamList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("teamslist", teamList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to Fetch Teams");
		}

		return resultMap;

	}

	@GetMapping("/getAlllables/")
	public Map<String, Object> getAlllables() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<Lables> lablist = dashBoardService.getAlllables();

		if (!Sharp6Validation.isEmpty(lablist)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("labelsList", lablist);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "unable to lablelist");
		}

		return resultMap;

	}

	@GetMapping("/getContestsBySubCatagoryId/{subCatagoryId}/{userId}/")
	public Map<String, Object> getContestsBySubCatagoryId(@PathVariable("subCatagoryId") String subCatagoryId,
			@PathVariable("userId") String userId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<Contestdetails> contestList = dashBoardService.getContestsBySubCatagoryId(subCatagoryId, userId);

		if (!Sharp6Validation.isEmpty(contestList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("lablesList", contestList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "no contests avaiable");
		}

		return resultMap;

	}

	@PostMapping("/winningsBoard/")
	public Map<String, Object> winningsBoard(@RequestParam("lableId") int lableId) {
		// Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, Object> winningBoard = dashBoardService.winnings(lableId);
		return winningBoard;
	}

	@PostMapping("/shuffleRankings/")
	public Map<String, Object> shuffleRankings(@RequestParam("contestId") String contestId,
			@RequestParam("userId") String userId, @RequestParam("lableId") int lableId) {
		// Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, Object> shuffleList = dashBoardService.shuffleRankings(contestId, userId, lableId);

		/*
		 * if (!Sharp6Validation.isEmpty(shuffleList)) {
		 * shuffleList.put(Constants.STATUS, true);
		 * 
		 * 
		 * } else { shuffleList.put(Constants.STATUS, false);
		 * shuffleList.put("errorValue", "no contests avaiable"); }
		 */

		return shuffleList;

	}

	@PostMapping("/gameHistoryByUserId/")
	public Map<String, Object> gameHistoryByUserId(@RequestParam("userId") String userId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<Contestdetails> gameHistoryByUserId = dashBoardService.gameHistoryByUserId(userId);

		if (!Sharp6Validation.isEmpty(gameHistoryByUserId)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("GamesHistory", gameHistoryByUserId);

		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "no games availabe");
		}

		return resultMap;

	}

	@GetMapping("/inProgessContestList/")
	public Map<String, Object> inProgessContestList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<Contestdetails> inprogress = dashBoardService.inProgessContestList();

		if (!Sharp6Validation.isEmpty(inprogress)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("inProgessContestList", inprogress);

		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "no contests available");
		}

		return resultMap;

	}

	@RequestMapping(value = "/contestWinningsByUserandContestId/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> contestWinningsByUserandContestId(@RequestParam String contestId,
			@RequestParam String userId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		/* try { */
		resultMap = dashBoardService.getuserLables(contestId, userId);

		// List<Questions> qlist = userService.getquestionsHistoryByContestId(contestId,
		// userId);

		/*
		 * if (!Sharp6Validation.isEmpty(getuserJoinedLables)) {
		 * resultMap.put(Constants.STATUS, true); resultMap.put("LableList",
		 * getuserJoinedLables); //resultMap.put("questionsHistory", qlist); } else {
		 * resultMap.put(Constants.STATUS, false); resultMap.put("value",
		 * "No questions history Available"); } } catch (Exception e) {
		 * resultMap.put(Constants.STATUS, false); resultMap.put("errormessage",
		 * e.getMessage()); resultMap.put("errorvalue", e.getLocalizedMessage()); }
		 */
		resultMap.put(Constants.URLPATH, "/contestWinningsByUserandContestId");
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@PostMapping("/inprogesscontestById/")
	public Map<String, Object> inprogesscontestById(@RequestParam("contestid") String contestid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Contestdetails> resultList = dashBoardService.inprogesscontestById(contestid);
		if (!Sharp6Validation.isEmpty(resultList)) {
			resultMap.put(Constants.STATUS, true);
			resultMap.put("contest", resultList);
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "contest not Exist");
		}

		return resultMap;

	}

	@PostMapping("/inactiveContestById/")
	public Map<String, Object> inactiveContestById(@RequestParam("contestid") String contestid, int lableId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultList = dashBoardService.inactiveContestById(contestid, lableId);
		if (resultList.contains(Constants.SUCCESS)) {
			resultMap.put(Constants.STATUS, true);
			// resultMap.put("lable", resultList);
			resultMap.put("data", "lable inactive successfully");
		} else {
			resultMap.put(Constants.STATUS, false);
			resultMap.put(Constants.ERROR_MSG, "contest not Exist");
		}

		return resultMap;

	}

	@Scheduled(cron = "0 0/1 * * * ?")
	@GetMapping("/sendNotification/")
	public List<JSONObject> sendNotification() {
		List<JSONObject> resultList = dashBoardService.getAfterThreeMinutes();
		return resultList;

	}

}
