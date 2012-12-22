package com.beencoder.amazon.ws;

import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

public class AmazonS3Delegate {
	private static final Logger log = Logger.getLogger(AmazonS3Delegate.class);
	private static final String AWS_ACCESS_KEY = "AKIAJR2MB3GTFL5QAYOA";
	private static final String AWS_SECRET_ACCESS_KEY = "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";
	public static final String BUCKET_ENCODED_NAME = "samba-encoded-files";
	public static final String BUCKET_DEFAULT_NAME = "samba-files";
	private static AWSCredentials awsCredentials;
	private static RestS3Service service;

	public AmazonS3Delegate() {
		try {
			awsCredentials = new AWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_ACCESS_KEY);
			service = new RestS3Service(awsCredentials);
		} catch (S3ServiceException e) {
			log.error(e);
		}
	}

	public S3Object push(FileInputStream inputStream, String fileName) {
		S3Object s3Obj = null;
		try {
			s3Obj = new S3Object();
			s3Obj.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
			s3Obj.setBucketName(BUCKET_DEFAULT_NAME);
			s3Obj.setDataInputStream(inputStream);
			s3Obj.setName(fileName);

			service.putObject(BUCKET_DEFAULT_NAME, s3Obj);

		} catch (Exception e) {
			log.error(e);
		}

		return s3Obj;
	}

	public S3Object[] listEncodedFiles() {
		return listFiles(BUCKET_ENCODED_NAME);
	}

	public S3Object[] listFiles() {
		return listFiles(BUCKET_DEFAULT_NAME);
	}

	private S3Object[] listFiles(String bucketName) {
		S3Object[] s3Objects = null;
		try {
			s3Objects = service.listObjects(bucketName);
		} catch (S3ServiceException e) {
			log.error(e);
		}

		return s3Objects;
	}
}
