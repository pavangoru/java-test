package com.sharp.sharp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.Lables;

@Repository
public interface LableRepository extends JpaRepository<Lables, Integer> {

}
