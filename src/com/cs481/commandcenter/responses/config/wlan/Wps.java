package com.cs481.commandcenter.responses.config.wlan;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Wps implements Parcelable {
   	private Number mode;
   	private boolean pbc;
   	private String pin;
   	private Number state;

   	public Wps(){
   		//empty constructor for jackson
   	}
   	
 	public Number getMode(){
		return this.mode;
	}
	public void setMode(Number mode){
		this.mode = mode;
	}
 	public boolean getPbc(){
		return this.pbc;
	}
	public void setPbc(boolean pbc){
		this.pbc = pbc;
	}
 	public String getPin(){
		return this.pin;
	}
	public void setPin(String pin){
		this.pin = pin;
	}
 	public Number getState(){
		return this.state;
	}
	public void setState(Number state){
		this.state = state;
	}

    protected Wps(Parcel in) {
        mode = (Number) in.readValue(Number.class.getClassLoader());
        pbc = in.readByte() != 0x00;
        pin = in.readString();
        state = (Number) in.readValue(Number.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mode);
        dest.writeByte((byte) (pbc ? 0x01 : 0x00));
        dest.writeString(pin);
        dest.writeValue(state);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Wps> CREATOR = new Parcelable.Creator<Wps>() {
        @Override
        public Wps createFromParcel(Parcel in) {
            return new Wps(in);
        }

        @Override
        public Wps[] newArray(int size) {
            return new Wps[size];
        }
    };
}