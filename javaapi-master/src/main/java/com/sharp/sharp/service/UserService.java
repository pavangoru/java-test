package com.sharp.sharp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.sharp.entity.Language;
import com.sharp.sharp.entity.LoginSession;
import com.sharp.sharp.entity.ModelQuestions;
import com.sharp.sharp.entity.Questions;
import com.sharp.sharp.entity.ReviewQuestions;
import com.sharp.sharp.entity.UserAnswerHistory;
import com.sharp.sharp.entity.UserMaster;
import com.sharp.sharp.util.MyBusinessException;

@Service
@Transactional
public interface UserService {

	public UserMaster newUserRegister(UserMaster user);

	public UserMaster userLogin(LoginSession entity);

	public String changePassword(UserMaster user);

	public List<Object[]> getALLLanguages();

	public List<Questions> inserttQueries(List<Questions> question);

	public List<Questions> getAllquestions();

	public List<UserMaster> getAllUserlist();

	//public UserMaster loginAdmin(UserMaster user);

	public UserMaster isMobileNumberExist(String mobileNumber);
	

	public UserMaster loginAdmin(String userName, String password);

	public List<ReviewQuestions> reviewQuestion(List<ReviewQuestions> review);

	public List<Questions> getQuestionsBycontestId(String contestId);
	
	

	public UserAnswerHistory submitAnswers(UserAnswerHistory userAnswers) throws MyBusinessException;

	public List<UserAnswerHistory> getQuestionshistorybyuserId(String userId) throws MyBusinessException;

	public List<Questions> getquestionsHistoryByContestId(String contestId, String userId);

	

	ModelQuestions insertModelQuestions(ModelQuestions question);

	public List<ModelQuestions> getmodelQuestionsBycontestId(String contestId);

	public String removeModelQuestionsByCOntestId(String contestId);

	public List<ModelQuestions> getAllInprogressedcontestsModelquestions();

	UserMaster blockorActiveUser(String userId);

	public UserMaster getuserById(String userId);

	

	String updateKalesTOUser(String userId, double mainkales);

	String updateUserDeviceToken(String userId, String token);

	public boolean isUserSubmitAnswers(String userId, String contestId);

}
