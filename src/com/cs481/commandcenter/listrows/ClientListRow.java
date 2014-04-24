package com.cs481.commandcenter.listrows;

import com.cs481.commandcenter.responses.status.lan.Client;

/**
 * Allows the list of clients on a router to be
 * used as a row for listing
 * @author Mike Perez
 */
public class ClientListRow {
	private String title = "";
	private String subtitle = "";
	private Client client;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public ClientListRow(String title) {
		this.title = title;
	}

	public ClientListRow(Client client, String title, String subtitle) {
		this.client = client;
		this.title = title;
		this.subtitle = subtitle;
	}
}
