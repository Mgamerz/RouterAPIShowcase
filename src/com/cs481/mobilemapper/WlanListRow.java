package com.cs481.mobilemapper;

public class WlanListRow {
	private String title = "";
	private String subtitle = "";
	private int id = -1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public WlanListRow(String title) {
		this.title = title;
	}

	public WlanListRow(int wlan_id, String title, String subtitle) {
		this.id = wlan_id;
		this.title = title;
		this.subtitle = subtitle;
	}
}
