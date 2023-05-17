package com.sharp.sharp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.ImagesEntity;

@Repository
public interface ImageUpoadRespository extends JpaRepository<ImagesEntity, Integer> {

	@Query(value = "SELECT * FROM images_entity WHERE user_id = ?1 ", nativeQuery = true)
	List<ImagesEntity> findAllById(String userId);
	
	@Query(value = "SELECT * FROM images_entity WHERE id = ?1 ", nativeQuery = true)
	ImagesEntity getImageById(long id);

	

}
