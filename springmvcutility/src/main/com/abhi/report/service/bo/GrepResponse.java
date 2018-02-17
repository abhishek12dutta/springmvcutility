package com.abhi.report.service.bo;

import java.util.List;

public class GrepResponse {

	private boolean inserted;
	private List<String> logFile;


	public List<String> getLogFile() {
		return logFile;
	}

	public void setLogFile(List<String> logFile) {
		this.logFile = logFile;
	}

	public boolean isInserted() {
		return inserted;
	}

	public void setInserted(boolean inserted) {
		this.inserted = inserted;
	}


}
