
package com.cs481.mobilemapper.responses.status.product_info;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Memory{
	@JsonProperty("active")
   	private int active;
	
	@JsonProperty("active(anon)")
   	private int active_anon;
	
	@JsonProperty("active(file)")
   	private int active_file;
	
	@JsonProperty("anonpages")
   	private int anonpages;
	
	@JsonProperty("bounce")
   	private int bounce;
	
	@JsonProperty("buffers")
   	private int buffers;
	
	@JsonProperty("cached")
   	private int cached;
	
	@JsonProperty("commitlimit")
   	private int commitlimit;
	
	@JsonProperty("committed_as")
   	private int committed_as;
	
	@JsonProperty("dirty")
   	private int dirty;
	
	@JsonProperty("inactive")
   	private int inactive;
	
	@JsonProperty("inactive(anon)")
   	private int inactive_anon;
	
	@JsonProperty("inactive(file)")
   	private int inactive_file;
	
	@JsonProperty("kernelstack")
   	private int kernelstack;
	
	@JsonProperty("mapped")
   	private int mapped;
	
	@JsonProperty("memfree")
   	private int memfree;
	
	@JsonProperty("memtotal")
   	private int memtotal;
	
	@JsonProperty("mlocked")
   	private int mlocked;
	
	@JsonProperty("nfs_unstable")
   	private int nfs_unstable;
	
	@JsonProperty("pagetables")
   	private int pagetables;
	
	@JsonProperty("shmem")
   	private int shmem;
	
	@JsonProperty("slab")
   	private int slab;
	
	@JsonProperty("sreclaimable")
   	private int sreclaimable;
	
	@JsonProperty("sunreclaim")
   	private int sunreclaim;
	
	@JsonProperty("swapcached")
   	private int swapcached;
	
	@JsonProperty("swapfree")
   	private int swapfree;
	
	@JsonProperty("swaptotal")
   	private int swaptotal;
	
	@JsonProperty("unevictable")
   	private int unevictable;
	
	@JsonProperty("vmallocchunk")
   	private int vmallocchunk;
	
	@JsonProperty("vmalloctotal")
   	private int vmalloctotal;
	
	@JsonProperty("vmallocused")
   	private int vmallocused;
	
	@JsonProperty("writeback")
   	private int writeback;
	
	@JsonProperty("writebacktmp")
   	private int writebacktmp;

 	public int getActive(){
		return this.active;
	}
	public void setActive(int active){
		this.active = active;
	}
 	public int getactive_anon(){
		return this.active_anon;
	}
	public void setactive_anon(int active_anon){
		this.active_anon = active_anon;
	}
 	public int getactive_file(){
		return this.active_file;
	}
	public void setactive_file(int active_file){
		this.active_file = active_file;
	}
 	public int getAnonpages(){
		return this.anonpages;
	}
	public void setAnonpages(int anonpages){
		this.anonpages = anonpages;
	}
 	public int getBounce(){
		return this.bounce;
	}
	public void setBounce(int bounce){
		this.bounce = bounce;
	}
 	public int getBuffers(){
		return this.buffers;
	}
	public void setBuffers(int buffers){
		this.buffers = buffers;
	}
 	public int getCached(){
		return this.cached;
	}
	public void setCached(int cached){
		this.cached = cached;
	}
 	public int getCommitlimit(){
		return this.commitlimit;
	}
	public void setCommitlimit(int commitlimit){
		this.commitlimit = commitlimit;
	}
 	public int getCommitted_as(){
		return this.committed_as;
	}
	public void setCommitted_as(int committed_as){
		this.committed_as = committed_as;
	}
 	public int getDirty(){
		return this.dirty;
	}
	public void setDirty(int dirty){
		this.dirty = dirty;
	}
 	public int getInactive(){
		return this.inactive;
	}
	public void setInactive(int inactive){
		this.inactive = inactive;
	}
 	public int getInactive_anon(){
		return this.inactive_anon;
	}
	public void setInactive_anon(int inactive_anon){
		this.inactive_anon = inactive_anon;
	}
 	public int getInactive_file(){
		return this.inactive_file;
	}
	public void setInactive_file(int inactive_file){
		this.inactive_file = inactive_file;
	}
 	public int getKernelstack(){
		return this.kernelstack;
	}
	public void setKernelstack(int kernelstack){
		this.kernelstack = kernelstack;
	}
 	public int getMapped(){
		return this.mapped;
	}
	public void setMapped(int mapped){
		this.mapped = mapped;
	}
 	public int getMemfree(){
		return this.memfree;
	}
	public void setMemfree(int memfree){
		this.memfree = memfree;
	}
 	public int getMemtotal(){
		return this.memtotal;
	}
	public void setMemtotal(int memtotal){
		this.memtotal = memtotal;
	}
 	public int getMlocked(){
		return this.mlocked;
	}
	public void setMlocked(int mlocked){
		this.mlocked = mlocked;
	}
 	public int getNfs_unstable(){
		return this.nfs_unstable;
	}
	public void setNfs_unstable(int nfs_unstable){
		this.nfs_unstable = nfs_unstable;
	}
 	public int getPagetables(){
		return this.pagetables;
	}
	public void setPagetables(int pagetables){
		this.pagetables = pagetables;
	}
 	public int getShmem(){
		return this.shmem;
	}
	public void setShmem(int shmem){
		this.shmem = shmem;
	}
 	public int getSlab(){
		return this.slab;
	}
	public void setSlab(int slab){
		this.slab = slab;
	}
 	public int getSreclaimable(){
		return this.sreclaimable;
	}
	public void setSreclaimable(int sreclaimable){
		this.sreclaimable = sreclaimable;
	}
 	public int getSunreclaim(){
		return this.sunreclaim;
	}
	public void setSunreclaim(int sunreclaim){
		this.sunreclaim = sunreclaim;
	}
 	public int getSwapcached(){
		return this.swapcached;
	}
	public void setSwapcached(int swapcached){
		this.swapcached = swapcached;
	}
 	public int getSwapfree(){
		return this.swapfree;
	}
	public void setSwapfree(int swapfree){
		this.swapfree = swapfree;
	}
 	public int getSwaptotal(){
		return this.swaptotal;
	}
	public void setSwaptotal(int swaptotal){
		this.swaptotal = swaptotal;
	}
 	public int getUnevictable(){
		return this.unevictable;
	}
	public void setUnevictable(int unevictable){
		this.unevictable = unevictable;
	}
 	public int getVmallocchunk(){
		return this.vmallocchunk;
	}
	public void setVmallocchunk(int vmallocchunk){
		this.vmallocchunk = vmallocchunk;
	}
 	public int getVmalloctotal(){
		return this.vmalloctotal;
	}
	public void setVmalloctotal(int vmalloctotal){
		this.vmalloctotal = vmalloctotal;
	}
 	public int getVmallocused(){
		return this.vmallocused;
	}
	public void setVmallocused(int vmallocused){
		this.vmallocused = vmallocused;
	}
 	public int getWriteback(){
		return this.writeback;
	}
	public void setWriteback(int writeback){
		this.writeback = writeback;
	}
 	public int getWritebacktmp(){
		return this.writebacktmp;
	}
	public void setWritebacktmp(int writebacktmp){
		this.writebacktmp = writebacktmp;
	}
}
