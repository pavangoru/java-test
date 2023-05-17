package com.sharp.sharp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sharp.sharp.entity.Questions;

@Service
public interface QuestionsService {

	public List<Questions> getQuestionsBycontestId(String contestId);
}
