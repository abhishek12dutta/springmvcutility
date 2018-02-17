package com.abhi.report.model;

import java.util.Date;

public class FTCAvailableDateEnvForm {
	private String ftcdate;
	private String environment;
	private String processById;
	private Date processeddateTime;


	public String getFtcdate() {
		return ftcdate;
	}

	public void setFtcdate(String ftcdate) {
		this.ftcdate = ftcdate;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getProcessById() {
		return processById;
	}

	public void setProcessById(String processById) {
		this.processById = processById;
	}

	public Date getProcesseddateTime() {
		return processeddateTime;
	}

	public void setProcesseddateTime(Date processeddateTime) {
		this.processeddateTime = processeddateTime;
	}

	
}
