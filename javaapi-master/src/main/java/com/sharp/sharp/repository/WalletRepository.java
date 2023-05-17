package com.sharp.sharp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.Sharp6Wallet;
@Repository
public interface WalletRepository extends JpaRepository<Sharp6Wallet,String> {
	
	
	
	
	

}
