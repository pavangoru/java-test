package com.sharp.sharp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.Teams;

@Repository
public interface TeamRepository extends JpaRepository<Teams, String> {

}
