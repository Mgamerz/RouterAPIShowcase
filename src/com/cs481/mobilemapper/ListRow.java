package com.cs481.mobilemapper;

public class ListRow {
	private String title = "";
	private String subtitle = "";
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
	
	public ListRow(String title){
		this.title = title;
	}
	
	public ListRow(String title, String subtitle){
		this.title = title;
		this.subtitle = subtitle;
	}
}
