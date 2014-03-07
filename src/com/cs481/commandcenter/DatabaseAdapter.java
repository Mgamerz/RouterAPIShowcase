package com.cs481.commandcenter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cs481.commandcenter.activities.CommandCenterActivity;

public class DatabaseAdapter {

	private static final String DATABASE_NAME = "RouterDB";
	private static final String DATABASE_TABLE_ECM = "ECM";
	private static final String DATABASE_TABLE_DIRECT = "Direct";
	private static final int DATABASE_VERSION = 1;
	/* ECM - TABLE_ECM_ROW_PROFILE_NAME, TABLE_ECM_ROW_USERNAME, TABLE_ECM_ROW_PASSWORD, TABLE_ECM_ROW_ROUTERID*/
	private final static String TABLE_ECM_ROW_PROFILE_NAME = "profilename";
	private final static String TABLE_ECM_ROW_USERNAME = "username";
	private final static String TABLE_ECM_ROW_PASSWORD = "password";
	private final static String TABLE_ECM_ROW_ROUTERID = "routerid";
	/* DIRECT - TABLE_DIRECT_ROW_PROFILE_NAME, TABLE_DIRECT_ROW_USERNAME, TABLE_DIRECT_ROW_PASSWORD, TABLE_DIRECT_ROW_ROUTERIP, TABLE_DIRECT_ROW_ROUTERPORT, TABLE_DIRECT_ROW_HTTPS */
	private final static String TABLE_DIRECT_ROW_PROFILE_NAME = "profilename";
	private final static String TABLE_DIRECT_ROW_USERNAME = "username";
	private final static String TABLE_DIRECT_ROW_PASSWORD = "password";
	private final static String TABLE_DIRECT_ROW_ROUTERIP = "routerip";
	private final static String TABLE_DIRECT_ROW_ROUTERPORT = "routerport";
	private final static String TABLE_DIRECT_ROW_HTTPS = "https";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DatabaseAdapter(Context ctx)
	{
	    this.context = ctx;
	    DBHelper = new DatabaseHelper(context);
	    }
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			String ecmTableQueryString = 	
					"create table " +
					DATABASE_TABLE_ECM +
					" (" +
					TABLE_ECM_ROW_PROFILE_NAME + " text unique not null," +
					TABLE_ECM_ROW_USERNAME + " text," +
					TABLE_ECM_ROW_PASSWORD + " text," +
					TABLE_ECM_ROW_ROUTERID + " text primary key not null" +
					");";
			
			String directTableQueryString = 	
					"create table " +
					DATABASE_TABLE_DIRECT +
					" (" +
					TABLE_DIRECT_ROW_PROFILE_NAME + " text unique not null," +
					TABLE_DIRECT_ROW_USERNAME + " text," +
					TABLE_DIRECT_ROW_PASSWORD + " text," +
					TABLE_DIRECT_ROW_ROUTERIP  + " text primary key not null," +
					TABLE_DIRECT_ROW_ROUTERPORT + " integer," +
					TABLE_DIRECT_ROW_HTTPS + " text" +
					");";
			
			db.execSQL(ecmTableQueryString);
			db.execSQL(directTableQueryString);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			 Log.i(CommandCenterActivity.TAG, "Upgrading database from version " + oldVersion
		              + " to "
		              + newVersion + ", which will destroy all old data");
		        db.execSQL("DROP TABLE IF EXISTS ECM");
		        db.execSQL("DROP TABLE IF EXISTS Direct");
		        onCreate(db);

		}
	}
	
	/**
	 * Opens the database. 
	 * @return 
	 * @throws SQLException
	 */
	public DatabaseAdapter open() throws SQLException
	{
	    db = DBHelper.getWritableDatabase();
	    return this;
	}
	
	/**
	 * Closes the database.  
	 */
	public void close()
	{
	    DBHelper.close();
	}
	
	/**
	 * Inserts a profile into the database
	 * @param profile, the profile to be inserted
	 * @return positive if insertion successful
	 */
	public long insertProfile(Profile profile) {
		boolean ecm = profile.getAuthInfo().isEcm();
		open();
		if (ecm) {
			return insertProfileEcm(profile);
		} else
			return insertProfileDirect(profile);
	}
	
	/**
	 * Inserts a profile into the ECM table in the database
	 * @param profile
	 * @return positive on success
	 */
	public long insertProfileEcm(Profile profile) {
		
		AuthInfo authInfo = profile.getAuthInfo();
		
		ContentValues values = new ContentValues();
		values.put(TABLE_ECM_ROW_PROFILE_NAME, profile.getProfileName());
		values.put(TABLE_ECM_ROW_USERNAME, authInfo.getUsername());
		values.put(TABLE_ECM_ROW_PASSWORD, authInfo.getPassword());
		values.put(TABLE_ECM_ROW_ROUTERID, authInfo.getRouterId());
		
		return db.insert(DATABASE_TABLE_ECM, null, values);
	}
	
	/**
	 * Inserts a profile into the Direct table in the database
	 * @param profile
	 * @return positive on success
	 */
	public long insertProfileDirect(Profile profile) {

		AuthInfo authInfo = profile.getAuthInfo();

		ContentValues values = new ContentValues();
		values.put(TABLE_DIRECT_ROW_PROFILE_NAME, profile.getProfileName());
		values.put(TABLE_DIRECT_ROW_USERNAME, authInfo.getUsername());
		values.put(TABLE_DIRECT_ROW_PASSWORD, authInfo.getPassword());
		values.put(TABLE_DIRECT_ROW_ROUTERIP, authInfo.getRouterip());
		values.put(TABLE_DIRECT_ROW_ROUTERPORT, authInfo.getRouterport());
		values.put(TABLE_DIRECT_ROW_HTTPS, Boolean.toString(authInfo.isHttps()));

		return db.insert(DATABASE_TABLE_DIRECT, null, values);
	}
	
	/**
	 * Deletes a profile from the database
	 * @param profile, the profile to be deleted
	 * @return positive if deletion successful
	 */
	public long deleteProfile(Profile profile) {
		boolean ecm = profile.getAuthInfo().isEcm();
		open();
		if (ecm) {
			return deleteProfileEcm(profile);
		} else
			return deleteProfileDirect(profile);
	}
	
	/**
	 * Deletes a profile from the ECM table in the database
	 * @param profile
	 * @return positive on success
	 */
	public long deleteProfileEcm(Profile profile) {
		AuthInfo authInfo = profile.getAuthInfo();
		//routerid is primary key
		String where = TABLE_ECM_ROW_ROUTERID + "= ?" ;
		String[] args = {authInfo.getRouterId()};
		return db.delete(DATABASE_TABLE_ECM, where, args);
	}

	/**
	 * Deletes a profile from the Direct table in the database
	 * @param profile
	 * @return positive on success
	 */
	public long deleteProfileDirect(Profile profile) {
		AuthInfo authInfo = profile.getAuthInfo();
		//routerip is primary key
		String where = TABLE_DIRECT_ROW_ROUTERIP + "= ?";
		String[] args = {authInfo.getRouterip()};
		return db.delete(DATABASE_TABLE_DIRECT, where, args);
	}
	
	/**
	 * Deletes all profiles in both Direct and ECM tables
	 * @return positive on success
	 */
	public long deleteAllProfiles() {
		
		if (deleteAllEcmProfiles() > 0 && deleteAllDirectProfiles() > 0)
			return 1;
		else
			return -1;
	}
	
	/**
	 * 
	 * @return positive on success
	 */
	public long deleteAllEcmProfiles(){
		return db.delete(DATABASE_TABLE_ECM, null, null);
	}
	
	/**
	 * 
	 * @return positive on success
	 */
	public long deleteAllDirectProfiles(){
		return  db.delete(DATABASE_TABLE_DIRECT, null, null);
	}
	
	/**
	 * Gets all profiles currently stored in the ECM table
	 * @return ArrayList of all profiles in ECM table
	 */
	public ArrayList<Profile> getEcmProfiles() {
		String select = "SELECT * FROM ECM";
		ArrayList<Profile> profileArrayList = new ArrayList<Profile>();
		Cursor c = db.rawQuery(select, null);
		/*
		 * ECM - TABLE_ECM_ROW_PROFILE_NAME, TABLE_ECM_ROW_USERNAME,
		 * TABLE_ECM_ROW_PASSWORD, TABLE_ECM_ROW_ROUTERID
		 */
		if (c != null && c.moveToFirst()) {
			while (c.isAfterLast() == false) {
				Profile profile = new Profile();
				AuthInfo authInfo = new AuthInfo();
				
				profile.setProfileName(c.getString(c.getColumnIndex(TABLE_ECM_ROW_PROFILE_NAME)));
				authInfo.setUsername(c.getString(c.getColumnIndex(TABLE_ECM_ROW_USERNAME)));
				authInfo.setPassword(c.getString(c.getColumnIndex(TABLE_ECM_ROW_PASSWORD)));
				authInfo.setRouterId(c.getString(c.getColumnIndex(TABLE_ECM_ROW_ROUTERID)));
				authInfo.setEcm(true);
				profile.setAuthInfo(authInfo);

				profileArrayList.add(profile);
				c.moveToNext();
			}
		}
		return profileArrayList;
	}
	
	/**
	 * Gets all profiles currently stored in the Direct table
	 * @return ArrayList of all profiles in Direct table
	 */
	public ArrayList<Profile> getDirectProfiles() {
		String select = "SELECT * FROM DIRECT";
		ArrayList<Profile> profileArrayList = new ArrayList<Profile>();
		Cursor c = db.rawQuery(select, null);
		/*
		 * DIRECT - TABLE_DIRECT_ROW_PROFILE_NAME, TABLE_DIRECT_ROW_USERNAME,
		 * TABLE_DIRECT_ROW_PASSWORD, TABLE_DIRECT_ROW_ROUTERIP,
		 * TABLE_DIRECT_ROW_ROUTERPORT, TABLE_DIRECT_ROW_HTTPS
		 */
		if (c != null && c.moveToFirst()) {
			while (c.isAfterLast() == false) {
				Profile profile = new Profile();
				AuthInfo authInfo = new AuthInfo();
				
				profile.setProfileName(c.getString(c.getColumnIndex(TABLE_DIRECT_ROW_PROFILE_NAME)));
				authInfo.setUsername(c.getString(c.getColumnIndex(TABLE_DIRECT_ROW_USERNAME)));
				authInfo.setPassword(c.getString(c.getColumnIndex(TABLE_DIRECT_ROW_PASSWORD)));
				authInfo.setRouterip(c.getString(c.getColumnIndex(TABLE_DIRECT_ROW_ROUTERIP)));
				authInfo.setRouterport(c.getInt(c.getColumnIndex(TABLE_DIRECT_ROW_ROUTERPORT)));
				boolean value = c.getInt(c.getColumnIndex(TABLE_DIRECT_ROW_HTTPS))>0;
				authInfo.setHttps(value);
				authInfo.setEcm(false);

				profile.setAuthInfo(authInfo);

				profileArrayList.add(profile);
				c.moveToNext();
			}
		}

		return profileArrayList;
	}
	
	/**
	 * Gets a list of all profiles stored in the database and returns it as an arraylist
	 * @return Arraylist of all profiles stored in the database, or an empty one if there are none.
	 */
	public ArrayList<Profile> getProfiles(){
		ArrayList<Profile> allProfiles = new ArrayList<Profile>();
		
		allProfiles.addAll(getDirectProfiles());
		allProfiles.addAll(getEcmProfiles());
		
		return allProfiles;
	}
}