package com.beencoder


public class BeeVideo implements Serializable {
	String name
	String outputPath
	String jobId
	Date date

	@Override
	public String toString() {
		return outputPath;
	}
}
