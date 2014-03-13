package com.cs481.commandcenter.responses.status.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = LogMessageDeserializer.class)
public class LogMessage implements Parcelable {

	private double timeStamp;
	private String severity;
	private String tag;
	private String message;
	private String trace; // traceback, can be null.

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

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public LogMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public String getDateString(){
		//This might need to be localized.
		double seconds = getTimeStamp(); 
		long millis = (long) (seconds * 1000); //saves some precision... not all of it as we don't have nanoseconds.
		Date date = new Date(millis);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d h:mm:ssa", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getDefault());
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

    protected LogMessage(Parcel in) {
        timeStamp = in.readDouble();
        severity = in.readString();
        tag = in.readString();
        message = in.readString();
        trace = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(timeStamp);
        dest.writeString(severity);
        dest.writeString(tag);
        dest.writeString(message);
        dest.writeString(trace);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LogMessage> CREATOR = new Parcelable.Creator<LogMessage>() {
        @Override
        public LogMessage createFromParcel(Parcel in) {
            return new LogMessage(in);
        }

        @Override
        public LogMessage[] newArray(int size) {
            return new LogMessage[size];
        }
    };
}