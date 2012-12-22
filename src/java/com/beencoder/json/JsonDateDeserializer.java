package com.beencoder.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonDateDeserializer implements JsonDeserializer<Date> {
	private static final Logger log = Logger.getLogger(JsonDateDeserializer.class);
	String[] dateFormats = { "yyyy-MM-dd'T'HH:mm" };

	@Override
	public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		Date formattedDate = null;
		if (json != null) {
			Pattern pattern = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2})");
			Matcher matcher = pattern.matcher(json.toString());

			if (matcher.find()) {
				try {
					formattedDate = DateUtils.parseDate(matcher.group(), dateFormats);
				} catch (ParseException e) {
					log.warn(e);
				}
			}
		}
		return formattedDate;
	}

}
