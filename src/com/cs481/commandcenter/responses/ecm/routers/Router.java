package com.cs481.commandcenter.responses.ecm.routers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import android.os.Parcel;
import android.os.Parcelable;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Router implements Parcelable {
   	private String account;
   	private String actual_firmware;
   	private String alerts;
   	private String config_status;
   	private String configuration_manager;
   	private String create_ts;
   	private String custom1;
   	private String custom2;
   	private String desc;
   	private boolean factory_reset_pending;
   	private String full_product_name;
   	private String group;
   	private String id;
   	private String ip_address;
   	private String last_fw_activity;
   	private String locality;
   	private String logs;
   	private String mac;
   	private String name;
   	private String net_devices;
   	private String plugins;
   	private String product;
   	private boolean reboot_pending;
   	private String resource_uri;
   	private boolean session_restart_pending;
   	private String state;
   	private String state_samples;
   	private String state_ts;
   	private String stream_usage_in;
   	private String stream_usage_out;
   	private Number stream_usage_period;
   	private String stream_usage_samples;
   	private String target_firmware;
   	private String update_ts;
   	private String wireless_ap_surveys;

   	public Router(){
   		//must be here
   	}
   	
 	public String getAccount(){
		return this.account;
	}
	public void setAccount(String account){
		this.account = account;
	}
 	public String getActual_firmware(){
		return this.actual_firmware;
	}
	public void setActual_firmware(String actual_firmware){
		this.actual_firmware = actual_firmware;
	}
 	public String getAlerts(){
		return this.alerts;
	}
	public void setAlerts(String alerts){
		this.alerts = alerts;
	}
 	public String getConfig_status(){
		return this.config_status;
	}
	public void setConfig_status(String config_status){
		this.config_status = config_status;
	}
 	public String getConfiguration_manager(){
		return this.configuration_manager;
	}
	public void setConfiguration_manager(String configuration_manager){
		this.configuration_manager = configuration_manager;
	}
 	public String getCreate_ts(){
		return this.create_ts;
	}
	public void setCreate_ts(String create_ts){
		this.create_ts = create_ts;
	}
 	public String getCustom1(){
		return this.custom1;
	}
	public void setCustom1(String custom1){
		this.custom1 = custom1;
	}
 	public String getCustom2(){
		return this.custom2;
	}
	public void setCustom2(String custom2){
		this.custom2 = custom2;
	}
 	public String getDesc(){
		return this.desc;
	}
	public void setDesc(String desc){
		this.desc = desc;
	}
 	public boolean getFactory_reset_pending(){
		return this.factory_reset_pending;
	}
	public void setFactory_reset_pending(boolean factory_reset_pending){
		this.factory_reset_pending = factory_reset_pending;
	}
 	public String getFull_product_name(){
		return this.full_product_name;
	}
	public void setFull_product_name(String full_product_name){
		this.full_product_name = full_product_name;
	}
 	public String getGroup(){
		return this.group;
	}
	public void setGroup(String group){
		this.group = group;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getIp_address(){
		return this.ip_address;
	}
	public void setIp_address(String ip_address){
		this.ip_address = ip_address;
	}
 	public String getLast_fw_activity(){
		return this.last_fw_activity;
	}
	public void setLast_fw_activity(String last_fw_activity){
		this.last_fw_activity = last_fw_activity;
	}
 	public String getLocality(){
		return this.locality;
	}
	public void setLocality(String locality){
		this.locality = locality;
	}
 	public String getLogs(){
		return this.logs;
	}
	public void setLogs(String logs){
		this.logs = logs;
	}
 	public String getMac(){
		return this.mac;
	}
	public void setMac(String mac){
		this.mac = mac;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public String getNet_devices(){
		return this.net_devices;
	}
	public void setNet_devices(String net_devices){
		this.net_devices = net_devices;
	}
 	public String getPlugins(){
		return this.plugins;
	}
	public void setPlugins(String plugins){
		this.plugins = plugins;
	}
 	public String getProduct(){
		return this.product;
	}
	public void setProduct(String product){
		this.product = product;
	}
 	public boolean getReboot_pending(){
		return this.reboot_pending;
	}
	public void setReboot_pending(boolean reboot_pending){
		this.reboot_pending = reboot_pending;
	}
 	public String getResource_uri(){
		return this.resource_uri;
	}
	public void setResource_uri(String resource_uri){
		this.resource_uri = resource_uri;
	}
 	public boolean getSession_restart_pending(){
		return this.session_restart_pending;
	}
	public void setSession_restart_pending(boolean session_restart_pending){
		this.session_restart_pending = session_restart_pending;
	}
 	public String getState(){
		return this.state;
	}
	public void setState(String state){
		this.state = state;
	}
 	public String getState_samples(){
		return this.state_samples;
	}
	public void setState_samples(String state_samples){
		this.state_samples = state_samples;
	}
 	public String getState_ts(){
		return this.state_ts;
	}
	public void setState_ts(String state_ts){
		this.state_ts = state_ts;
	}
 	public String getStream_usage_in(){
		return this.stream_usage_in;
	}
	public void setStream_usage_in(String stream_usage_in){
		this.stream_usage_in = stream_usage_in;
	}
 	public String getStream_usage_out(){
		return this.stream_usage_out;
	}
	public void setStream_usage_out(String stream_usage_out){
		this.stream_usage_out = stream_usage_out;
	}
 	public Number getStream_usage_period(){
		return this.stream_usage_period;
	}
	public void setStream_usage_period(Number stream_usage_period){
		this.stream_usage_period = stream_usage_period;
	}
 	public String getStream_usage_samples(){
		return this.stream_usage_samples;
	}
	public void setStream_usage_samples(String stream_usage_samples){
		this.stream_usage_samples = stream_usage_samples;
	}
 	public String getTarget_firmware(){
		return this.target_firmware;
	}
	public void setTarget_firmware(String target_firmware){
		this.target_firmware = target_firmware;
	}
 	public String getUpdate_ts(){
		return this.update_ts;
	}
	public void setUpdate_ts(String update_ts){
		this.update_ts = update_ts;
	}
 	public String getWireless_ap_surveys(){
		return this.wireless_ap_surveys;
	}
	public void setWireless_ap_surveys(String wireless_ap_surveys){
		this.wireless_ap_surveys = wireless_ap_surveys;
	}

    protected Router(Parcel in) {
        account = in.readString();
        actual_firmware = in.readString();
        alerts = in.readString();
        config_status = in.readString();
        configuration_manager = in.readString();
        create_ts = in.readString();
        custom1 = in.readString();
        custom2 = in.readString();
        desc = in.readString();
        factory_reset_pending = in.readByte() != 0x00;
        full_product_name = in.readString();
        group = in.readString();
        id = in.readString();
        ip_address = in.readString();
        last_fw_activity = in.readString();
        locality = in.readString();
        logs = in.readString();
        mac = in.readString();
        name = in.readString();
        net_devices = in.readString();
        plugins = in.readString();
        product = in.readString();
        reboot_pending = in.readByte() != 0x00;
        resource_uri = in.readString();
        session_restart_pending = in.readByte() != 0x00;
        state = in.readString();
        state_samples = in.readString();
        state_ts = in.readString();
        stream_usage_in = in.readString();
        stream_usage_out = in.readString();
        stream_usage_period = (Number) in.readValue(Number.class.getClassLoader());
        stream_usage_samples = in.readString();
        target_firmware = in.readString();
        update_ts = in.readString();
        wireless_ap_surveys = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account);
        dest.writeString(actual_firmware);
        dest.writeString(alerts);
        dest.writeString(config_status);
        dest.writeString(configuration_manager);
        dest.writeString(create_ts);
        dest.writeString(custom1);
        dest.writeString(custom2);
        dest.writeString(desc);
        dest.writeByte((byte) (factory_reset_pending ? 0x01 : 0x00));
        dest.writeString(full_product_name);
        dest.writeString(group);
        dest.writeString(id);
        dest.writeString(ip_address);
        dest.writeString(last_fw_activity);
        dest.writeString(locality);
        dest.writeString(logs);
        dest.writeString(mac);
        dest.writeString(name);
        dest.writeString(net_devices);
        dest.writeString(plugins);
        dest.writeString(product);
        dest.writeByte((byte) (reboot_pending ? 0x01 : 0x00));
        dest.writeString(resource_uri);
        dest.writeByte((byte) (session_restart_pending ? 0x01 : 0x00));
        dest.writeString(state);
        dest.writeString(state_samples);
        dest.writeString(state_ts);
        dest.writeString(stream_usage_in);
        dest.writeString(stream_usage_out);
        dest.writeValue(stream_usage_period);
        dest.writeString(stream_usage_samples);
        dest.writeString(target_firmware);
        dest.writeString(update_ts);
        dest.writeString(wireless_ap_surveys);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Router> CREATOR = new Parcelable.Creator<Router>() {
        @Override
        public Router createFromParcel(Parcel in) {
            return new Router(in);
        }

        @Override
        public Router[] newArray(int size) {
            return new Router[size];
        }
    };
}