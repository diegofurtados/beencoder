package com.beencoder.zencoder;

import com.google.gson.annotations.SerializedName;

public class ZencoderPostOutput {
	public String label;
	public String format;
	public String url;
	@SerializedName("public")
	public boolean s3Public;
}
