package com.sharp.sharp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.BankAccounts;
@Repository
public interface BankAccountRepository extends CrudRepository<BankAccounts, Integer> {

List<BankAccounts> findByuserid(String userid);
}
