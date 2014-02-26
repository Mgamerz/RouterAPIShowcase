
package com.cs481.mobilemapper.responses.status.product_info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product_info{
	@JsonProperty("company_name")
   	private String company_name;
	
	@JsonProperty("company_url")
   	private String company_url;
	
	@JsonProperty("contact_web_link")
   	private String contact_web_link;
	
	@JsonProperty("copyright")
   	private String copyright;
	
	@JsonProperty("default_SSID_guest")
   	private String default_SSID_guest;
	
	@JsonProperty("default_SSID_root")
   	private String default_SSID_root;
	
	@JsonProperty("mac0")
   	private String mac0;
	
	@JsonProperty("manufacturing_url")
   	private String manufacturing_url;
	
	@JsonProperty("product_name")
   	private String product_name;

 	public String getCompany_name(){
		return this.company_name;
	}
	public void setCompany_name(String company_name){
		this.company_name = company_name;
	}
 	public String getCompany_url(){
		return this.company_url;
	}
	public void setCompany_url(String company_url){
		this.company_url = company_url;
	}
 	public String getContact_web_link(){
		return this.contact_web_link;
	}
	public void setContact_web_link(String contact_web_link){
		this.contact_web_link = contact_web_link;
	}
 	public String getCopyright(){
		return this.copyright;
	}
	public void setCopyright(String copyright){
		this.copyright = copyright;
	}
 	public String getDefault_SSID_guest(){
		return this.default_SSID_guest;
	}
	public void setDefault_SSID_guest(String default_SSID_guest){
		this.default_SSID_guest = default_SSID_guest;
	}
 	public String getDefault_SSID_root(){
		return this.default_SSID_root;
	}
	public void setDefault_SSID_root(String default_SSID_root){
		this.default_SSID_root = default_SSID_root;
	}
 	public String getMac0(){
		return this.mac0;
	}
	public void setMac0(String mac0){
		this.mac0 = mac0;
	}
 	public String getManufacturing_url(){
		return this.manufacturing_url;
	}
	public void setManufacturing_url(String manufacturing_url){
		this.manufacturing_url = manufacturing_url;
	}
 	public String getProduct_name(){
		return this.product_name;
	}
	public void setProduct_name(String product_name){
		this.product_name = product_name;
	}
}
