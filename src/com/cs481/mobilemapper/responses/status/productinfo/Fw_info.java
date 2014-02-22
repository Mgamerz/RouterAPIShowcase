
package com.cs481.mobilemapper.responses.status.productinfo;

import java.util.List;

public class Fw_info{
   	private String build_date;
   	private String build_type;
   	private Number build_version;
   	private boolean custom_defaults;
   	private boolean fw_update_available;
   	private Number major_version;
   	private boolean manufacturing_upgrade;
   	private Number minor_version;
   	private Number patch_version;
   	private Number upgrade_major_version;
   	private Number upgrade_minor_version;
   	private Number upgrade_patch_version;

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
 	public Number getBuild_version(){
		return this.build_version;
	}
	public void setBuild_version(Number build_version){
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
 	public Number getMajor_version(){
		return this.major_version;
	}
	public void setMajor_version(Number major_version){
		this.major_version = major_version;
	}
 	public boolean getManufacturing_upgrade(){
		return this.manufacturing_upgrade;
	}
	public void setManufacturing_upgrade(boolean manufacturing_upgrade){
		this.manufacturing_upgrade = manufacturing_upgrade;
	}
 	public Number getMinor_version(){
		return this.minor_version;
	}
	public void setMinor_version(Number minor_version){
		this.minor_version = minor_version;
	}
 	public Number getPatch_version(){
		return this.patch_version;
	}
	public void setPatch_version(Number patch_version){
		this.patch_version = patch_version;
	}
 	public Number getUpgrade_major_version(){
		return this.upgrade_major_version;
	}
	public void setUpgrade_major_version(Number upgrade_major_version){
		this.upgrade_major_version = upgrade_major_version;
	}
 	public Number getUpgrade_minor_version(){
		return this.upgrade_minor_version;
	}
	public void setUpgrade_minor_version(Number upgrade_minor_version){
		this.upgrade_minor_version = upgrade_minor_version;
	}
 	public Number getUpgrade_patch_version(){
		return this.upgrade_patch_version;
	}
	public void setUpgrade_patch_version(Number upgrade_patch_version){
		this.upgrade_patch_version = upgrade_patch_version;
	}
}
