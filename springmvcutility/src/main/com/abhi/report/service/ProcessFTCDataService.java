package com.abhi.report.service;

import java.util.List;

import com.abhi.report.exception.ChangeBookingException;
import com.abhi.report.model.CBGraphicalForm;
import com.abhi.report.model.FTCAvailableDateEnvForm;
import com.abhi.report.model.FTCTransactionDetailsForm;
import com.abhi.report.model.GrepStringForm;
import com.abhi.report.model.ProcessDataForm;
import com.abhi.report.service.bo.FTCTransactionDetailsResponse;
import com.abhi.report.service.bo.GrepResponse;

public interface ProcessFTCDataService {
	public FTCTransactionDetailsResponse processFTCData(ProcessDataForm processDataForm) throws ChangeBookingException;

	public boolean deleteFTCData(String date, String env);

	public List<String> retrieveProcessDate(String env);

	public List<FTCAvailableDateEnvForm> fetchFTCProcessDataTableList(String env);

	public CBGraphicalForm calulatePnrCountForGraphicalRepresentation(String env, String date);

	public List<FTCTransactionDetailsForm> retrieveFTCTxDetail(String date, String env);

	public GrepResponse processLogForString(GrepStringForm grepStringForm) throws ChangeBookingException;

}
