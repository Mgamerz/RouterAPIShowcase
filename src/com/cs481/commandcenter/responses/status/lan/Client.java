package com.cs481.commandcenter.responses.status.lan;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client implements Parcelable {
	public Client(){
		//empty constructor for jackson
	}
	
	@JsonProperty("ip_address")
   	private String ip_address;
	
	@JsonProperty("mac")
   	private String mac;

 	public String getIp_address(){
		return this.ip_address;
	}
	public void setIp_address(String ip_address){
		this.ip_address = ip_address;
	}
 	public String getMac(){
		return this.mac;
	}
	public void setMac(String mac){
		this.mac = mac;
	}

    public Client(Parcel in) {
        ip_address = in.readString();
        mac = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ip_address);
        dest.writeString(mac);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Client> CREATOR = new Parcelable.Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

}