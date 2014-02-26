package com.cs481.mobilemapper.responses.config.wwan;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WWAN {
	@JsonProperty("radio")
	private ArrayList<Radio> radios;
	@JsonProperty("scan_connected")
	private boolean scan_connected;
	@JsonProperty("survey_interval")
	private Number survey_interval;

	public ArrayList<Radio> getRadios() {
		return this.radios;
	}

	public void setRadio(ArrayList<Radio> radios) {
		this.radios = radios;
	}

	public boolean getScan_connected() {
		return this.scan_connected;
	}

	public void setScan_connected(boolean scan_connected) {
		this.scan_connected = scan_connected;
	}

	public Number getSurvey_interval() {
		return this.survey_interval;
	}

	public void setSurvey_interval(Number survey_interval) {
		this.survey_interval = survey_interval;
	}
}
