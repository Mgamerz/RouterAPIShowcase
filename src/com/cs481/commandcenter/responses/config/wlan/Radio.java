
package com.cs481.commandcenter.responses.config.wlan;

import java.util.List;

public class Radio{
   	private Number beacon;
   	private List bss;
   	private Number channel;
   	private String channel_selection;
   	private Number client_timeout;
   	private Number diversity;
   	private Number dtim;
   	private boolean enabled;
   	private Number extended_channel;
   	private Number fragthresh;
   	private boolean greenfield;
   	private boolean isolation;
   	private Number mcs;
   	private boolean phycoex;
   	private Number phymhz;
   	private Number phymode;
   	private Number radius_retry;
   	private Number radius_timeout;
   	private Number rtsthresh;
   	private Number rx_stream;
   	private String selection_schedule;
   	private boolean shortgi;
   	private boolean shortslot;
   	private Number tx_stream;
   	private Number txpower;
   	private Number wifi_band;
   	private boolean wimax_optimized;
   	private boolean wimax_optimized_aggressive;
   	private boolean wps_enabled;

 	public Number getBeacon(){
		return this.beacon;
	}
	public void setBeacon(Number beacon){
		this.beacon = beacon;
	}
 	public List getBss(){
		return this.bss;
	}
	public void setBss(List bss){
		this.bss = bss;
	}
 	public Number getChannel(){
		return this.channel;
	}
	public void setChannel(Number channel){
		this.channel = channel;
	}
 	public String getChannel_selection(){
		return this.channel_selection;
	}
	public void setChannel_selection(String channel_selection){
		this.channel_selection = channel_selection;
	}
 	public Number getClient_timeout(){
		return this.client_timeout;
	}
	public void setClient_timeout(Number client_timeout){
		this.client_timeout = client_timeout;
	}
 	public Number getDiversity(){
		return this.diversity;
	}
	public void setDiversity(Number diversity){
		this.diversity = diversity;
	}
 	public Number getDtim(){
		return this.dtim;
	}
	public void setDtim(Number dtim){
		this.dtim = dtim;
	}
 	public boolean getEnabled(){
		return this.enabled;
	}
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
 	public Number getExtended_channel(){
		return this.extended_channel;
	}
	public void setExtended_channel(Number extended_channel){
		this.extended_channel = extended_channel;
	}
 	public Number getFragthresh(){
		return this.fragthresh;
	}
	public void setFragthresh(Number fragthresh){
		this.fragthresh = fragthresh;
	}
 	public boolean getGreenfield(){
		return this.greenfield;
	}
	public void setGreenfield(boolean greenfield){
		this.greenfield = greenfield;
	}
 	public boolean getIsolation(){
		return this.isolation;
	}
	public void setIsolation(boolean isolation){
		this.isolation = isolation;
	}
 	public Number getMcs(){
		return this.mcs;
	}
	public void setMcs(Number mcs){
		this.mcs = mcs;
	}
 	public boolean getPhycoex(){
		return this.phycoex;
	}
	public void setPhycoex(boolean phycoex){
		this.phycoex = phycoex;
	}
 	public Number getPhymhz(){
		return this.phymhz;
	}
	public void setPhymhz(Number phymhz){
		this.phymhz = phymhz;
	}
 	public Number getPhymode(){
		return this.phymode;
	}
	public void setPhymode(Number phymode){
		this.phymode = phymode;
	}
 	public Number getRadius_retry(){
		return this.radius_retry;
	}
	public void setRadius_retry(Number radius_retry){
		this.radius_retry = radius_retry;
	}
 	public Number getRadius_timeout(){
		return this.radius_timeout;
	}
	public void setRadius_timeout(Number radius_timeout){
		this.radius_timeout = radius_timeout;
	}
 	public Number getRtsthresh(){
		return this.rtsthresh;
	}
	public void setRtsthresh(Number rtsthresh){
		this.rtsthresh = rtsthresh;
	}
 	public Number getRx_stream(){
		return this.rx_stream;
	}
	public void setRx_stream(Number rx_stream){
		this.rx_stream = rx_stream;
	}
 	public String getSelection_schedule(){
		return this.selection_schedule;
	}
	public void setSelection_schedule(String selection_schedule){
		this.selection_schedule = selection_schedule;
	}
 	public boolean getShortgi(){
		return this.shortgi;
	}
	public void setShortgi(boolean shortgi){
		this.shortgi = shortgi;
	}
 	public boolean getShortslot(){
		return this.shortslot;
	}
	public void setShortslot(boolean shortslot){
		this.shortslot = shortslot;
	}
 	public Number getTx_stream(){
		return this.tx_stream;
	}
	public void setTx_stream(Number tx_stream){
		this.tx_stream = tx_stream;
	}
 	public Number getTxpower(){
		return this.txpower;
	}
	public void setTxpower(Number txpower){
		this.txpower = txpower;
	}
 	public Number getWifi_band(){
		return this.wifi_band;
	}
	public void setWifi_band(Number wifi_band){
		this.wifi_band = wifi_band;
	}
 	public boolean getWimax_optimized(){
		return this.wimax_optimized;
	}
	public void setWimax_optimized(boolean wimax_optimized){
		this.wimax_optimized = wimax_optimized;
	}
 	public boolean getWimax_optimized_aggressive(){
		return this.wimax_optimized_aggressive;
	}
	public void setWimax_optimized_aggressive(boolean wimax_optimized_aggressive){
		this.wimax_optimized_aggressive = wimax_optimized_aggressive;
	}
 	public boolean getWps_enabled(){
		return this.wps_enabled;
	}
	public void setWps_enabled(boolean wps_enabled){
		this.wps_enabled = wps_enabled;
	}
}
