package com.sharp.sharp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, String> {

}
