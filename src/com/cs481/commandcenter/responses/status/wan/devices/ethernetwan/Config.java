
package com.cs481.commandcenter.responses.status.wan.devices.ethernetwan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config{
	@JsonProperty("bandwidth_egress")
   	private int bandwidth_egress;
	
	@JsonProperty("bandwidth_ingress")
   	private int bandwidth_ingress;
	
	@JsonProperty("disabled")
   	private boolean disabled;
	
	@JsonProperty("dns6")
   	private List dns6;
	
	@JsonProperty("failback")
   	private Failback failback;
	
	@JsonProperty("failureCheck")
   	private FailureCheck failureCheck;
	
	@JsonProperty("failureCheck6")
   	private FailureCheck6 failureCheck6;
	
	@JsonProperty("hostname")
   	private String hostname;
	
	@JsonProperty("ip_mode")
   	private String ip_mode;
	
	@JsonProperty("ip_override")
   	private Ip_override ip_override;
	
	@JsonProperty("loadbalance")
   	private boolean loadbalance;
	
	@JsonProperty("modem")
   	private Modem modem;
	
	@JsonProperty("ondemand")
   	private boolean ondemand;
	
	@JsonProperty("ondemand_start_connected")
	private boolean ondemand_start_connected;
	
	@JsonProperty("ondemand_idle_minutes")
   	private int ondemand_idle_minutes;
	
	@JsonProperty("pd6")
   	private List pd6;
	
	@JsonProperty("priority")
   	private int priority;
	
	@JsonProperty("rule_indexes")
   	private List rule_indexes;
	
	@JsonProperty("static")
   	private Static staticObj;

 	public int getBandwidth_egress(){
		return this.bandwidth_egress;
	}
	public void setBandwidth_egress(int bandwidth_egress){
		this.bandwidth_egress = bandwidth_egress;
	}
 	public int getBandwidth_ingress(){
		return this.bandwidth_ingress;
	}
	public void setBandwidth_ingress(int bandwidth_ingress){
		this.bandwidth_ingress = bandwidth_ingress;
	}
 	public boolean getDisabled(){
		return this.disabled;
	}
	public void setDisabled(boolean disabled){
		this.disabled = disabled;
	}
 	public List getDns6(){
		return this.dns6;
	}
	public void setDns6(List dns6){
		this.dns6 = dns6;
	}
 	public Failback getFailback(){
		return this.failback;
	}
	public void setFailback(Failback failback){
		this.failback = failback;
	}
 	public FailureCheck getFailureCheck(){
		return this.failureCheck;
	}
	public void setFailureCheck(FailureCheck failureCheck){
		this.failureCheck = failureCheck;
	}
 	public FailureCheck6 getFailureCheck6(){
		return this.failureCheck6;
	}
	public void setFailureCheck6(FailureCheck6 failureCheck6){
		this.failureCheck6 = failureCheck6;
	}
 	public String getHostname(){
		return this.hostname;
	}
	public void setHostname(String hostname){
		this.hostname = hostname;
	}
 	public String getIp_mode(){
		return this.ip_mode;
	}
	public void setIp_mode(String ip_mode){
		this.ip_mode = ip_mode;
	}
 	public Ip_override getIp_override(){
		return this.ip_override;
	}
	public void setIp_override(Ip_override ip_override){
		this.ip_override = ip_override;
	}
 	public boolean getLoadbalance(){
		return this.loadbalance;
	}
	public void setLoadbalance(boolean loadbalance){
		this.loadbalance = loadbalance;
	}
 	public Modem getModem(){
		return this.modem;
	}
	public void setModem(Modem modem){
		this.modem = modem;
	}
 	public boolean getOndemand(){
		return this.ondemand;
	}
	public void setOndemand(boolean ondemand){
		this.ondemand = ondemand;
	}
 	public int getOndemand_idle_minutes(){
		return this.ondemand_idle_minutes;
	}
	public void setOndemand_idle_minutes(int ondemand_idle_minutes){
		this.ondemand_idle_minutes = ondemand_idle_minutes;
	}
 	public List getPd6(){
		return this.pd6;
	}
	public void setPd6(List pd6){
		this.pd6 = pd6;
	}
 	public int getPriority(){
		return this.priority;
	}
	public void setPriority(int priority){
		this.priority = priority;
	}
 	public List getRule_indexes(){
		return this.rule_indexes;
	}
	public void setRule_indexes(List rule_indexes){
		this.rule_indexes = rule_indexes;
	}
 	public Static getStatic(){
		return this.staticObj;
	}
	public void setStatic(Static staticObj){
		this.staticObj = staticObj;
	}
	
	public boolean isOndemand_start_connected() {
		return ondemand_start_connected;
	}
	public void setOndemand_start_connected(boolean ondemand_start_connected) {
		this.ondemand_start_connected = ondemand_start_connected;
	}
}
