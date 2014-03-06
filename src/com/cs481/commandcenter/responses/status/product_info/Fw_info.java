
package com.cs481.commandcenter.responses.status.product_info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Fw_info{
	@JsonProperty("build_date")
   	private String build_date;
	
	@JsonProperty("build_type")
   	private String build_type;
	
	@JsonProperty("build_version")
   	private int build_version;
	
	@JsonProperty("custom_defaults")
   	private boolean custom_defaults;
	
	@JsonProperty("fw_update_available")
   	private boolean fw_update_available;
	
	@JsonProperty("major_version")
   	private int major_version;
	
	@JsonProperty("manufacturing_upgrade")
   	private boolean manufacturing_upgrade;
	
	@JsonProperty("minor_version")
   	private int minor_version;
	
	@JsonProperty("patch_version")
   	private int patch_version;
	
	@JsonProperty("upgrade_major_version")
   	private int upgrade_major_version;
	
	@JsonProperty("upgrade_minor_version")
   	private int upgrade_minor_version;
	
	@JsonProperty("upgrade_patch_version")
   	private int upgrade_patch_version;

	/** public String getFirmware()
	 * 
	 * @return
	 */
	public String getFirmware(){
		return "" + major_version + "." + minor_version + "." + patch_version;
	}
	
 	public String getBuild_date(){
		return this.build_date;
	}
	public void setBuild_date(String build_date){
		this.build_date = build_date;
	}
 	public String getBuild_type(){
		return this.build_type;
	}
	public void setBuild_type(String build_type){
		this.build_type = build_type;
	}
 	public int getBuild_version(){
		return this.build_version;
	}
	public void setBuild_version(int build_version){
		this.build_version = build_version;
	}
 	public boolean getCustom_defaults(){
		return this.custom_defaults;
	}
	public void setCustom_defaults(boolean custom_defaults){
		this.custom_defaults = custom_defaults;
	}
 	public boolean getFw_update_available(){
		return this.fw_update_available;
	}
	public void setFw_update_available(boolean fw_update_available){
		this.fw_update_available = fw_update_available;
	}
 	public int getMajor_version(){
		return this.major_version;
	}
	public void setMajor_version(int major_version){
		this.major_version = major_version;
	}
 	public boolean getManufacturing_upgrade(){
		return this.manufacturing_upgrade;
	}
	public void setManufacturing_upgrade(boolean manufacturing_upgrade){
		this.manufacturing_upgrade = manufacturing_upgrade;
	}
 	public int getMinor_version(){
		return this.minor_version;
	}
	public void setMinor_version(int minor_version){
		this.minor_version = minor_version;
	}
 	public int getPatch_version(){
		return this.patch_version;
	}
	public void setPatch_version(int patch_version){
		this.patch_version = patch_version;
	}
 	public int getUpgrade_major_version(){
		return this.upgrade_major_version;
	}
	public void setUpgrade_major_version(int upgrade_major_version){
		this.upgrade_major_version = upgrade_major_version;
	}
 	public int getUpgrade_minor_version(){
		return this.upgrade_minor_version;
	}
	public void setUpgrade_minor_version(int upgrade_minor_version){
		this.upgrade_minor_version = upgrade_minor_version;
	}
 	public int getUpgrade_patch_version(){
		return this.upgrade_patch_version;
	}
	public void setUpgrade_patch_version(int upgrade_patch_version){
		this.upgrade_patch_version = upgrade_patch_version;
	}
}
