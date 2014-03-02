package com.cs481.mobilemapper;

import java.util.ArrayList;

import com.cs481.mobilemapper.activities.CommandCenterActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
			// TODO Auto-generated constructor stub
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
			// TODO Need to fix all of this for our database
			 Log.i(CommandCenterActivity.TAG, "Upgrading database from version " + oldVersion
		              + " to "
		              + newVersion + ", which will destroy all old data");
		        db.execSQL("DROP TABLE IF EXISTS ECM");
		        db.execSQL("DROP TABLE IF EXISTS Direct");
		        onCreate(db);

		}
	}
	
	public DatabaseAdapter open() throws SQLException
	{
	    db = DBHelper.getWritableDatabase();
	    return this;
	}
	 
	//---closes the database---
	public void close()
	{
	    DBHelper.close();
	}
	
	public long insertProfile(Profile profile) {
		boolean ecm = profile.getAuthInfo().isEcm();
		open();
		if (ecm) {
			return insertProfileEcm(profile);
		} else
			return insertProfileDirect(profile);
	}

	public long insertProfileEcm(Profile profile) {

		AuthInfo authInfo = profile.getAuthInfo();

		ContentValues values = new ContentValues();
		values.put(TABLE_ECM_ROW_PROFILE_NAME, profile.getProfileName());
		values.put(TABLE_ECM_ROW_USERNAME, authInfo.getUsername());
		values.put(TABLE_ECM_ROW_PASSWORD, authInfo.getPassword());
		values.put(TABLE_ECM_ROW_ROUTERID, authInfo.getRouterId());

		return db.insert(DATABASE_TABLE_ECM, null, values);
	}

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
	/*
	"SELECT * FROM user WHERE email=:email AND password=:password";
	String select = "SELECT * FROM ECM WHERE routerid = ?"
	String[] arguments = { }
	return db.rawQuery(select, arguments);*/
	public ArrayList<Profile> getProfiles() {
		String select = "SELECT * FROM ECM";
		ArrayList<Profile> profileArrayList = null;
		Cursor c = db.rawQuery(select, null);
		/*
		 * ECM - TABLE_ECM_ROW_PROFILE_NAME, TABLE_ECM_ROW_USERNAME,
		 * TABLE_ECM_ROW_PASSWORD, TABLE_ECM_ROW_ROUTERID
		 */
		if (c != null && c.moveToFirst()) {
			profileArrayList = new ArrayList<Profile>();
			while (c.isAfterLast() == false) {
				Profile profile = new Profile();
				AuthInfo authInfo = new AuthInfo();
				profile.setProfileName(c.getString(c.getColumnIndex(TABLE_ECM_ROW_PROFILE_NAME)));
				authInfo.setUsername(c.getString(c.getColumnIndex(TABLE_ECM_ROW_USERNAME)));
				authInfo.setPassword(c.getString(c.getColumnIndex(TABLE_ECM_ROW_PASSWORD)));
				authInfo.setRouterId(c.getString(c.getColumnIndex(TABLE_ECM_ROW_ROUTERID)));
				profile.setAuthInfo(authInfo);

				profileArrayList.add(profile);
				c.moveToNext();
			}

		}

		return profileArrayList;

	}
}
