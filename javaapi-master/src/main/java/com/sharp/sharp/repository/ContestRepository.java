package com.sharp.sharp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.Contestdetails;

@Repository
public interface ContestRepository extends JpaRepository<Contestdetails, String> {

	

}
