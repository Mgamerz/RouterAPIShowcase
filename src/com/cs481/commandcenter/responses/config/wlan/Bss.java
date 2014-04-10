package com.cs481.commandcenter.responses.config.wlan;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bss implements Parcelable {
   	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		Bss other = (Bss) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

	@JsonIgnore
	private transient String Exception;
	private String authmode;
   	private Number defaultwepkey;
   	private boolean enabled;
   	private boolean hidden;
   	private boolean isolate;
   	private String radius0ip;
   	private String radius0key;
   	private String radius0nasid;
   	private Number radius0port;
   	private String radius1ip;
   	private String ssid;
   	private String uid;
   	private String wepkey0;
   	private String wepkey1;
   	private String wepkey2;
   	private String wepkey3;
   	private boolean wmm;
   	private String wpacipher;
   	private String wpapsk;
   	private Number wparekeyinterval;
   	private Wps wps;

   	public Bss(){
   		//empty constructor for jackson
   	}
   	
 	public String getAuthmode(){
		return this.authmode;
	}
	public void setAuthmode(String authmode){
		this.authmode = authmode;
	}
 	public Number getDefaultwepkey(){
		return this.defaultwepkey;
	}
	public void setDefaultwepkey(Number defaultwepkey){
		this.defaultwepkey = defaultwepkey;
	}
 	public boolean getEnabled(){
		return this.enabled;
	}
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
 	public boolean getHidden(){
		return this.hidden;
	}
	public void setHidden(boolean hidden){
		this.hidden = hidden;
	}
 	public boolean getIsolate(){
		return this.isolate;
	}
	public void setIsolate(boolean isolate){
		this.isolate = isolate;
	}
 	public String getRadius0ip(){
		return this.radius0ip;
	}
	public void setRadius0ip(String radius0ip){
		this.radius0ip = radius0ip;
	}
 	public String getRadius0key(){
		return this.radius0key;
	}
	public void setRadius0key(String radius0key){
		this.radius0key = radius0key;
	}
 	public String getRadius0nasid(){
		return this.radius0nasid;
	}
	public void setRadius0nasid(String radius0nasid){
		this.radius0nasid = radius0nasid;
	}
 	public Number getRadius0port(){
		return this.radius0port;
	}
	public void setRadius0port(Number radius0port){
		this.radius0port = radius0port;
	}
 	public String getRadius1ip(){
		return this.radius1ip;
	}
	public void setRadius1ip(String radius1ip){
		this.radius1ip = radius1ip;
	}
 	public String getSsid(){
		return this.ssid;
	}
	public void setSsid(String ssid){
		this.ssid = ssid;
	}
 	public String getUid(){
		return this.uid;
	}
	public void setUid(String uid){
		this.uid = uid;
	}
 	public String getWepkey0(){
		return this.wepkey0;
	}
	public void setWepkey0(String wepkey0){
		this.wepkey0 = wepkey0;
	}
 	public String getWepkey1(){
		return this.wepkey1;
	}
	public void setWepkey1(String wepkey1){
		this.wepkey1 = wepkey1;
	}
 	public String getWepkey2(){
		return this.wepkey2;
	}
	public void setWepkey2(String wepkey2){
		this.wepkey2 = wepkey2;
	}
 	public String getWepkey3(){
		return this.wepkey3;
	}
	public void setWepkey3(String wepkey3){
		this.wepkey3 = wepkey3;
	}
 	public boolean getWmm(){
		return this.wmm;
	}
	public void setWmm(boolean wmm){
		this.wmm = wmm;
	}
 	public String getWpacipher(){
		return this.wpacipher;
	}
	public void setWpacipher(String wpacipher){
		this.wpacipher = wpacipher;
	}
 	public String getWpapsk(){
		return this.wpapsk;
	}
	public void setWpapsk(String wpapsk){
		this.wpapsk = wpapsk;
	}
 	public Number getWparekeyinterval(){
		return this.wparekeyinterval;
	}
	public void setWparekeyinterval(Number wparekeyinterval){
		this.wparekeyinterval = wparekeyinterval;
	}
 	public Wps getWps(){
		return this.wps;
	}
	public void setWps(Wps wps){
		this.wps = wps;
	}

    protected Bss(Parcel in) {
        authmode = in.readString();
        defaultwepkey = (Number) in.readValue(Number.class.getClassLoader());
        enabled = in.readByte() != 0x00;
        hidden = in.readByte() != 0x00;
        isolate = in.readByte() != 0x00;
        radius0ip = in.readString();
        radius0key = in.readString();
        radius0nasid = in.readString();
        radius0port = (Number) in.readValue(Number.class.getClassLoader());
        radius1ip = in.readString();
        ssid = in.readString();
        uid = in.readString();
        wepkey0 = in.readString();
        wepkey1 = in.readString();
        wepkey2 = in.readString();
        wepkey3 = in.readString();
        wmm = in.readByte() != 0x00;
        wpacipher = in.readString();
        wpapsk = in.readString();
        wparekeyinterval = (Number) in.readValue(Number.class.getClassLoader());
        wps = (Wps) in.readValue(Wps.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(Exception);
        dest.writeString(authmode);
        dest.writeValue(defaultwepkey);
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeByte((byte) (hidden ? 0x01 : 0x00));
        dest.writeByte((byte) (isolate ? 0x01 : 0x00));
        dest.writeString(radius0ip);
        dest.writeString(radius0key);
        dest.writeString(radius0nasid);
        dest.writeValue(radius0port);
        dest.writeString(radius1ip);
        dest.writeString(ssid);
        dest.writeString(uid);
        dest.writeString(wepkey0);
        dest.writeString(wepkey1);
        dest.writeString(wepkey2);
        dest.writeString(wepkey3);
        dest.writeByte((byte) (wmm ? 0x01 : 0x00));
        dest.writeString(wpacipher);
        dest.writeString(wpapsk);
        dest.writeValue(wparekeyinterval);
        dest.writeValue(wps);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bss> CREATOR = new Parcelable.Creator<Bss>() {
        @Override
        public Bss createFromParcel(Parcel in) {
            return new Bss(in);
        }

        @Override
        public Bss[] newArray(int size) {
            return new Bss[size];
        }
    };
    

	@JsonProperty("exception")
	public void setException(String exception) {
		this.Exception = exception;
	}

	@JsonIgnore
	public String getException() {
		// TODO Auto-generated method stub
		return Exception;
	}
	
	@JsonProperty("reason")
	private transient String reason;

	@JsonIgnore
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}