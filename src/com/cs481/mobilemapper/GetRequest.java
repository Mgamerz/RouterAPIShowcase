package com.cs481.mobilemapper;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

//Create a request in its own Java file, it should not an inner class of a Context
public class GetRequest extends SpringAndroidSpiceRequest<POJO> {

	private String url;

	public GetRequest(String url) {
		super(POJO.class);
		this.url = url;
	}

	@Override
	public POJO loadDataFromNetwork() throws Exception {
		return getRestTemplate().getForObject(url, POJO.class);
	}

	/**
	 * This method generates a unique cache key for this request. In this case
	 * our cache key depends just on the url.
	 * 
	 * @return
	 */
	public String createCacheKey() {
		return "urlreq" + url;
	}
}
