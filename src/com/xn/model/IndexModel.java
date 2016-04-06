package com.xn.model;

public class IndexModel {

	private Integer id;
	private String createTime;
	private String indexDir;
	private int indexNum;
	private String spareTime;
	private String indexUser;
	private boolean Buckup;
	private String buckupDir;
	private boolean Current;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getIndexDir() {
		return indexDir;
	}
	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}
	public int getIndexNum() {
		return indexNum;
	}
	public void setIndexNum(int indexNum) {
		this.indexNum = indexNum;
	}
	public String getSpareTime() {
		return spareTime;
	}
	public void setSpareTime(String spareTime) {
		this.spareTime = spareTime;
	}
	public String getIndexUser() {
		return indexUser;
	}
	public void setIndexUser(String indexUser) {
		this.indexUser = indexUser;
	}
	public boolean isBuckup() {
		return Buckup;
	}
	public void setBuckup(boolean buckup) {
		Buckup = buckup;
	}
	public String getBuckupDir() {
		return buckupDir;
	}
	public void setBuckupDir(String buckupDir) {
		this.buckupDir = buckupDir;
	}
	public boolean isCurrent() {
		return Current;
	}
	public void setCurrent(boolean current) {
		Current = current;
	}
	
	
}
