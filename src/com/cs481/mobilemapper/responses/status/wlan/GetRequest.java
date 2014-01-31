package com.cs481.mobilemapper.responses.status.wlan;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class GetRequest extends SpringAndroidSpiceRequest<Wlan> {

	  private String routerip;

	  public GetRequest(String routerip) {
	    super(Wlan.class);
	    this.routerip = routerip;
	  }

	  @Override
	  public Wlan loadDataFromNetwork() throws Exception {

	    String url = String.format("http://%s/api/status/wlan", routerip);
	    RestTemplate rt = getRestTemplate();
	    DefaultHttpClient client = new DefaultHttpClient();
	    Credentials defaultcreds = new UsernamePasswordCredentials("admin", "routerpass");

	    client.getCredentialsProvider()
	      .setCredentials(new AuthScope("192.168.0.1", 80, AuthScope.ANY_REALM), defaultcreds);
	    // The HttpComponentsClientHttpRequestFactory uses the
	    // org.apache.http package to make network requests
	    rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
	    return rt.getForObject(url, Wlan.class);
	  }

	  /**
	   * This method generates a unique cache key for this request. In this case
	   * our cache key depends just on the keyword.
	   * @return
	   */
	  public String createCacheKey() {
	      return "wlan_status";
	  }
}
