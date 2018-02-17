package com.abhi.report.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.abhi.report.model.FTCAvailableDateEnvForm;
import com.abhi.report.model.FTCTransactionDetailsForm;
import com.abhi.report.model.ProcessDataForm;

public interface FTCTxDetailsDao {

	public void insertFTCTxDetails(List<FTCTransactionDetailsForm> forms)
			throws DataAccessException;
	public List<String> getAllFTCCommands() throws DataAccessException;
	public String getGrepCommand() throws DataAccessException;
	public boolean insertedProcessDataTable(ProcessDataForm processDataForm) throws DataAccessException;
	public List<FTCTransactionDetailsForm> retrieveFTCTxDetails(String date, String env);
	public double retrievePNRCountForBookingType(String date, String env,String bookingType);
	public double retrievePnrCountForSuccessFullBookingType(String date, String env,String bookingType);
	public double retrieveQueuedPnrCount(String date, String env,String bookingType);
	public double fetchFTCRecordCountForDateAndEnvironment(String date, String env) throws EmptyResultDataAccessException;
	public void deleteFTCData(String date, String env) throws DataAccessException;
	public List<FTCAvailableDateEnvForm> fetchProcessedFTCDates(String env) throws EmptyResultDataAccessException;;
		public double retrieveTotalQueuedPnrCount(String date, String env);
	public List<String> retrieveProcessedDate(String env);
	public double retrievePNRCount(String env,String date);
}
