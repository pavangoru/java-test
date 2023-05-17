package com.sharp.sharp.awss3;

import lombok.Data;


public class AmazonImage {

	
	private String amazonUserImageId;

	
	private String imageUrl;

	public String getAmazonUserImageId() {
		return amazonUserImageId;
	}

	public void setAmazonUserImageId(String amazonUserImageId) {
		this.amazonUserImageId = amazonUserImageId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	

}
