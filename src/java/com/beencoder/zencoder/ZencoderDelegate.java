package com.beencoder.zencoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.codehaus.groovy.grails.web.json.JSONObject;

import com.beencoder.BeeVideoJob;
import com.beencoder.amazon.ws.AmazonS3Delegate;
import com.beencoder.json.JsonDateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ZencoderDelegate {
	private static final Logger log = Logger.getLogger(ZencoderDelegate.class);
	private static final String API_BASE_URL = "https://app.zencoder.com/api/v2/jobs/";
	private static final int CONNECTION_TIMEOUT = 4 * 60 * 1000;
	private static final String API_KEY = "10325759e6ada877daf176bc82b16244";
	private static final String S3_URL = "s3://";
	private static final String DEFAULT_ENCODE_FORMAT = "ogg";

	public String encode(String s3BeeVideoName) {
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

		ZencoderPostOutput output = new ZencoderPostOutput();
		output.format = DEFAULT_ENCODE_FORMAT;
		output.label = beeVideoNameWithoutExtension.toString();
		output.url = s3OutputUrl.toString();
		output.s3Public = true;
		ZencoderPostOutput[] outputs = new ZencoderPostOutput[1];
		outputs[0] = output;

		ZencoderPostJob job = new ZencoderPostJob();
		job.input = s3InputUrl.toString();
		job.test = true;
		job.outputs = outputs;

		String response = sendPostRequest(API_BASE_URL, job);
		JSONObject json = new JSONObject(response);

		return json.getString("id");
	}

	public String getJobProgress(String jobId) {
		StringBuilder url = new StringBuilder(API_BASE_URL);
		url.append(jobId);
		url.append("/");
		url.append("progress.json");
		String response = sendGetRequest(url.toString());

		return response;
	}

	public BeeVideoJob getBeeVideo(int jobId) {
		StringBuilder url = new StringBuilder(API_BASE_URL);
		url.append(jobId);
		url.append(".json");
		String response = sendGetRequest(url.toString());

		return getBeeVideoJobFromJson(response);
	}

	private String sendGetRequest(String url) {

		Client client = Client.create();
		client.setConnectTimeout(CONNECTION_TIMEOUT);
		WebResource webResource = client.resource(url.toString());
		webResource.header("Content-Type", "application/json");
		webResource.header("Zencoder-Api-Key", API_KEY);
		String response = webResource.queryParam("api_key", API_KEY).accept(MediaType.APPLICATION_JSON).get(String.class);

		return response;
	}

	private String sendPostRequest(String url, ZencoderPostJob job) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(API_BASE_URL);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Zencoder-Api-Key", API_KEY);

		String encodedJson = new Gson().toJson(job);
		HttpResponse response = null;
		StringBuilder jsonResponse = new StringBuilder();

		try {
			StringEntity input = new StringEntity(encodedJson);
			input.setContentType(MediaType.APPLICATION_JSON);
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

		return jsonResponse.toString();
	}

	private List<BeeVideoJob> getBeeVideoJobListFromJson(String json) {
		Gson gson = getDefaultGsonBuilder();
		Type typeToken = new TypeToken<List<BeeVideoJob>>() {
		}.getType();

		return gson.fromJson(json, typeToken);
	}

	private BeeVideoJob getBeeVideoJobFromJson(String json) {
		Gson gson = getDefaultGsonBuilder();

		return gson.fromJson(json, BeeVideoJob.class);
	}

	private Gson getDefaultGsonBuilder() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new JsonDateDeserializer());
		Gson gson = gsonBuilder.create();
		return gson;
	}

	List<BeeVideoJob> list() {
		StringBuilder url = new StringBuilder(API_BASE_URL);
		String response = sendGetRequest(url.toString());

		return getBeeVideoJobListFromJson(response);
	}
}
