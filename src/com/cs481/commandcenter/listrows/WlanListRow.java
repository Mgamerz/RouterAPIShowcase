package com.cs481.commandcenter.listrows;

import com.cs481.commandcenter.responses.status.wlan.WAP;

public class WlanListRow {
	private String title = "";
	private String subtitle = "";
	private WAP wap;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public WAP getWap() {
		return wap;
	}

	public void setWap(WAP wap) {
		this.wap = wap;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public WlanListRow(String title) {
		this.title = title;
	}

	public WlanListRow(WAP wap, String title, String subtitle) {
		this.wap = wap;
		this.title = title;
		this.subtitle = subtitle;
	}
}
