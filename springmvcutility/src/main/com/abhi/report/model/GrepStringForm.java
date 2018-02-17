package com.abhi.report.model;

public class GrepStringForm {

	private String userId;
	private String password;
	private String date;
	private String env;
	private String side;
	private String grepStr;
	private String logFileName;
	
	public String getLogFileName() {
		return logFileName;
	}
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getGrepStr() {
		return grepStr;
	}
	public void setGrepStr(String grepStr) {
		this.grepStr = grepStr;
	}
	
	
}
