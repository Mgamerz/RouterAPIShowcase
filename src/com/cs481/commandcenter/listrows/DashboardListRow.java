package com.cs481.commandcenter.listrows;

/**
 * Defines the data that is used to create a list element in 
 * the dashboard list rows. 
 * @author Mike Perez
 */
public class DashboardListRow {
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

	public DashboardListRow(String title) {
		this.title = title;
	}

	public DashboardListRow(int lWLAN, String title, String subtitle) {
		this.id = lWLAN;
		this.title = title;
		this.subtitle = subtitle;
	}
}
