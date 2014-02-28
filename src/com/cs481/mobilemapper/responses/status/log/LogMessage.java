package com.cs481.mobilemapper.responses.status.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = LogMessageDeserializer.class)
public class LogMessage {

	private double timeStamp;
	private String severity;
	private String tag;
	private String message;
	private Object unknown; // "null", not sure what this is.

	public double getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(double timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getUnknown() {
		return unknown;
	}

	public void setUnknown(Object unknown) {
		this.unknown = unknown;
	}

	public LogMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public String getDateString(){
		//This might need to be localized.
		double seconds = getTimeStamp(); 
		long millis = (long) (seconds * 1000); //saves some precision... not all of it as we don't have nanoseconds.
		Date date = new Date(millis);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm:ss,a", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(date);
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(getDateString());
		str.append(": ");
		str.append(getSeverity());
		str.append(" ");
		str.append(getTag());
		str.append(" - ");
		str.append(getMessage());		
		return str.toString();
	}
}
