package com.sharp.sharp.awss3;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AmazonS3ImageService extends AmazonClientService {

	// Upload a List of Images to AWS S3.
	public AmazonImage insertImages(MultipartFile panIMage, String panNumber) {
		// List<AmazonImage> amazonImages = new ArrayList<>();
		AmazonImage amazonImages = uploadImageToAmazon(panIMage, panNumber);
		return amazonImages;
	}

	// Upload image to AWS S3.
	public AmazonImage uploadImageToAmazon(MultipartFile multipartFile, String panNumber) {

		// Valid extensions array, like jpeg/jpg and png.
		List<String> validExtensions = Arrays.asList("jpeg", "jpg", "png", "JPG", "PNG", "JPEG");

		// Get extension of MultipartFile
		String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		if (!validExtensions.contains(extension)) {
			// If file have a invalid extension, call an Exception.
			System.out.println("error");
			return null;
		} else {

			// Upload file to Amazon.
			String url = uploadMultipartFile(multipartFile, panNumber);

			// Save image information on MongoDB and return them.
			AmazonImage amazonImage = new AmazonImage();
			amazonImage.setImageUrl(url);
			return amazonImage;

		}

	}

	public void removeImageFromAmazon(AmazonImage amazonImage) {
		String fileName = amazonImage.getImageUrl().substring(amazonImage.getImageUrl().lastIndexOf("/") + 1);
		getClient().deleteObject(new DeleteObjectRequest(getBucketName(), fileName));

	}

	// Make upload to Amazon.
	private String uploadMultipartFile(MultipartFile multipartFile, String panNumber) {
		String fileUrl;

		try {
			// Get the file from MultipartFile.
			File file = FileUtils.convertMultipartToFile(multipartFile);

			// Extract the file name.
			String noSpaceTime = new Timestamp(new Date().getTime()).toString().replaceAll(" ", "-");
			String fileName = noSpaceTime + "_" + panNumber + "_"
					+ FileUtils.generateFileName(multipartFile);

			// Upload file.
			uploadPublicFile(fileName, file);

			// Delete the file and get the File Url.
			file.delete();
			fileUrl = getUrl().concat(fileName);
		} catch (IOException e) {

			// If IOException on conversion or any file manipulation, call exception.

			throw null;
		}

		return fileUrl;
	}

	// Send image to AmazonS3, if have any problems here, the image fragments are
	// removed from amazon.
	// Font:
	// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#putObject%28com.amazonaws.services.s3.model.PutObjectRequest%29
	private void uploadPublicFile(String fileName, File file) {
		getClient().putObject(new PutObjectRequest(getBucketName(), fileName, file)
				.withCannedAcl(CannedAccessControlList.PublicReadWrite));
	}

}