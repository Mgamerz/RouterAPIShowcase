
package com.cs481.mobilemapper.status.wan.devices.ethernetwan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Modem{
   	private boolean aggressive_reset;
   	private String apn_mode;
   	private boolean aux_antenna_enable;
   	private String connect_mode;
   	private String functional_mode;
   	private boolean ip_src_wan_subnet_filter;
   	private String net_sel_mode;
   	private List profiles;
   	
   	@JsonProperty("ehrdp_enable")
   	private boolean ehrdp_enable;

   	@JsonProperty("ehrdp_enable")
 	public boolean getEhrdp_enable() {
		return ehrdp_enable;
	}
   	
   	@JsonProperty("ehrdp_enable")
	public void setEhrdp_enable(boolean ehrdp_enable) {
		this.ehrdp_enable = ehrdp_enable;
	}
	public boolean getAggressive_reset(){
		return this.aggressive_reset;
	}
	public void setAggressive_reset(boolean aggressive_reset){
		this.aggressive_reset = aggressive_reset;
	}
 	public String getApn_mode(){
		return this.apn_mode;
	}
	public void setApn_mode(String apn_mode){
		this.apn_mode = apn_mode;
	}
 	public boolean getAux_antenna_enable(){
		return this.aux_antenna_enable;
	}
	public void setAux_antenna_enable(boolean aux_antenna_enable){
		this.aux_antenna_enable = aux_antenna_enable;
	}
 	public String getConnect_mode(){
		return this.connect_mode;
	}
	public void setConnect_mode(String connect_mode){
		this.connect_mode = connect_mode;
	}
 	public String getFunctional_mode(){
		return this.functional_mode;
	}
	public void setFunctional_mode(String functional_mode){
		this.functional_mode = functional_mode;
	}
 	public boolean getIp_src_wan_subnet_filter(){
		return this.ip_src_wan_subnet_filter;
	}
	public void setIp_src_wan_subnet_filter(boolean ip_src_wan_subnet_filter){
		this.ip_src_wan_subnet_filter = ip_src_wan_subnet_filter;
	}
 	public String getNet_sel_mode(){
		return this.net_sel_mode;
	}
	public void setNet_sel_mode(String net_sel_mode){
		this.net_sel_mode = net_sel_mode;
	}
 	public List getProfiles(){
		return this.profiles;
	}
	public void setProfiles(List profiles){
		this.profiles = profiles;
	}
}
