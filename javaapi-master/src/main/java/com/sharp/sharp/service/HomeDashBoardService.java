package com.sharp.sharp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.sharp.sharp.entity.Category;
import com.sharp.sharp.entity.Channel;
import com.sharp.sharp.entity.ContestVivo;
import com.sharp.sharp.entity.Contestdetails;
import com.sharp.sharp.entity.Lables;
import com.sharp.sharp.entity.ShowDetails;
import com.sharp.sharp.entity.ShowDetailsVivo;
import com.sharp.sharp.entity.ShowsVivo;
import com.sharp.sharp.entity.ShuffleMoney;
import com.sharp.sharp.entity.Status;
import com.sharp.sharp.entity.Teams;

@Service
@Transactional
public interface HomeDashBoardService {
	Category addCategory(Category category);

	List<Category> getAllCategory();

	Optional<Category> getCategoryBYId(Category category);

	Channel createChannel(Channel category);

	List<Channel> getAllChannels();

	Optional<Channel> getChannelById(Channel channel);

	ShowDetails crateShow(ShowDetails show);

	List<ShowsVivo> getAllShows();

	String deleteShow(String showId);

	Contestdetails createCOntest(Contestdetails contest);

	Contestdetails updateContest(Contestdetails contest);

	// List<Contestdetails> getContestById(String contestid);

	List<Contestdetails> getAllContests();

	Teams saveTeam(Teams teams);

	// Status saveStatus(Contestdetails contest);

	List<Teams> getAllTeams();

	List<ContestVivo> getAllShowByUserId(String userId);

	Set<Contestdetails> getContestsByUserId(String userId);

	String userjoinContest(String userId, String contestId, int lableId);

	Status saveStatus(Status status);

	List<ShowDetailsVivo> getShowById(String showId);

	List<Lables> getAlllables();

	List<Contestdetails> getContestsBySubCatagoryId(String subCatagoryId,String userId);

	String updatelables(String contestid, ContestVivo vivo);

	List<Contestdetails> getContestById(String contestid, String userId);

	Map<String, Object> shuffleRankings(String contestId, String userId, int lableId);

	String userwithdrawLable(String userId, String contestId, int lableId);

	Map<String, Object> winnings(int lableId);

	ArrayList<Object> getuserJoinedLables(String contestId, String userId);

	List<Contestdetails> gameHistoryByUserId(String userId);

	List<Contestdetails> completedContest(String mobileNumber);

	List<Contestdetails> inProgessContestList();

	HashMap<String, Object> getuserLables(String contestId, String userId);

	String remooveCategorybyId(String categoryid);

	String remooveChannel(String channelId);

	String remoovecontestById(String contestid);

	List<Contestdetails> inprogesscontestById(String contestid);

	String inactiveContestById(String contestid, int lableId);

	List<ShuffleMoney> getShuffleList(int lableId);

	Long joinedUserCount(String contestId, int lableId);

	String isUserJoinedInLable(String userId, String contestId, int lableId);

	List<JSONObject> getAfterThreeMinutes();

	// List<Contestdetails> getContestById(String contestid, String userId);

}
