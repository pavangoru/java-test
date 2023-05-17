package com.sharp.sharp.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSS3Config {

	// Access key id will be read from the application.properties file during the
	// application intialization.
	@Value("${s3.accessKeyId}")
	private String accessKeyId;
	// Secret access key will be read from the application.properties file during
	// the application intialization.
	@Value("${s3.secretKey}")
	private String secretAccessKey;
	// Region will be read from the application.properties file during the
	// application intialization.
	@Value("${s3.region}")
	private String region;

	@Bean
	public AmazonS3 s3client() {

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

		return s3Client;
	}
}