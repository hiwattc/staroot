package com.staroot.domain;

public class PageInfo {
	private String pageText;
	private int selPageNo;
	private String active;
	private int pageSize;
	
	public int getSelPageNo() {
		return selPageNo;
	}
	public void setSelPageNo(int selPageNo) {
		this.selPageNo = selPageNo;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getPageText() {
		return pageText;
	}
	public void setPageText(String pageText) {
		this.pageText = pageText;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
