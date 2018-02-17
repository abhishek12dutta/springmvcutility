package com.abhi.report.model;

public class FTCQueueDetailsForm {

	private String ftcQueueLogDate;
	private String pnr;
	private String queuedPnrEnv;
	private String queueNumber;
	private String queueOfficeId;
	private String queuePOS;
	private String queueReason;
	private String txId;

	public String getFtcQueueLogDate() {
		return ftcQueueLogDate;
	}

	public String getPnr() {
		return pnr;
	}

	public String getQueuedPnrEnv() {
		return queuedPnrEnv;
	}

	public String getQueueNumber() {
		return queueNumber;
	}

	public String getQueueOfficeId() {
		return queueOfficeId;
	}

	public String getQueuePOS() {
		return queuePOS;
	}

	public String getQueueReason() {
		return queueReason;
	}

	public String getTxId() {
		return txId;
	}

	public void setFtcQueueLogDate(String ftcQueueLogDate) {
		this.ftcQueueLogDate = ftcQueueLogDate;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	public void setQueuedPnrEnv(String queuedPnrEnv) {
		this.queuedPnrEnv = queuedPnrEnv;
	}

	public void setQueueNumber(String queueNumber) {
		this.queueNumber = queueNumber;
	}

	public void setQueueOfficeId(String queueOfficeId) {
		this.queueOfficeId = queueOfficeId;
	}

	public void setQueuePOS(String queuePOS) {
		this.queuePOS = queuePOS;
	}

	public void setQueueReason(String queueReason) {
		this.queueReason = queueReason;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

}
