package com.sharp.sharp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {

}
