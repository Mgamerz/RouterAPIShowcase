
package com.cs481.commandcenter.responses.ecm.routers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Meta{
   	private Number limit;
   	private String next;
   	private Number offset;
   	private String previous;
   	private Number total_count;

 	public Number getLimit(){
		return this.limit;
	}
	public void setLimit(Number limit){
		this.limit = limit;
	}
 	public String getNext(){
		return this.next;
	}
	public void setNext(String next){
		this.next = next;
	}
 	public Number getOffset(){
		return this.offset;
	}
	public void setOffset(Number offset){
		this.offset = offset;
	}
 	public String getPrevious(){
		return this.previous;
	}
	public void setPrevious(String previous){
		this.previous = previous;
	}
 	public Number getTotal_count(){
		return this.total_count;
	}
	public void setTotal_count(Number total_count){
		this.total_count = total_count;
	}
}
