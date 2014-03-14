package com.cs481.commandcenter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;

import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The utility class holds methods that are accessed by many classes.
 * 
 * @author Mgamerz
 * 
 */
public class Utility {

	public static String convertToDataSegment(String str) {
		// JsonObject json = new JsonObject()
		str = str.substring(0, str.length() - 1); // remove last }
		str = str.substring(1, str.length()); // remove first {
		str = str.replaceFirst("\"data\":", "");
		return str;
	}

	/**
	 * Converts an ECM reply into a normal direct-interface based response
	 * 
	 * @param str
	 *            String to normalize
	 * @return Normalized string
	 */
	public static String normalizeECM(ObjectMapper mapper, String str) {
		try {
			JsonNode root = mapper.readTree(str);
			// Log.w(CommandCenterActivity.TAG, "Json Tree:");
			// Log.w(CommandCenterActivity.TAG, root.toString());
			root = root.get("data");
			// Log.w(CommandCenterActivity.TAG, "Descend to data:");
			// Log.w(CommandCenterActivity.TAG, root.toString());
			root = root.get(0);
			// Log.w(CommandCenterActivity.TAG, "Descend to index 0:");
			// Log.w(CommandCenterActivity.TAG, root.toString());
			return root.toString();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			Log.e(CommandCenterActivity.TAG,
					"JSONPROCESSING: ERROR PARSING ECM JSON");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(CommandCenterActivity.TAG,
					"IOEXCEPTION: ERROR PARSING ECM JSON");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Prepares a connection and makes it transparent to other classes what the
	 * type of router they are accessing, be it ECM or local-based.
	 * 
	 * @param url
	 *            Suburl of the API to connect to, e.g. status/wlan
	 * @param authInfo
	 *            Authinfo package defining authorization and router
	 *            information.
	 * @return ConnectionInfo bundle describing this connection
	 */
	public static ConnectionInfo prepareConnection(Context context, String url, AuthInfo authInfo) {
		//https stuff
		// Load CAs from an InputStream
		// (could be from a resource or ByteArrayInputStream or ...)
		/*CertificateFactory cf = CertificateFactory.getInstance("X.509");
		// From https://www.washington.edu/itconnect/security/ca/load-der.crt
		InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
		Certificate ca;
		try {
		    ca = cf.generateCertificate(caInput);
		    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
		} finally {
		    caInput.close();
		}

		// Create a KeyStore containing our trusted CAs
		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		keyStore.load(null, null);
		keyStore.setCertificateEntry("ca", ca);

		// Create a TrustManager that trusts the CAs in our KeyStore
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		tmf.init(keyStore);

		// Create an SSLContext that uses our TrustManager
		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, tmf.getTrustManagers(), null);

		// Tell the URLConnection to use a SocketFactory from our SSLContext
		URL url = new URL("https://certs.cac.washington.edu/CAtest/");
		HttpsURLConnection urlConnection =
		    (HttpsURLConnection)url.openConnection();
		urlConnection.setSSLSocketFactory(context.getSocketFactory());
		InputStream in = urlConnection.getInputStream();
		copyInputStreamToOutputStream(in, System.out);
		
		*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		ConnectionInfo ci = new ConnectionInfo();
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials(
				authInfo.getUsername(), authInfo.getPassword());

		// set auth type
		AuthScope auth;
		if (authInfo.isEcm()) {
			SharedPreferences advancedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			
			boolean isUsingAdvanced = advancedPrefs.getBoolean(context.getResources().getString(R.string.prefskey_advanced), false);
			String baseurl;
			if (isUsingAdvanced) { 
				baseurl = advancedPrefs.getString(context.getResources().getString(R.string.prefskey_ecmapiurl), context.getResources().getString(R.string.ecmapi_base_url));
			} else {
				baseurl = context.getResources().getString(R.string.ecmapi_base_url);
			}
			ci.setAccessUrl(String.format(
					"%sremote/%s?id=%s", baseurl, url,
					authInfo.getRouterId()));
			Log.i(CommandCenterActivity.TAG, "Get Request to " + url);
			String scope = null; 
			try {
				scope = getDomainName(baseurl);
			} catch (URISyntaxException e) {
				Log.e(CommandCenterActivity.TAG, "WARNING: couldn't extract hostname from supplied ECM base URL. The app will accept items from all hosts, even untrusted ones!");
				e.printStackTrace();
			}
			auth = new AuthScope(scope, 443);
		} else {
			// Creates the connection URL. Adds an s if it's an SSL (https) connection
			ci.setAccessUrl(String.format("http%s://%s:%s/api/%s", authInfo.isHttps() ? "s" : "",
					authInfo.getRouterip(), authInfo.getRouterport(), url));
			auth = new AuthScope(authInfo.getRouterip(),
					authInfo.getRouterport(), AuthScope.ANY_REALM);
		}
		client.getCredentialsProvider().setCredentials(auth, defaultcreds);
		ci.setClient(client);
		return ci;
	}

	/**
	 * Gets the default gateway this device is connected to
	 * 
	 * @param context
	 *            Context to use to get the service (the calling activity)
	 * @return String of the gateway
	 */
	public static String getDefaultGateway(Context context) {
		final WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		final DhcpInfo dhcp = manager.getDhcpInfo();
		// This method is deprecated because we use IPv4 and the new one
		// is IPv6. IPv6 is way more complicated and used in more
		// telecom related things (public facing things). It still works
		// so ignore the deprecation.
		return Formatter.formatIpAddress(dhcp.gateway);
	}

	/**
	 * Converts RSSI into a signal strength percentage
	 * 
	 * @param rssi
	 *            rssi the router gives you
	 * @return human readable percentage
	 */
	public static int rssiToSignalStrength(int rssi) {
		return 2 * (rssi + 100);
	}

	public static HttpPut preparePutRequest(AuthInfo authInfo, HttpPut put,
			String data) throws JsonProcessingException, IOException {
		// TODO Auto-generated method stub
		// Log.i(CommandCenterActivity.TAG, "PPR: "+r.getData());

		if (authInfo.isEcm()) {
			// ECM prepare
			// ObjectMapper mapper = new ObjectMapper();
			put.setHeader("Content-Type", "application/json");
			// JsonNode json = mapper.readTree(data);
			// json = json.get("data");
			// Log.i(CommandCenterActivity.TAG, json.toString());
			put.setEntity(new StringEntity(data, "UTF-8"));
		} else {
			put.setHeader("Content-Type", "application/x-www-form-urlencoded");
			put.setEntity(new StringEntity("data=" + data, "UTF-8"));
		}
		return put;
	}

	public static HttpPost preparePostRequest(AuthInfo authInfo, HttpPost post,
			String data) throws JsonProcessingException, IOException {
		// TODO Auto-generated method stub
		// Log.i(CommandCenterActivity.TAG, "PPR: "+r.getData());

		if (authInfo.isEcm()) {
			// ECM prepare
			// ObjectMapper mapper = new ObjectMapper();
			post.setHeader("Content-Type", "application/json");
			// JsonNode json = mapper.readTree(data);
			// json = json.get("data");
			// Log.i(CommandCenterActivity.TAG, json.toString());
			post.setEntity(new StringEntity(data, "UTF-8"));
		} else {
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setEntity(new StringEntity("data=" + data, "UTF-8"));
		}
		Log.i(CommandCenterActivity.TAG, "Final data going to the network: "
				+ post.getEntity().toString());
		return post;
	}

	/**
	 * Converts a rssi value to a string such as poor, good, or excellent.
	 * 
	 * @param rssi
	 *            RSSI value to translate
	 * @param resources
	 *            Refernece to a resources object - since a static class can't
	 *            access it.
	 * @return
	 */
	public static String rssiToSignalLiteral(int rssi, Resources resources) {
		Log.i(CommandCenterActivity.TAG, "Rssi value: " + rssi);
		int signalQuality = rssiToHumanSignal(rssi);
		switch (Math.abs(signalQuality - 1) / 25) {
		case 0:
			return resources.getString(R.string.poor);
		case 1:
			return resources.getString(R.string.fair);
		case 2:
			return resources.getString(R.string.good);
		case 3:
			return resources.getString(R.string.excellent);
		default:
			return resources.getString(R.string.unknown);
		}
	}

	/**
	 * Converts RSSI to a human readable "percent" for the most part. It's not
	 * perfect as some values fall out of the range.
	 * 
	 * @param dbm
	 * @return
	 */
	public static int rssiToHumanSignal(int dbm) {
		int signalQuality;
		if (dbm <= -100) {
			signalQuality = 0;
		} else if (dbm >= -50) {
			signalQuality = 100;
		} else {
			signalQuality = Utility.rssiToSignalStrength(dbm) - 1;
			if (signalQuality < 0) {
				signalQuality = 0; // rollunder check
			}
		}
		return signalQuality;
	}

	public static String getPutString(Object data, Class clazz,
			ObjectMapper mapper) {
		// } else {
		try {
			String result;
			// if (ecm) {
			// ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			// result = ow.writeValueAsString(clazz.cast(data));
			// Log.i(CommandCenterActivity.TAG, "Data to post: " + result);

			result = mapper.writeValueAsString(clazz.cast(data));
			// if (!ecm){
			// result = "data="+
			// }
			return result;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
		return null;
	}

	/**
	 * Saves a profile to the database.
	 * 
	 * @param profile
	 *            Profile to be saved.
	 */
	public static boolean saveProfile(Context context, Profile profile) {
		// TODO Need help here. I don't understand what activity/context to pass
		// on to this db as param
		DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
		if (dbAdapter.insertProfile(profile) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Deletes all saved profiles from the database
	 * 
	 * @param context
	 *            Context that this method will use for environment information
	 */
	public static void deleteAllProfiles(Context context) {
		// TODO Need help here. I don't understand what activity/context to pass
		// on to this db as param
		DatabaseAdapter dbAdapter = new DatabaseAdapter(context).open();
		dbAdapter.deleteAllProfiles();
		dbAdapter.close();
	}

	/**
	 * Reads the database list of profiles and returns rebuilt profile objects.
	 * 
	 * @return Arraylist of Profile objects read from the database.
	 */
	public static ArrayList<Profile> getProfiles(Context context) {

		Log.i(CommandCenterActivity.TAG, "Context for profiles: " + context);
		DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
		dbAdapter.open();
		ArrayList<Profile> profiles = dbAdapter.getProfiles();
		dbAdapter.close();
		return profiles;
	}

	public static int getTheme(Activity activity) {
		// set theme
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		Resources resources = activity.getResources();
		String theme = mPrefs.getString(resources.getString(R.string.prefskey_theme), "Red");
		// Theme strings
		
		String[] colors = resources.getStringArray(R.array.theme_names);
		String red = colors[0];
		String blue = colors[1];
		String green = colors[2];
		String black = colors[3];

		if (theme.equals(blue)) {
			return R.style.BlueAppTheme;
		} else if (theme.equals(black)) {
			return R.style.BlackAppTheme;
		} else if (theme.equals(green)) {
			return R.style.GreenAppTheme;
		}
		return R.style.RedAppTheme;
	}

	public static AuthInfo encryptAuthInfo(Context context, String pin,
			AuthInfo unencryptedAuthInfo) {
		// make a clone of the original so it doesn't change what the original
		// reference points to.
		Parcel p = Parcel.obtain();
		p.writeValue(unencryptedAuthInfo);
		p.setDataPosition(0);
		AuthInfo authInfo = (AuthInfo) p.readValue(AuthInfo.class
				.getClassLoader());
		p.recycle();
		try {
			String uuid = Cryptography.createLocalUUID(context);
			SecretKey secret = Cryptography.generateKey(pin,
					uuid.getBytes("UTF-8"));
			byte[] encryptedUsername = Cryptography.encryptMsg(
					authInfo.getUsername(), secret);
			byte[] encryptedPassword = Cryptography.encryptMsg(
					authInfo.getPassword(), secret);

			authInfo.setUsername(Base64.encodeToString(encryptedUsername,
					Base64.DEFAULT));
			authInfo.setPassword(Base64.encodeToString(encryptedPassword,
					Base64.DEFAULT));
			return authInfo;

		} catch (Exception e) {
			Log.e(CommandCenterActivity.TAG,
					"Unable to encrypt auth info, object cannot be saved.");
			e.printStackTrace();
			// Log.e(CommandCenterActivity.TAG, e.getSta);
			return null;
		}
	}
	
	public static String getDomainName(String url) throws URISyntaxException {
	    URI uri = new URI(url);
	    String domain = uri.getHost();
	    return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}
