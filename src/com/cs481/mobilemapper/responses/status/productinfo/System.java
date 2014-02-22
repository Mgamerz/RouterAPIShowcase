
package com.cs481.mobilemapper.responses.status.productinfo;

import java.util.List;

public class System{
   	private String boot;
   	private Number context_switches;
   	private Cpu cpu;
   	private boolean fw_upgrade_timeout;
   	private Number interrupts;
   	private Load_avg load_avg;
   	private Memory memory;
   	private Serial serial;
   	private Number time;
   	private Number uptime;
   	private Number wan_signal_strength;

 	public String getBoot(){
		return this.boot;
	}
	public void setBoot(String boot){
		this.boot = boot;
	}
 	public Number getContext_switches(){
		return this.context_switches;
	}
	public void setContext_switches(Number context_switches){
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
 	public Number getInterrupts(){
		return this.interrupts;
	}
	public void setInterrupts(Number interrupts){
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
 	public Number getTime(){
		return this.time;
	}
	public void setTime(Number time){
		this.time = time;
	}
 	public Number getUptime(){
		return this.uptime;
	}
	public void setUptime(Number uptime){
		this.uptime = uptime;
	}
 	public Number getWan_signal_strength(){
		return this.wan_signal_strength;
	}
	public void setWan_signal_strength(Number wan_signal_strength){
		this.wan_signal_strength = wan_signal_strength;
	}
}
