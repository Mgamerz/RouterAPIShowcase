package com.cs481.commandcenter.responses.status.wlan;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WAP implements Parcelable {
	@JsonProperty("authmode")
   	private String authmode;
	
	@JsonProperty("bssid")
   	private String bssid;
	
	@JsonProperty("channel")
   	private int channel;
	
	@JsonProperty("extcha")
   	private String extcha;
	
	@JsonProperty("mode")
   	private String mode;
	
	@JsonProperty("rssi")
   	private int  rssi;
	
	@JsonProperty("ssid")
   	private String ssid;
	
	@JsonProperty("type")
   	private String type;

 	public String getAuthmode(){
		return this.authmode;
	}
	public void setAuthmode(String authmode){
		this.authmode = authmode;
	}
 	public String getBssid(){
		return this.bssid;
	}
	public void setBssid(String bssid){
		this.bssid = bssid;
	}
 	public int getChannel(){
		return this.channel;
	}
	public void setChannel(int channel){
		this.channel = channel;
	}
 	public String getExtcha(){
		return this.extcha;
	}
	public void setExtcha(String extcha){
		this.extcha = extcha;
	}
 	public String getMode(){
		return this.mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}
 	public int getRssi(){
		return this.rssi;
	}
	public void setRssi(int rssi){
		this.rssi = rssi;
	}
 	public String getSsid(){
		return this.ssid;
	}
	public void setSsid(String ssid){
		this.ssid = ssid;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
	
	/**
	 * Get's the cipher that this WAP uses when authenticating. It is the final part of the 'authmode' variable.
	 * @return cipher string
	 */
    public String getCipher(){
    	String[] parts = authmode.split("/");
    	String cipher = parts[parts.length-1];
    	return cipher;
    }

	public WAP(){
		//empty constructor for jackson
	}
	
    protected WAP(Parcel in) {
        authmode = in.readString();
        bssid = in.readString();
        channel = in.readInt();
        extcha = in.readString();
        mode = in.readString();
        rssi = in.readInt();
        ssid = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authmode);
        dest.writeString(bssid);
        dest.writeInt(channel);
        dest.writeString(extcha);
        dest.writeString(mode);
        dest.writeInt(rssi);
        dest.writeString(ssid);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WAP> CREATOR = new Parcelable.Creator<WAP>() {
        @Override
        public WAP createFromParcel(Parcel in) {
            return new WAP(in);
        }

        @Override
        public WAP[] newArray(int size) {
            return new WAP[size];
        }
    };

    /**
     * Returns the authentication type on this network, like wpa2psk, or wep. It removes the cipher.
     * @return
     */
	public String getSecurityType() {
    	String[] parts = authmode.split("/");
    	String security = parts[0];
    	return security;
	}
    

}