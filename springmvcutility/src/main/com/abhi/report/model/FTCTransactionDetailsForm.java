package com.abhi.report.model;

public class FTCTransactionDetailsForm {

	private String addtionalCollectionAmount;
	private String currency;
	private String env;
	private FTCQueueDetailsForm ftcQueueDetailsForm;
	private String ftcTxLogDate;
	private String logFilename;
	private boolean ndcBooking;
	private String pnr;
	private boolean queued;
	private String refundAmount;
	private boolean successStatus;
	private String txDateTime;
	private String txId;
	private String typeOfBooking;

	public String getAddtionalCollectionAmount() {
		return addtionalCollectionAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public String getEnv() {
		return env;
	}

	public FTCQueueDetailsForm getFtcQueueDetailsForm() {
		return ftcQueueDetailsForm;
	}

	public String getFtcTxLogDate() {
		return ftcTxLogDate;
	}

	public String getLogFilename() {
		return logFilename;
	}

	public String getPnr() {
		return pnr;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public String getTxDateTime() {
		return txDateTime;
	}

	public String getTxId() {
		return txId;
	}

	public String getTypeOfBooking() {
		return typeOfBooking;
	}

	public boolean isNdcBooking() {
		return ndcBooking;
	}

	public boolean isQueued() {
		return queued;
	}

	public boolean isSuccessStatus() {
		return successStatus;
	}

	public void setAddtionalCollectionAmount(String addtionalCollectionAmount) {
		this.addtionalCollectionAmount = addtionalCollectionAmount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public void setFtcQueueDetailsForm(FTCQueueDetailsForm ftcQueueDetailsForm) {
		this.ftcQueueDetailsForm = ftcQueueDetailsForm;
	}

	public void setFtcTxLogDate(String ftcTxLogDate) {
		this.ftcTxLogDate = ftcTxLogDate;
	}

	public void setLogFilename(String logFilename) {
		this.logFilename = logFilename;
	}

	public void setNdcBooking(boolean ndcBooking) {
		this.ndcBooking = ndcBooking;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	public void setQueued(boolean queued) {
		this.queued = queued;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public void setSuccessStatus(boolean successStatus) {
		this.successStatus = successStatus;
	}

	public void setTxDateTime(String txDateTime) {
		this.txDateTime = txDateTime;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public void setTypeOfBooking(String typeOfBooking) {
		this.typeOfBooking = typeOfBooking;
	}

}
