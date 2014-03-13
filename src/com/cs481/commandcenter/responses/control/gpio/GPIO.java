package com.cs481.commandcenter.responses.control.gpio;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)

public class GPIO implements Parcelable {
	
	public GPIO(){
		//empty constructor.
	}
	
	@JsonIgnore
	private transient String Exception;
	
	@JsonIgnore
	public String getException() {
		return Exception;
	}

	@JsonProperty("exception")
	public void setException(String exception) {
		this.Exception = exception;
	}
	
	@JsonIgnore
	private transient String Key;
	
	@JsonIgnore
	public String getKey() {
		return Key;
	}

	@JsonProperty("key")
	public void setKey(String key) {
		Key = key;
	}

	@JsonProperty("GPIO_SS_BUTTON")
	private java.lang.Integer gpio_ss_button;

 	public void setGpio_ss_button(java.lang.Integer gpio_ss_button) {
		this.gpio_ss_button = gpio_ss_button;
	}

	public java.lang.Integer getGpio_ss_button() {
		return gpio_ss_button;
	}

	@JsonProperty("GPIO_RESET_BUTTON")
	private java.lang.Integer gpio_reset_button;

 	public void setGpio_reset_button(java.lang.Integer gpio_reset_button) {
		this.gpio_reset_button = gpio_reset_button;
	}

	public java.lang.Integer getGpio_reset_button() {
		return gpio_reset_button;
	}

	@JsonProperty("LED_POWER")
	private java.lang.Integer led_power;

 	public void setLed_power(java.lang.Integer led_power) {
		this.led_power = led_power;
	}

	public java.lang.Integer getLed_power() {
		return led_power;
	}

	@JsonProperty("CURRENT_LIMIT_FLAG_USB2")
	private java.lang.Integer current_limit_flag_usb2;

 	public void setCurrent_limit_flag_usb2(java.lang.Integer current_limit_flag_usb2) {
		this.current_limit_flag_usb2 = current_limit_flag_usb2;
	}

	public java.lang.Integer getCurrent_limit_flag_usb2() {
		return current_limit_flag_usb2;
	}

	@JsonProperty("CURRENT_LIMIT_FLAG_USB1")
	private java.lang.Integer current_limit_flag_usb1;

 	public void setCurrent_limit_flag_usb1(java.lang.Integer current_limit_flag_usb1) {
		this.current_limit_flag_usb1 = current_limit_flag_usb1;
	}

	public java.lang.Integer getCurrent_limit_flag_usb1() {
		return current_limit_flag_usb1;
	}

	@JsonProperty("CURRENT_LIMIT_FLAG_USB3")
	private java.lang.Integer current_limit_flag_usb3;

 	public void setCurrent_limit_flag_usb3(java.lang.Integer current_limit_flag_usb3) {
		this.current_limit_flag_usb3 = current_limit_flag_usb3;
	}

	public java.lang.Integer getCurrent_limit_flag_usb3() {
		return current_limit_flag_usb3;
	}

	@JsonProperty("GPIO_WIFI_ENABLE")
	private java.lang.Integer gpio_wifi_enable;

 	public void setGpio_wifi_enable(java.lang.Integer gpio_wifi_enable) {
		this.gpio_wifi_enable = gpio_wifi_enable;
	}

	public java.lang.Integer getGpio_wifi_enable() {
		return gpio_wifi_enable;
	}

	@JsonProperty("LED_USB3_R")
	private java.lang.Integer led_usb3_r;

 	public void setLed_usb3_r(java.lang.Integer led_usb3_r) {
		this.led_usb3_r = led_usb3_r;
	}

	public java.lang.Integer getLed_usb3_r() {
		return led_usb3_r;
	}

	@JsonProperty("LED_USB3_G")
	private java.lang.Integer led_usb3_g;

 	public void setLed_usb3_g(java.lang.Integer led_usb3_g) {
		this.led_usb3_g = led_usb3_g;
	}

	public java.lang.Integer getLed_usb3_g() {
		return led_usb3_g;
	}

	@JsonProperty("POWER_EN_USB3")
	private java.lang.Integer power_en_usb3;

 	public void setPower_en_usb3(java.lang.Integer power_en_usb3) {
		this.power_en_usb3 = power_en_usb3;
	}

	public java.lang.Integer getPower_en_usb3() {
		return power_en_usb3;
	}

	@JsonProperty("POWER_EN_USB2")
	private java.lang.Integer power_en_usb2;

 	public void setPower_en_usb2(java.lang.Integer power_en_usb2) {
		this.power_en_usb2 = power_en_usb2;
	}

	public java.lang.Integer getPower_en_usb2() {
		return power_en_usb2;
	}

	@JsonProperty("GPIO_WPS_BUTTON")
	private java.lang.Integer gpio_wps_button;

 	public void setGpio_wps_button(java.lang.Integer gpio_wps_button) {
		this.gpio_wps_button = gpio_wps_button;
	}

	public java.lang.Integer getGpio_wps_button() {
		return gpio_wps_button;
	}

	@JsonProperty("POWER_EN_USB1")
	private java.lang.Integer power_en_usb1;

 	public void setPower_en_usb1(java.lang.Integer power_en_usb1) {
		this.power_en_usb1 = power_en_usb1;
	}

	public java.lang.Integer getPower_en_usb1() {
		return power_en_usb1;
	}

	@JsonProperty("LED_WIFI_RED")
	private java.lang.Integer led_wifi_red;

 	public void setLed_wifi_red(java.lang.Integer led_wifi_red) {
		this.led_wifi_red = led_wifi_red;
	}

	public java.lang.Integer getLed_wifi_red() {
		return led_wifi_red;
	}

	@JsonProperty("BPF_ENABLE")
	private java.lang.Integer bpf_enable;

 	public void setBpf_enable(java.lang.Integer bpf_enable) {
		this.bpf_enable = bpf_enable;
	}

	public java.lang.Integer getBpf_enable() {
		return bpf_enable;
	}

	@JsonProperty("POWER_EN_EX1")
	private java.lang.Integer power_en_ex1;

 	public void setPower_en_ex1(java.lang.Integer power_en_ex1) {
		this.power_en_ex1 = power_en_ex1;
	}

	public java.lang.Integer getPower_en_ex1() {
		return power_en_ex1;
	}

	@JsonProperty("POWER_EN_EX2")
	private java.lang.Integer power_en_ex2;

 	public void setPower_en_ex2(java.lang.Integer power_en_ex2) {
		this.power_en_ex2 = power_en_ex2;
	}

	public java.lang.Integer getPower_en_ex2() {
		return power_en_ex2;
	}

	@JsonProperty("LED_WIFI")
	private java.lang.Integer led_wifi;

 	public void setLed_wifi(java.lang.Integer led_wifi) {
		this.led_wifi = led_wifi;
	}

	public java.lang.Integer getLed_wifi() {
		return led_wifi;
	}

	@JsonProperty("LED_EX1_G")
	private java.lang.Integer led_ex1_g;

 	public void setLed_ex1_g(java.lang.Integer led_ex1_g) {
		this.led_ex1_g = led_ex1_g;
	}

	public java.lang.Integer getLed_ex1_g() {
		return led_ex1_g;
	}

	@JsonProperty("EX1_CPUSB_RST")
	private java.lang.Integer ex1_cpusb_rst;

 	public void setEx1_cpusb_rst(java.lang.Integer ex1_cpusb_rst) {
		this.ex1_cpusb_rst = ex1_cpusb_rst;
	}

	public java.lang.Integer getEx1_cpusb_rst() {
		return ex1_cpusb_rst;
	}

	@JsonProperty("LED_WIFI_BLUE")
	private java.lang.Integer led_wifi_blue;

 	public void setLed_wifi_blue(java.lang.Integer led_wifi_blue) {
		this.led_wifi_blue = led_wifi_blue;
	}

	public java.lang.Integer getLed_wifi_blue() {
		return led_wifi_blue;
	}

	@JsonProperty("LED_USB2_G")
	private java.lang.Integer led_usb2_g;

 	public void setLed_usb2_g(java.lang.Integer led_usb2_g) {
		this.led_usb2_g = led_usb2_g;
	}

	public java.lang.Integer getLed_usb2_g() {
		return led_usb2_g;
	}

	@JsonProperty("LED_USB1_G")
	private java.lang.Integer led_usb1_g;

 	public void setLed_usb1_g(java.lang.Integer led_usb1_g) {
		this.led_usb1_g = led_usb1_g;
	}

	public java.lang.Integer getLed_usb1_g() {
		return led_usb1_g;
	}

	@JsonProperty("LED_EX1_R")
	private java.lang.Integer led_ex1_r;

 	public void setLed_ex1_r(java.lang.Integer led_ex1_r) {
		this.led_ex1_r = led_ex1_r;
	}

	public java.lang.Integer getLed_ex1_r() {
		return led_ex1_r;
	}

	@JsonProperty("LED_SS_0")
	private java.lang.Integer led_ss_0;

 	public void setLed_ss_0(java.lang.Integer led_ss_0) {
		this.led_ss_0 = led_ss_0;
	}

	public java.lang.Integer getLed_ss_0() {
		return led_ss_0;
	}

	@JsonProperty("LED_USB1_R")
	private java.lang.Integer led_usb1_r;

 	public void setLed_usb1_r(java.lang.Integer led_usb1_r) {
		this.led_usb1_r = led_usb1_r;
	}

	public java.lang.Integer getLed_usb1_r() {
		return led_usb1_r;
	}

	@JsonProperty("LED_EX2_R")
	private java.lang.Integer led_ex2_r;

 	public void setLed_ex2_r(java.lang.Integer led_ex2_r) {
		this.led_ex2_r = led_ex2_r;
	}

	public java.lang.Integer getLed_ex2_r() {
		return led_ex2_r;
	}

	@JsonProperty("LED_SS_2")
	private java.lang.Integer led_ss_2;

 	public void setLed_ss_2(java.lang.Integer led_ss_2) {
		this.led_ss_2 = led_ss_2;
	}

	public java.lang.Integer getLed_ss_2() {
		return led_ss_2;
	}

	@JsonProperty("LED_SS_1")
	private java.lang.Integer led_ss_1;

 	public void setLed_ss_1(java.lang.Integer led_ss_1) {
		this.led_ss_1 = led_ss_1;
	}

	public java.lang.Integer getLed_ss_1() {
		return led_ss_1;
	}

	@JsonProperty("BPF_DISABLE")
	private java.lang.Integer bpf_disable;

 	public void setBpf_disable(java.lang.Integer bpf_disable) {
		this.bpf_disable = bpf_disable;
	}

	public java.lang.Integer getBpf_disable() {
		return bpf_disable;
	}

	@JsonProperty("LED_USB2_R")
	private java.lang.Integer led_usb2_r;

 	public void setLed_usb2_r(java.lang.Integer led_usb2_r) {
		this.led_usb2_r = led_usb2_r;
	}

	public java.lang.Integer getLed_usb2_r() {
		return led_usb2_r;
	}

	@JsonProperty("LED_WPS")
	private java.lang.Integer led_wps;

 	public void setLed_wps(java.lang.Integer led_wps) {
		this.led_wps = led_wps;
	}

	public java.lang.Integer getLed_wps() {
		return led_wps;
	}

	@JsonProperty("CURRENT_LIMIT_FLAG_EX1")
	private java.lang.Integer current_limit_flag_ex1;

 	public void setCurrent_limit_flag_ex1(java.lang.Integer current_limit_flag_ex1) {
		this.current_limit_flag_ex1 = current_limit_flag_ex1;
	}

	public java.lang.Integer getCurrent_limit_flag_ex1() {
		return current_limit_flag_ex1;
	}

	@JsonProperty("LED_EX2_G")
	private java.lang.Integer led_ex2_g;

 	public void setLed_ex2_g(java.lang.Integer led_ex2_g) {
		this.led_ex2_g = led_ex2_g;
	}

	public java.lang.Integer getLed_ex2_g() {
		return led_ex2_g;
	}

	@JsonProperty("EX2_CPUSB_RST")
	private java.lang.Integer ex2_cpusb_rst;

 	public void setEx2_cpusb_rst(java.lang.Integer ex2_cpusb_rst) {
		this.ex2_cpusb_rst = ex2_cpusb_rst;
	}

	public java.lang.Integer getEx2_cpusb_rst() {
		return ex2_cpusb_rst;
	}

	@JsonProperty("LED_SS_3")
	private java.lang.Integer led_ss_3;

 	public void setLed_ss_3(java.lang.Integer led_ss_3) {
		this.led_ss_3 = led_ss_3;
	}

	public java.lang.Integer getLed_ss_3() {
		return led_ss_3;
	}

	@JsonProperty("CURRENT_LIMIT_FLAG_EX2")
	private java.lang.Integer current_limit_flag_ex2;

 	public void setCurrent_limit_flag_ex2(java.lang.Integer current_limit_flag_ex2) {
		this.current_limit_flag_ex2 = current_limit_flag_ex2;
	}

	public java.lang.Integer getCurrent_limit_flag_ex2() {
		return current_limit_flag_ex2;
	}


    protected GPIO(Parcel in) {
        Exception = in.readString();
        Key = in.readString();
        gpio_ss_button = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        gpio_reset_button = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_power = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        current_limit_flag_usb2 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        current_limit_flag_usb1 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        current_limit_flag_usb3 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        gpio_wifi_enable = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_usb3_r = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_usb3_g = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        power_en_usb3 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        power_en_usb2 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        gpio_wps_button = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        power_en_usb1 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_wifi_red = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        bpf_enable = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        power_en_ex1 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        power_en_ex2 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_wifi = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_ex1_g = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        ex1_cpusb_rst = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_wifi_blue = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_usb2_g = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_usb1_g = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_ex1_r = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_ss_0 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_usb1_r = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_ex2_r = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_ss_2 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_ss_1 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        bpf_disable = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_usb2_r = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_wps = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        current_limit_flag_ex1 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_ex2_g = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        ex2_cpusb_rst = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        led_ss_3 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
        current_limit_flag_ex2 = (java.lang.Integer) in.readValue(java.lang.Integer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Exception);
        dest.writeString(Key);
        dest.writeValue(gpio_ss_button);
        dest.writeValue(gpio_reset_button);
        dest.writeValue(led_power);
        dest.writeValue(current_limit_flag_usb2);
        dest.writeValue(current_limit_flag_usb1);
        dest.writeValue(current_limit_flag_usb3);
        dest.writeValue(gpio_wifi_enable);
        dest.writeValue(led_usb3_r);
        dest.writeValue(led_usb3_g);
        dest.writeValue(power_en_usb3);
        dest.writeValue(power_en_usb2);
        dest.writeValue(gpio_wps_button);
        dest.writeValue(power_en_usb1);
        dest.writeValue(led_wifi_red);
        dest.writeValue(bpf_enable);
        dest.writeValue(power_en_ex1);
        dest.writeValue(power_en_ex2);
        dest.writeValue(led_wifi);
        dest.writeValue(led_ex1_g);
        dest.writeValue(ex1_cpusb_rst);
        dest.writeValue(led_wifi_blue);
        dest.writeValue(led_usb2_g);
        dest.writeValue(led_usb1_g);
        dest.writeValue(led_ex1_r);
        dest.writeValue(led_ss_0);
        dest.writeValue(led_usb1_r);
        dest.writeValue(led_ex2_r);
        dest.writeValue(led_ss_2);
        dest.writeValue(led_ss_1);
        dest.writeValue(bpf_disable);
        dest.writeValue(led_usb2_r);
        dest.writeValue(led_wps);
        dest.writeValue(current_limit_flag_ex1);
        dest.writeValue(led_ex2_g);
        dest.writeValue(ex2_cpusb_rst);
        dest.writeValue(led_ss_3);
        dest.writeValue(current_limit_flag_ex2);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GPIO> CREATOR = new Parcelable.Creator<GPIO>() {
        @Override
        public GPIO createFromParcel(Parcel in) {
            return new GPIO(in);
        }

        @Override
        public GPIO[] newArray(int size) {
            return new GPIO[size];
        }
    };
}