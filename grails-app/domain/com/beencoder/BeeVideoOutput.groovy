package com.beencoder

import com.google.gson.annotations.SerializedName

class BeeVideoOutput {
	@SerializedName("id")
	String id
	@SerializedName("label")
	String label
	@SerializedName("state")
	String state
	@SerializedName("url")
	String url
	@SerializedName("file_size_bytes")
	Long size
	@SerializedName("duration_in_ms")
	Long duration


	@Override
	public String toString() {
		return label;
	}
}
