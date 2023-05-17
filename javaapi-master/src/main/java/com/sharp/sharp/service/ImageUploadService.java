package com.sharp.sharp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sharp.sharp.entity.ImagesEntity;

@Service
@Transactional
public interface ImageUploadService {

	ImagesEntity upoadImages(ImagesEntity images);

	List<ImagesEntity> getAllImagesById();

	ImagesEntity getImage(long id);

	
	ImagesEntity updateImageName(String shortName, long Id);

	String deleteImageById(long id);

	String deleteImageById(long id, int categoryId);

}
