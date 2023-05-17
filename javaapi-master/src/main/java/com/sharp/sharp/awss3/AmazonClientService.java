package com.sharp.sharp.awss3;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Service
public class AmazonClientService {

	// AmazonS3 Client, in this object you have all AWS API calls about S3.
	private AmazonS3 amazonS3;

	// Your bucket URL, this URL is https://{bucket-name}.s3-{region}.amazonaws.com/
	// If you don't know if your URL is ok, send one file to your bucket using AWS
	// and
	// click on them, the file URL contains your bucket URL.
	@Value("${s3.endpointUrl}")
	private String url;

	// Your bucket name.
	@Value("${s3.bucketName}")
	private String bucketName;

	// The IAM access key.
	@Value("${s3.accessKeyId}")
	private String accessKey;

	// The IAM secret key.
	@Value("${s3.secretKey}")
	private String secretKey;

	// Getters for parents.
	protected AmazonS3 getClient() {
		return amazonS3;
	}

	protected String getUrl() {
		return url;
	}

	protected String getBucketName() {
		return bucketName;
	}

	// This method are called after Spring starts AmazonClientService into your
	// container.
	@PostConstruct
	private void init() {

		// Init your AmazonS3 credentials using BasicAWSCredentials.
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		// Start the client using AmazonS3ClientBuilder, here we goes to make a standard
		// cliente, in the
		// region SA_EAST_1, and the basic credentials.

		this.amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1 /* Regions.US_EAST_1 */)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

}
