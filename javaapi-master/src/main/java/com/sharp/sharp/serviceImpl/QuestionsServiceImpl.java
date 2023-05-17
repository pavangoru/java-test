package com.sharp.sharp.serviceImpl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sharp.sharp.entity.Options;
import com.sharp.sharp.entity.Questions;
import com.sharp.sharp.repository.QeryRepository;
import com.sharp.sharp.service.QuestionsService;

@Service
public class QuestionsServiceImpl implements QuestionsService{

	
	@Autowired
	private QeryRepository queryDao;
	
	@Override
	public List<Questions> getQuestionsBycontestId(String contestId) {
		//Session session = entityManager.unwrap(Session.class);
		try {
			Gson gson=new Gson();
//			Query query = session.createQuery("from Questions where contestid = :contestid");
//			query.setParameter("contestid", contestId);
			List<Questions> qlist = queryDao.getAllQuestionsByContestId(contestId);
			// for shuffling
			Collections.shuffle(qlist);
			// for limiting no of records based on banner value
			List<Questions> quess=qlist.stream().limit(qlist.get(0).getBannerValue()).collect(Collectors.toList());
			for (Questions questions : quess) {
				Options options= gson.fromJson(questions.getOptions(),Options.class);
				System.out.println(options);
				questions.setAnswer(null);
			}
			
			return quess;
		} catch (Exception e) {
			return null;
		} finally {
			//session.close();
		}
	}
}
