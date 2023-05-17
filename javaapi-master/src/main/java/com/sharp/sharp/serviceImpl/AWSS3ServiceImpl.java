package com.sharp.sharp.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class AWSS3ServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImpl.class);
	@Autowired
	private AmazonS3 s3client;

	@Autowired
	private AmazonS3 amazonS3;
	@Value("${s3.bucketName}")
	private String bucketName;
	@Autowired
	private ResourceLoader resourceLoader;
	@Value("${file.location}")
	private String location;
	@Value("${s3.endpointUrl}")
	private String url;

	public Map<String, Object> fileUploadMultipartFile(String kpostId, MultipartFile file) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			String fileName = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = dateFormat.format(Calendar.getInstance().getTime());
			String FolderName = "sharp6" + "/" + date1;
			String fileNameAWS = file.getName();
			/*
			 * String retStatus = KPOSTMailValidation
			 * .checkSpecialCharacters(KPOSTMailValidation.getFileNameWithExt(fileNameAWS));
			 */
			if (true) {
				Date date = new Date();
				fileName = "sharp6" + date.getTime() + fileNameAWS;
			} else
				fileName = fileNameAWS;
			/*
			 * AmazonS3Client s3Client = (AmazonS3Client)
			 * AmazonS3ClientBuilder.standard().withRegion("us-east-1")
			 * .withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).
			 * withPathStyleAccessEnabled(true) .build();
			 */

// Upload a file as a new object with ContentType and title specified.
			/*
			 * PutObjectRequest request = new PutObjectRequest(bucketName, fileName,
			 * file.getInputStream(), metadata); s3client.putObject(request);
			 */
			
			s3client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(),metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			url = url+fileName;
			
			URL s3Url = s3client.getUrl(bucketName, fileName);
			/*
			 * System.out.println("file ready to upload in service impl");
			 * s3client.putObject( new PutObjectRequest(bucketName, fileName,
			 * file.getInputStream(), metadata));
			 */

			System.out.println("file uploaded successfully");
			resultMap.put("status", "success");
			resultMap.put("uploadFile", s3Url);
			// System.out.println("file uploaded success fully");
		} catch (IOException ioe) {
			// LOGEER.error("IOException: " + ioe.getMessage());
			resultMap.put("status", "failure");
			resultMap.put("Error Message: ", ioe.getMessage());
		} catch (AmazonServiceException ase) {
			/*
			 * logger.
			 * info("Caught an AmazonServiceException from PUT requests, rejected reasons:"
			 * ); logger.info("Error Message:    " + ase.getMessage());
			 * logger.info("HTTP Status Code: " + ase.getStatusCode());
			 * logger.info("AWS Error Code:   " + ase.getErrorCode());
			 * logger.info("Error Type:       " + ase.getErrorType());
			 * logger.info("Request ID:       " + ase.getRequestId());
			 */
			resultMap.put("status", "failure");
			resultMap.put("Error Message: ", ase.getMessage());
			resultMap.put("HTTP Status Code: ", ase.getStatusCode());
			resultMap.put("AWS Error Code:  ", ase.getErrorCode());
			resultMap.put("Error Type:  ", ase.getErrorType());
			resultMap.put("Request ID:", ase.getRequestId());
			throw ase;
		} catch (AmazonClientException ace) {
			LOGGER.info("Caught an AmazonClientException: ");
			LOGGER.info("Error Message: " + ace.getMessage());
			resultMap.put("status", "failure");
			resultMap.put("Error Message: ", ace.getMessage());
			throw ace;
		}
		return resultMap;
	}

	// @Async annotation ensures that the method is executed in a different
	// background thread
	// but not consume the main thread.
	@Async
	public String uploadFile(final MultipartFile multipartFile) {
		LOGGER.info("File upload in progress.");
		try {
			final File file = convertMultiPartFileToFile(multipartFile);
			uploadFileToS3Bucket(bucketName, file);
			LOGGER.info("File upload is completed.");
			file.delete(); // To remove the file locally created in the project folder.
			final String uniqueFileName = LocalDateTime.now() + "_" + file.getName();
			String fileUrl = "https://s3-acceleratee.amazonaws.com" + "/" + bucketName + "/" + uniqueFileName;
			return fileUrl;
		} catch (final AmazonServiceException ex) {
			LOGGER.info("File upload is failed.");
			LOGGER.error("Error= {} while uploading file.", ex.getMessage());
			return "";
		}
	}

	private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
		final File file = new File(multipartFile.getOriginalFilename());
		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(multipartFile.getBytes());
		} catch (final IOException ex) {
			LOGGER.error("Error converting the multi-part file to file= ", ex.getMessage());
		}
		return file;
	}

	private void uploadFileToS3Bucket(final String bucketName, final File file) {
		final String uniqueFileName = LocalDateTime.now() + "_" + file.getName();
		LOGGER.info("Uploading file with name= " + uniqueFileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
		amazonS3.putObject(putObjectRequest);
	}

	public ByteArrayOutputStream downloadFile(String keyName) {
		try {
			S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));

			InputStream is = s3object.getObjectContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[4096];
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, len);
			}

			return baos;
		} catch (IOException ioe) {
			// logger.error("IOException: " + ioe.getMessage());
		} catch (AmazonServiceException ase) {
			/*
			 * logger.
			 * info("sCaught an AmazonServiceException from GET requests, rejected reasons:"
			 * ); logger.info("Error Message:    " + ase.getMessage());
			 * logger.info("HTTP Status Code: " + ase.getStatusCode());
			 * logger.info("AWS Error Code:   " + ase.getErrorCode());
			 * logger.info("Error Type:       " + ase.getErrorType());
			 * logger.info("Request ID:       " + ase.getRequestId());
			 */
			throw ase;
		} catch (AmazonClientException ace) {
			/*
			 * logger.info("Caught an AmazonClientException: ");
			 * logger.info("Error Message: " + ace.getMessage());
			 */
			throw ace;
		}

		return null;
	}

	public byte[] getFile(final String id) throws IOException {
		System.out.println("File to be accessed :: " + getLocation(id));
		Resource resource = this.resourceLoader.getResource(getLocation(id));
		return FileCopyUtils.copyToByteArray(resource.getInputStream());
	}

	public void putFile(final String id) throws IOException {
		System.out.println("File to be placed :: " + getLocation(id));
		OutputStream outputStream = ((WritableResource) this.resourceLoader.getResource(getLocation(id)))
				.getOutputStream();
		FileCopyUtils.copy(createFile(id), outputStream);
	}

	private byte[] createFile(final String id) {
		String txt = "This file is created for the user," + id;
		return txt.getBytes();
	}

	private String getLocation(final String id) {
		// location/123.csv
		return this.location + "/" + id;
	}
}
