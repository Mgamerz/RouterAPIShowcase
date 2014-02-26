
package com.cs481.mobilemapper.responses.status.product_info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class System{
	@JsonProperty("boot")
   	private String boot;
	
	@JsonProperty("context_switches")
   	private int context_switches;
	
	@JsonProperty("cpu")
   	private Cpu cpu;
	
	@JsonProperty("fw_upgrade_timeout")
   	private boolean fw_upgrade_timeout;
	
	@JsonProperty("interrupts")
   	private int interrupts;
	
	@JsonProperty("load_avg")
   	private Load_avg load_avg;
	
	@JsonProperty("memory")
   	private Memory memory;
	
	@JsonProperty("serial")
   	private Serial serial;
	
	@JsonProperty("time")
   	private int time;
	
	@JsonProperty("uptime")
   	private double uptime;
	
	@JsonProperty("wan_signal_strength")
   	private int wan_signal_strength;
	
 	public String getBoot(){
		return this.boot;
	}
	public void setBoot(String boot){
		this.boot = boot;
	}
 	public int getContext_switches(){
		return this.context_switches;
	}
	public void setContext_switches(int context_switches){
		this.context_switches = context_switches;
	}
 	public Cpu getCpu(){
		return this.cpu;
	}
	public void setCpu(Cpu cpu){
		this.cpu = cpu;
	}
 	public boolean getFw_upgrade_timeout(){
		return this.fw_upgrade_timeout;
	}
	public void setFw_upgrade_timeout(boolean fw_upgrade_timeout){
		this.fw_upgrade_timeout = fw_upgrade_timeout;
	}
 	public int getInterrupts(){
		return this.interrupts;
	}
	public void setInterrupts(int interrupts){
		this.interrupts = interrupts;
	}
 	public Load_avg getLoad_avg(){
		return this.load_avg;
	}
	public void setLoad_avg(Load_avg load_avg){
		this.load_avg = load_avg;
	}
 	public Memory getMemory(){
		return this.memory;
	}
	public void setMemory(Memory memory){
		this.memory = memory;
	}
 	public Serial getSerial(){
		return this.serial;
	}
	public void setSerial(Serial serial){
		this.serial = serial;
	}
 	public int getTime(){
		return this.time;
	}
	public void setTime(int time){
		this.time = time;
	}
 	public double getUptime(){
		return this.uptime;
	}
	public void setUptime(double uptime){
		this.uptime = uptime;
	}
 	public int getWan_signal_strength(){
		return this.wan_signal_strength;
	}
	public void setWan_signal_strength(int wan_signal_strength){
		this.wan_signal_strength = wan_signal_strength;
	}
}
