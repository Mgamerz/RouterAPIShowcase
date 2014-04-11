package com.cs481.commandcenter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.cs481.commandcenter.activities.LoginActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The utility class holds methods that are accessed by many classes.
 * 
 * @author Mike Perez
 */
public class Utility {

	// Encryption constants for wifi: Enterprise denotes a radius server is used
	// to generate passwords, PSKs are pre shared keys which
	// normal folk use.
	public static String AUTH_OPEN = "none";

	// WEP
	public static String AUTH_WEPAUTO = "wepauto";
	public static String AUTH_WEPOPEN = "wepopen";
	public static String AUTH_WEPSHARED = "wepshared";

	// WPA1
	public static String AUTH_WPA1 = "wpa1psk";
	public static String AUTH_WPA1_ENTERPRISE = "wpa1";

	// WPA2
	public static String AUTH_WPA2 = "wpa2psk";
	public static String AUTH_WPA2_ENTERPRISE = "wpa2";

	// Mixed WPA
	public static String AUTH_WPA1WPA2 = "wpa1wpa2psk";
	public static String AUTH_WPA1WPA2_ENTERPRISE = "wpa1wpa2";

	// ciphers for WPA and WPA2
	public static String CIPHER_AES = "aes";
	public static String CIPHER_TKIP = "tkip";
	public static String CIPHER_TKIPAES = "tkipaes";

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
			Log.e(CommandCenterActivity.TAG, "JSONPROCESSING: ERROR PARSING ECM JSON");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(CommandCenterActivity.TAG, "IOEXCEPTION: ERROR PARSING ECM JSON");
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

		ConnectionInfo ci = new ConnectionInfo();
		DefaultHttpClient client = new DefaultHttpClient();
		Log.i(CommandCenterActivity.TAG, "authinfo: "+authInfo.getUsername());
		Credentials defaultcreds = new UsernamePasswordCredentials(authInfo.getUsername(), authInfo.getPassword());

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
			ci.setAccessUrl(String.format("%sremote/%s?id=%s", baseurl, url, authInfo.getRouterId()));
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
			// Creates the connection URL. Adds an s if it's an SSL (https)
			// connection
			ci.setAccessUrl(String.format("http%s://%s:%s/api/%s", authInfo.isHttps() ? "s" : "", authInfo.getRouterip(), authInfo.getRouterport(), url));
			auth = new AuthScope(authInfo.getRouterip(), authInfo.getRouterport(), AuthScope.ANY_REALM);
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
		final WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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

	/**
	 * Prepares a put request.
	 * 
	 * @param authInfo
	 *            AuthInfo to use for connecting to the router. It is parsed to
	 *            prepare the request.
	 * @param put
	 *            put object that will be used to execute the network request
	 * @param data
	 *            string data to set as the body of the message
	 * @return modified HttpPut object that will be used for the request with
	 *         the correct parameters set.
	 * @throws UnsupportedEncodingException
	 *             If the data to be put onto the body of the post request is
	 *             not encoded properly
	 */
	public static HttpPut preparePutRequest(AuthInfo authInfo, HttpPut put, String data) throws JsonProcessingException, IOException {
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

	/**
	 * Prepares a post request. Same as prepare put request but returns a post
	 * one instead.
	 * 
	 * @param authInfo
	 *            AuthInfo to use for connecting to the router. It is parsed to
	 *            prepare the request.
	 * @param post
	 *            Post object that will be used to execute the network request
	 * @param data
	 *            string data to set as the body of the message
	 * @return modified HttpPost object that will be used for the request with
	 *         the correct parameters set.
	 * @throws UnsupportedEncodingException
	 *             If the data to be put onto the body of the post request is
	 *             not encoded properly
	 */
	public static HttpPost preparePostRequest(AuthInfo authInfo, HttpPost post, String data) throws UnsupportedEncodingException {
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
		Log.i(CommandCenterActivity.TAG, "Final data going to the network: " + post.getEntity().toString());
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
	 * @return string literal, such as fair, good, or poor.
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
	 *            dbm signal strength.
	 * @return human readable percent signal strength
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

	/**
	 * Gets a string for putting to the network. Converts an object into it.
	 * 
	 * @param data
	 *            Data object to put to the network
	 * @param clazz
	 *            class (Name.class) of the object to put to the network, for
	 *            casting
	 * @param mapper
	 *            an objectmapper to use to make the writing with
	 * @return String that can be set as the body of a PUT or POST request
	 */
	public static String getPutString(Object data, Class clazz, ObjectMapper mapper) {
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

	/**
	 * Gets the current theme values and returns it so the calling activity can
	 * set it.
	 * 
	 * @param activity
	 *            Activity calling this method
	 * @return theme resource integer
	 */
	public static int getTheme(Activity activity) {
		// set theme
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
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

	/**
	 * Encrypts the auth info fields and returns one ready to store in the
	 * database
	 * 
	 * @param context
	 *            context to use for resources
	 * @param pin
	 *            pin to use for encrypting
	 * @param unencryptedAuthInfo
	 *            auth info that is not yet encrypted
	 * @return auth info with encrypted username and password fields
	 */
	public static AuthInfo encryptAuthInfo(Context context, String pin, AuthInfo unencryptedAuthInfo) {
		// make a clone of the original so it doesn't change what the original
		// reference points to.
		Parcel p = Parcel.obtain();
		p.writeValue(unencryptedAuthInfo);
		p.setDataPosition(0);
		AuthInfo authInfo = (AuthInfo) p.readValue(AuthInfo.class.getClassLoader());

		Log.i(CommandCenterActivity.TAG, "Encrypting authinfo " + authInfo);
		p.recycle();
		try {
			String uuid = Cryptography.createLocalUUID(context);
			SecretKey secret = Cryptography.generateKey(pin, uuid.getBytes("UTF-8"));
			byte[] encryptedUsername = Cryptography.encryptMsg(authInfo.getUsername(), secret);
			byte[] encryptedPassword = Cryptography.encryptMsg(authInfo.getPassword(), secret);

			authInfo.setUsername(Base64.encodeToString(encryptedUsername, Base64.DEFAULT));
			authInfo.setPassword(Base64.encodeToString(encryptedPassword, Base64.DEFAULT));
			return authInfo;

		} catch (Exception e) {
			Log.e(CommandCenterActivity.TAG, "Unable to encrypt auth info, object cannot be saved.");
			e.printStackTrace();
			// Log.e(CommandCenterActivity.TAG, e.getSta);
			return null;
		}
	}

	/**
	 * Gets a domain name from a url.
	 * 
	 * @param url
	 *            url to parse for a domain name
	 * @return domain name only string.
	 * @throws URISyntaxException
	 *             if the passed string is not a url
	 */
	public static String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	/**
	 * Restarts the application. Whatever calls this will likely not finish
	 * execution as the app will be killed.
	 * 
	 * @param context
	 *            Context to form the future intent with.
	 */
	public static void restartApp(Context context) {
		/*
		 * Intent mStartActivity = new Intent(context, LoginActivity.class); int
		 * mPendingIntentId = 123456; PendingIntent mPendingIntent =
		 * PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
		 * PendingIntent.FLAG_CANCEL_CURRENT); PendingIntent. AlarmManager mgr =
		 * (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		 * mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 200,
		 * mPendingIntent); //System.exit(0);
		 * android.os.Process.killProcess(android.os.Process.myPid());
		 */
		Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	}

	/**
	 * Delete's a profile from the database.
	 * 
	 * @param context
	 *            context to use for deleting the profile.
	 * @param profile
	 *            profile to delete.
	 */
	public static void deleteProfile(Context context, Profile profile) {
		DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
		// dbAdapter.open();
		dbAdapter.deleteProfile(profile);
		// dbAdapter.close();
	}

	/**
	 * Converts an encryption-type string (from the router) to a human readable
	 * one. E.g. none -> Open, wepauto -> WEP.
	 * 
	 * @param context
	 *            Context to load string resources from
	 * @param authmode
	 *            authmode string the router presented.
	 * @return human readable version of the string
	 */
	public static String authToHumanString(Context context, String authmode) {
		Resources resources = context.getResources();
		String[] humanEncryptionTypes = resources.getStringArray(R.array.wifiap_encryptiontype_values);

		if (authmode.equals(AUTH_OPEN)) {
			return humanEncryptionTypes[0];
		} else if (authmode.equals(AUTH_WEPAUTO)) {
			return humanEncryptionTypes[1];
		} else if (authmode.equals(AUTH_WEPOPEN)) {
			return humanEncryptionTypes[2];
		} else if (authmode.equals(AUTH_WEPSHARED)) {
			return humanEncryptionTypes[3];
		} else if (authmode.equals(AUTH_WPA1)) {
			return humanEncryptionTypes[4];
		} else if (authmode.equals(AUTH_WPA1_ENTERPRISE)) {
			return humanEncryptionTypes[5];
		} else if (authmode.equals(AUTH_WPA2)) {
			return humanEncryptionTypes[6];
		} else if (authmode.equals(AUTH_WPA2_ENTERPRISE)) {
			return humanEncryptionTypes[7];
		} else if (authmode.equals(AUTH_WPA1WPA2)) {
			return humanEncryptionTypes[8];
		} else if (authmode.equals(AUTH_WPA1WPA2_ENTERPRISE)) {
			return humanEncryptionTypes[9];
		}
		return "Unknown encryption type";
	}

	/**
	 * Converts an index value to the on-router encryption string. It always
	 * goes in this order: Open wepauto wepopen wepshared wpa1 wpa1 enterprise
	 * wpa2 wpa2 enterprise wpa1wpa2 wpa1wpa2 enterprise
	 * 
	 * @param authIndex
	 *            index to convert to string
	 * @return on-router string for encryption types.
	 */
	public static String indexToAuthString(int authIndex) {
		switch (authIndex) {
		case 0:
			return AUTH_OPEN;
		case 1:
			return AUTH_WEPAUTO;
		case 2:
			return AUTH_WEPOPEN;
		case 3:
			return AUTH_WEPSHARED;
		case 4:
			return AUTH_WPA1;
		case 5:
			return AUTH_WPA1_ENTERPRISE;
		case 6:
			return AUTH_WPA2;
		case 7:
			return AUTH_WPA2_ENTERPRISE;
		case 8:
			return AUTH_WPA1WPA2;
		case 9:
			return AUTH_WPA1WPA2_ENTERPRISE;
		default:
			return AUTH_OPEN; // this should be handled differently, in the
								// event something strange happens...
		}
	}

	/**
	 * Converts an index (likely from a spinner) to a on-router cipher string
	 * for WPA. Always goes in this order: TKIP/AES AES
	 * 
	 * @param encryptionIndex
	 *            index of encryption - 0 is wpa1, 1 is wpa2, 2 is wpa1wpa2. All
	 *            other values will return null.
	 * @param cipherIndex
	 *            Index to convert to on-router string
	 * @return on-router string version of the index
	 */
	public static String indexToCipherString(int encryptionIndex, int cipherIndex) {
		switch (encryptionIndex) {
		case 0:
			// wpa1
			switch (cipherIndex) {
			case 0:
				return CIPHER_TKIP;
			case 1:
				return CIPHER_AES;
			}
		case 1:
			// wpa2
			switch (cipherIndex) {
			case 0:
				return CIPHER_AES;
			}
		case 2:
			// wpa1wpa2
			switch (cipherIndex) {
			case 0:
				return CIPHER_TKIPAES;
			case 1:
				return CIPHER_AES;
			}
		}
		return null;
	}
}
