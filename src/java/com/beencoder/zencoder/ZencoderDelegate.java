package com.beencoder.zencoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONObject;

import com.beencoder.BeeVideo;
import com.beencoder.amazon.ws.AmazonS3Delegate;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;

public class ZencoderDelegate {
	private static final Logger log = Logger.getLogger(ZencoderDelegate.class);
	private static final int CONNECTION_TIMEOUT = 4 * 60 * 1000;
	private static final String API_KEY = "10325759e6ada877daf176bc82b16244";
	private static final String S3_URL = "s3://";
	private static final String DEFAULT_ENCODE_FORMAT = "ogg";

	public BeeVideo encode(String s3BeeVideoName) {

		StringBuilder s3InputUrl = new StringBuilder();
		s3InputUrl.append(S3_URL);
		s3InputUrl.append(AmazonS3Delegate.BUCKET_DEFAULT_NAME);
		s3InputUrl.append("/");
		s3InputUrl.append(s3BeeVideoName);

		String beeVideoNameWithoutExtension = s3BeeVideoName.substring(0, s3BeeVideoName.lastIndexOf("."));
		StringBuilder s3OutputUrl = new StringBuilder();
		s3OutputUrl.append(S3_URL);
		s3OutputUrl.append(AmazonS3Delegate.BUCKET_ENCODED_NAME);
		s3OutputUrl.append("/");
		s3OutputUrl.append(beeVideoNameWithoutExtension);
		s3OutputUrl.append(".");
		s3OutputUrl.append(DEFAULT_ENCODE_FORMAT);

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://app.zencoder.com/api/v2/jobs");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Zencoder-Api-Key", API_KEY);

		String encodedJson = String.format("{'test':'true','input':'%s','outputs':['label':'%s','format':'%s','url':'%s']}", s3InputUrl.toString(),
				beeVideoNameWithoutExtension, DEFAULT_ENCODE_FORMAT, s3OutputUrl.toString());
		HttpResponse response = null;
		StringBuilder jsonResponse = new StringBuilder();
		try {
			StringEntity input = new StringEntity(encodedJson);
			input.setContentType("application/json");
			post.setEntity(input);
			response = httpClient.execute(post);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			while ((output = bufferedReader.readLine()) != null) {
				jsonResponse.append(output);
			}
			bufferedReader.close();
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			log.error(e);
		}

		JSONObject job = new JSONObject(jsonResponse.toString());
		return getBeeVideoFromJson(job);
	}

	public String getJobProgress(String jobId) {
		StringBuilder url = new StringBuilder();
		url.append("https://app.zencoder.com/api/v2/jobs/");
		url.append(jobId);
		url.append("/");
		url.append("progress.json");
		url.append("?api_key=");
		url.append(API_KEY);

		ApacheHttpClient httpClient = ApacheHttpClient.create();
		httpClient.setConnectTimeout(CONNECTION_TIMEOUT);
		WebResource resource = httpClient.resource(url.toString());
		ClientResponse response = resource.get(ClientResponse.class);

		return response.getEntity(String.class);
	}

	public BeeVideo getBeeVideo(int jobId) {
		StringBuilder url = new StringBuilder();
		url.append("https://app.zencoder.com/api/v2/jobs/");
		url.append(jobId);
		url.append(".json");
		url.append("?api_key=");
		url.append(API_KEY);

		ApacheHttpClient httpClient = ApacheHttpClient.create();
		httpClient.setConnectTimeout(CONNECTION_TIMEOUT);
		WebResource resource = httpClient.resource(url.toString());
		ClientResponse response = resource.get(ClientResponse.class);

		String json = response.getEntity(String.class);

		JSONObject object = new JSONObject(json);
		JSONObject job = (JSONObject) object.get("job");

		return getBeeVideoFromJson(job);
	}

	private BeeVideo getBeeVideoFromJson(JSONObject job) {

		String jobId = job.getString("id");

		JSONArray outputMediaFiles = null;
		if (job.containsKey("output_media_files")) {
			outputMediaFiles = job.getJSONArray("output_media_files");
		} else {
			outputMediaFiles = job.getJSONArray("outputs");
		}

		JSONObject outputMedia = outputMediaFiles.getJSONObject(0);
		String outputUrl = outputMedia.getString("url");
		String videoName = outputMedia.getString("label");

		BeeVideo beeVideo = new BeeVideo();
		beeVideo.setJobId(jobId);

		Date formattedDate = null;
		if (job.containsKey("submitted_at")) {
			String submittedDate = job.getString("submitted_at");
			String[] dateFormats = { "yyyy-MM-dd'T'HH:mm:ss" };
			String dateWithoutTimezone = submittedDate.toString().substring(0, submittedDate.toString().lastIndexOf("-"));
			try {
				formattedDate = DateUtils.parseDate(dateWithoutTimezone, dateFormats);
			} catch (ParseException e) {
				log.error(e);
			}
		}
		beeVideo.setDate(formattedDate);
		beeVideo.setOutputPath(outputUrl);
		beeVideo.setName(videoName);

		return beeVideo;
	}
}
