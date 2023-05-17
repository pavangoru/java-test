package com.sharp.sharp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.sharp.entity.ContestastantsEntity;

@Service
@Transactional
public interface ContestatantService {

	List<ContestastantsEntity> createNewContestant(List<ContestastantsEntity> contestnts);

}
