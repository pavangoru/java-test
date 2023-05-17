package com.sharp.sharp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.Questions;

@Repository
public interface QeryRepository extends JpaRepository<Questions, String>{
	
	
	@Query(value = "SELECT * from questions where contestId = :contestId", nativeQuery = true)
	List<Questions> getAllQuestionsByContestId(String contestId);

}
