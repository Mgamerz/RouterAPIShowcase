package com.cs481.mobilemapper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a convenience bundle for authentication related information. If
 * the ecm flag is set to true, classes that use this object will expect
 * username, password and routerId to not be null. If the ecm flag is set to
 * false, the routerip, username, and password must not be null.
 * 
 * @author Mgamerz
 * 
 */
public class AuthInfo implements Parcelable {
	private boolean ecm; //used to sort to table
	private boolean remote; // changes what port to use if we are doing remote
							// admin //not needed in DB

	private String routerip; //direct only
	private int routerport = 80; // default to http port 80 //direct only
	private String username = "admin"; // default
	private String password;
	private String routerId; //ecm only

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}

	public boolean isEcm() {
		return ecm;
	}

	public void setEcm(boolean ecm) {
		this.ecm = ecm;
	}

	public String getRouterip() {
		return routerip;
	}

	public void setRouterip(String routerip) {
		this.routerip = routerip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRouterport() {
		return routerport;
	}

	public void setRouterport(int routerport) {
		this.routerport = routerport;
	}

	public AuthInfo(){
		//empty constructor
	}
	
    protected AuthInfo(Parcel in) {
        ecm = in.readByte() != 0x00;
        remote = in.readByte() != 0x00;
        routerip = in.readString();
        routerport = in.readInt();
        username = in.readString();
        password = in.readString();
        routerId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (ecm ? 0x01 : 0x00));
        dest.writeByte((byte) (remote ? 0x01 : 0x00));
        dest.writeString(routerip);
        dest.writeInt(routerport);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(routerId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AuthInfo> CREATOR = new Parcelable.Creator<AuthInfo>() {
        @Override
        public AuthInfo createFromParcel(Parcel in) {
            return new AuthInfo(in);
        }

        @Override
        public AuthInfo[] newArray(int size) {
            return new AuthInfo[size];
        }
    };
}