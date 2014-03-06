package com.cs481.commandcenter.responses.status.log;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = LogsDeserializer.class)

public class Logs {
	private ArrayList<LogMessage> logs;

	public ArrayList<LogMessage> getLogs() {
		return logs;
	}

	public void setLogs(ArrayList<LogMessage> logs) {
		this.logs = logs;
	}
}
