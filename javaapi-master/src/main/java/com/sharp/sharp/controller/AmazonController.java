package com.sharp.sharp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sharp.sharp.awss3.AmazonImage;
import com.sharp.sharp.awss3.AmazonS3ImageService;

import lombok.Getter;

@Getter
@RestController
@RequestMapping("/amazon")
public class AmazonController {

	@Autowired
	private AmazonS3ImageService amazonS3ImageService;

	@PostMapping("/images")
	public ResponseEntity<AmazonImage> insertImages(@RequestPart(value = "images") MultipartFile images) {
		return ResponseEntity.ok(amazonS3ImageService.insertImages(images,"123456"));
	}

}
