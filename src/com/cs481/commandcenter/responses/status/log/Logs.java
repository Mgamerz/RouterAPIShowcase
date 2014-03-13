package com.cs481.commandcenter.responses.status.log;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = LogsDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Logs implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9179743097263725622L;
	private ArrayList<LogMessage> logs;

	public ArrayList<LogMessage> getLogs() {
		return logs;
	}

	public void setLogs(ArrayList<LogMessage> logs) {
		this.logs = logs;
	}
}
