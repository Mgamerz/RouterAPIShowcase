
package com.cs481.mobilemapper.responses.control.gpio;


public class GPIO{
   	private Data data;
   	private boolean success;

 	public Data getData(){
		return this.data;
	}
	public void setData(Data data){
		this.data = data;
	}
 	public boolean getSuccess(){
		return this.success;
	}
	public void setSuccess(boolean success){
		this.success = success;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if (data != null) {
		sb.append("LED EX1_G: "+data.getLED_EX1_G());
		sb.append("\n");
		sb.append("LED EX1_R: "+data.getLED_EX1_R());
		sb.append("\n");
		sb.append("LED EX2_G: "+data.getLED_EX2_G());
		sb.append("\n");
		sb.append("LED EX2_R: "+data.getLED_EX2_R());
		sb.append("\n");
		sb.append("LED POWER: "+data.getLED_POWER());
		sb.append("\n");
		sb.append("LED SS_0: "+data.getLED_SS_0());
		sb.append("\n");
		sb.append("LED SS_1: "+data.getLED_SS_1());
		sb.append("\n");
		sb.append("LED SS_2: "+data.getLED_SS_2());
		}
		
		sb.append("Command status: "+success);
	   	
		/*lED_SS_3;
	   	lED_USB1_G;
	   	lED_USB1_R;
	   	lED_USB2_G;
	   	lED_USB2_R;
	   	lED_USB3_G;
	   	lED_USB3_R;
	   	lED_WIFI;
	   	lED_WIFI_BLUE;
	   	lED_WIFI_RED;
	   	lED_WPS;
	   	pOWER_EN_EX1;
	   	pOWER_EN_EX2;
	   	pOWER_EN_USB1;
	   	pOWER_EN_USB2;
	   	pOWER_EN_USB3;*/
		return sb.toString();
	}
}
