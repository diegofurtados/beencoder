package com.beencoder

import com.google.gson.annotations.SerializedName


public class BeeVideo implements Serializable {
	@SerializedName("id")
	Long id
	@SerializedName("created_at")
	Date creationDate
	@SerializedName("finished_at")
	Date finishDate
	@SerializedName("state")
	String state
	@SerializedName("output_media_files")
	List<BeeVideoOutput> output


	@Override
	public String toString() {
		return id;
	}
}
