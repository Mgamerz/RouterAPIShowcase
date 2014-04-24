package com.cs481.commandcenter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a convenience bundle for authentication related information. If
 * the ecm flag is set to true, classes that use this object will expect
 * username, password and routerId to not be null. If the ecm flag is set to
 * false, the routerip, username, and password must not be null.
 * 
 * @author Mike Perez
 * 
 */
public class AuthInfo implements Parcelable {
	private boolean ecm; //used to sort to table

	private String routerip; //direct only
	private int routerport = 80; // default to http port 80 //direct only
	private String username = "admin"; // default
	private String password;
	private String routerId; //ecm only
	private boolean https;

	/**
	 * Returns if this connection uses HTTPS. Only applies to direct connections.
	 * @return Connection uses HTTPS instead HTTP
	 */
	public boolean isHttps() {
		return https;
	}

	/**
	 * Sets this authinfo to use HTTPS. Only applies to direct connections.
	 * @param https true to use https, false to use http
	 */
	public void setHttps(boolean https) {
		this.https = https;
	}

	/**
	 * Sets the router port to use for connecting. Only applies to direct connections.
	 * @param routerport Router port to connect to
	 */
	public void setRouterport(int routerport) {
		this.routerport = routerport;
	}

	/**
	 * Gets the ECM router ID. These are globally unique. Only applies to ECM connections.
	 * @return ECM router ID. Only applies to ECM.
	 */
	public String getRouterId() {
		return routerId;
	}

	/**
	 * Sets teh ECM router ID. Only applies to ECM connections.
	 * @param routerId ECM router id
	 */
	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}

	/**
	 * Returns if this authinfo uses ECM or direct connections
	 * @return true if ECM, false for direct
	 */
	public boolean isEcm() {
		return ecm;
	}

	/**
	 * Sets the authinfo connection type, ECM or direct.
	 * @param ecm true to set this as 
	 */
	public void setEcm(boolean ecm) {
		this.ecm = ecm;
	}

	/**
	 * Gets the router IP that will be used when performing a direct connection.
	 * @return
	 */
	public String getRouterip() {
		return routerip;
	}

	/**
	 * Sets the router IP for direct connections. Only applies to direct connections.
	 * @param routerip Router IP to connect to
	 */
	public void setRouterip(String routerip) {
		this.routerip = routerip;
	}

	/**
	 * Gets the username to log in as. Applies to both ECM and direct connections.
	 * @return login username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the login username. Applies to both ECM and direct connections.
	 * @param username Username to login with
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password used to login
	 * @return password used for credentials
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set's the login password. Applies to both ECM and direct connections
	 * @param password Password to login with
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the router port used when connecting. Only applies to direct connections.
	 * @return Router port
	 */
	public int getRouterport() {
		return routerport;
	}

	/**
	 * Sets the port used for connecting to a router. Only applies to direct connections.
	 * @param routerport Router port to connect to
	 */
	public void setPort(int routerport) {
		this.routerport = routerport;
	}

	
	/**
	 * Empty constructor is required for parcel building. The following code is parcel generator code and is generic (hence not documented)
	 */
	public AuthInfo(){
		//empty constructor
	}
	
    protected AuthInfo(Parcel in) {
        ecm = in.readByte() != 0x00;
        routerip = in.readString();
        routerport = in.readInt();
        username = in.readString();
        password = in.readString();
        routerId = in.readString();
        https = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (ecm ? 0x01 : 0x00));
        dest.writeString(routerip);
        dest.writeInt(routerport);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(routerId);
        dest.writeByte((byte) (https ? 0x01 : 0x00));
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ecm ? 1231 : 1237);
		result = prime * result + ((routerId == null) ? 0 : routerId.hashCode());
		result = prime * result + ((routerip == null) ? 0 : routerip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthInfo other = (AuthInfo) obj;
		if (ecm != other.ecm)
			return false;
		if (routerId == null) {
			if (other.routerId != null)
				return false;
		} else if (!routerId.equals(other.routerId))
			return false;
		if (routerip == null) {
			if (other.routerip != null)
				return false;
		} else if (!routerip.equals(other.routerip))
			return false;
		return true;
	}
    
    
}