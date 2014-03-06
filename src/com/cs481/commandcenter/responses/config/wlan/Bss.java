
package com.cs481.commandcenter.responses.config.wlan;

import java.util.List;

public class Bss{
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
}
