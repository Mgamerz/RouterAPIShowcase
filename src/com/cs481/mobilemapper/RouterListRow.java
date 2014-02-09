package com.cs481.mobilemapper;

import com.cs481.mobilemapper.responses.ecm.routers.Router;

/**
 * Defines the data that is used to create a list element in the ECM Router
 * list. Also defines data that is relevant when clicked between the user and
 * the apps next tasks (navigating to the management interface).
 * 
 * @author Mgamerz
 * 
 */
public class RouterListRow {
	private String title = "";
	private String subtitle = "";
	private String id = "NOT_SET";
	private Router router;

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public RouterListRow(String title) {
		this.title = title;
	}

	public RouterListRow(Router router, String rId, String title, String subtitle) {
		this.id = rId;
		this.title = title;
		this.subtitle = subtitle;
		this.router = router;
	}
}
