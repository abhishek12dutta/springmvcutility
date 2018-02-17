package com.abhi.report.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.abhi.report.constant.ApplicationConstant;
import com.abhi.report.dao.mapper.FTCAvailableDateEnvFormMapper;
import com.abhi.report.dao.mapper.FTCTxDetailMapper;
import com.abhi.report.helper.ChangeBookingReportHelper;
import com.abhi.report.model.FTCAvailableDateEnvForm;
import com.abhi.report.model.FTCQueueDetailsForm;
import com.abhi.report.model.FTCTransactionDetailsForm;
import com.abhi.report.model.ProcessDataForm;

@Repository
public class FTCTxDetailsDaoImpl implements FTCTxDetailsDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*--create table for ftc tx table--
	create table APP.FTC_Transaction_Table(
	FTC_TX_LOG_DATE varchar(10) not null,
	TX_ID varchar(70) not null,
	PNR varchar(6),
	QUEUED varchar(1),
	IS_NDC_BOOKING varchar(1),  
	TX_DATE_TIME timestamp,
	BOOKING_TYPE varchar(10),
	SUCCESS_STATUS varchar(1),
	ADD_COLLECTION_AMOUNT varchar(10),
	REFUNDED_AMOUNT varchar(10),
	CURRENCY varchar(5),
	LOG_FILE_NAME varchar(100),
	ENVIRONMENT varchar(20),
	primary key(TX_ID,PNR)
	);*/
	@Override
	public void insertFTCTxDetails(List<FTCTransactionDetailsForm> forms) throws DataAccessException {
		List<Object[]> objs = new ArrayList<>();
		int cnt = 0;

		String sql = "insert into APP.FTC_Transaction_Table values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR };
		List<FTCQueueDetailsForm> queueDetailsForms = new ArrayList<FTCQueueDetailsForm>();
		for (FTCTransactionDetailsForm detailsForms : forms) {
			Object[] obj = new Object[] { detailsForms.getFtcTxLogDate(), detailsForms.getTxId(), detailsForms.getPnr(),
					detailsForms.isQueued() ? 'Y' : 'N', detailsForms.isNdcBooking() ? 'Y' : 'N',
					detailsForms.getTxDateTime(), detailsForms.getTypeOfBooking(),
					detailsForms.isSuccessStatus() ? 'Y' : 'N', detailsForms.getAddtionalCollectionAmount(),
					detailsForms.getRefundAmount(), detailsForms.getCurrency(), detailsForms.getLogFilename(),
					detailsForms.getEnv() };
			objs.add(obj);
			if (null != detailsForms.getFtcQueueDetailsForm()) {
				queueDetailsForms.add(detailsForms.getFtcQueueDetailsForm());
			}
			if (cnt == 49) {
				jdbcTemplate.batchUpdate(sql, objs, types);
				insertFTCQueueInfoDetails(queueDetailsForms);
				queueDetailsForms = new ArrayList<>();
				objs = new ArrayList<>();
				cnt = -1;
			}
			cnt++;
		}

		if (cnt < 49) {
			jdbcTemplate.batchUpdate(sql, objs, types);
			insertFTCQueueInfoDetails(queueDetailsForms);
			objs = new ArrayList<>();
		}
	}

	/*
	 * --create table for ftc queue info table-- create table
	 * APP.FTC_Queue_Info_Table( FTC_QUEUE_LOG_DATE varchar(10) not null,
	 * QUEUED_TX_ID varchar(70) not null, QUEUED_PNR varchar(6),
	 * QUEUE_ENVIRONMENT varchar(20), QUEUE_REASON varchar(20), QUEUE_POS
	 * varchar(5), QUEUE_NUMBER varchar(7), QUEUE_OFFICE_ID varchar(10),
	 * constraint FK_FTC_Queue_Info foreign key(QUEUED_TX_ID,QUEUED_PNR)
	 * references APP.FTC_Transaction_Table(TX_ID,PNR) );
	 */

	private void insertFTCQueueInfoDetails(List<FTCQueueDetailsForm> forms) throws DataAccessException {
		List<Object[]> objs = new ArrayList<>();
		String sql = "insert into APP.FTC_Queue_Info_Table values(?,?,?,?,?,?,?,?)";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };

		for (FTCQueueDetailsForm queueDetailsForms : forms) {
			Object[] obj = new Object[] { queueDetailsForms.getFtcQueueLogDate(), queueDetailsForms.getTxId(),
					queueDetailsForms.getPnr(), queueDetailsForms.getQueuedPnrEnv(), queueDetailsForms.getQueueReason(),
					queueDetailsForms.getQueuePOS(), queueDetailsForms.getQueueNumber(),
					queueDetailsForms.getQueueOfficeId() };
			objs.add(obj);
			jdbcTemplate.batchUpdate(sql, objs, types);
			objs = new ArrayList<>();
		}
	}

	@Override
	public List<String> getAllFTCCommands() throws DataAccessException {

		String sql = "select COMMAND from APP.FTC_Command_Table where APPLICATION_NAME='FTC'";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	/*--create table for process data success:--
	PROCESS_DATA_DATE varchar(20),
	ENVIRONMENT varchar(20),
	PROCESS_DATA_INDICATOR VARCHAR(1),
	PROCESSED_BY varchar(20) not null,
	PROCESSED_DATE_TIME DATE not null,
	);*/
	public boolean insertedProcessDataTable(ProcessDataForm processDataForm) throws DataAccessException {
		boolean isDataProcessesSuccessfuly = false;
		String formattedDate = ChangeBookingReportHelper.formatDates(processDataForm.getDate());
		String sql = "insert into APP.Process_Data_Success values(?,?,?,?,?)";
		Object[] objects = new Object[] { formattedDate, ApplicationConstant.envMap.get(processDataForm.getEnv()), 'Y',
				processDataForm.getUserId(), new Date() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE };
		int rows = jdbcTemplate.update(sql, objects, types);
		if (rows > 0) {
			isDataProcessesSuccessfuly = true;
		}
		return isDataProcessesSuccessfuly;
	}

	@Override
	public List<FTCTransactionDetailsForm> retrieveFTCTxDetails(String date, String env) {

		String sql = "select f.FTC_TX_LOG_DATE,f.TX_ID,f.PNR,f.QUEUED,f.IS_NDC_BOOKING,f.TX_DATE_TIME,"
				+ " f.BOOKING_TYPE, f.SUCCESS_STATUS, f.ADD_COLLECTION_AMOUNT, f.REFUNDED_AMOUNT,"
				+ " f.CURRENCY, f.LOG_FILE_NAME, f.ENVIRONMENT, q.QUEUE_REASON,q.QUEUE_OFFICE_ID "
				+ ",q.QUEUE_POS,q.QUEUE_NUMBER from APP.FTC_Transaction_Table f left outer join"
				+ " APP.FTC_Queue_Info_Table q on f.TX_ID = q.QUEUED_TX_ID and f.PNR = q.QUEUED_PNR "
				+ "where FTC_TX_LOG_DATE=? and ENVIRONMENT=?";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		Object[] obj = new Object[] { date, env };
		List<FTCTransactionDetailsForm> ftcTxDetailList = jdbcTemplate.query(sql, obj, types, new FTCTxDetailMapper());
		return ftcTxDetailList;
	}

	/**
	 * PROCESS_DATA_DATE varchar(20), ENVIRONMENT varchar(20),
	 * PROCESS_DATA_INDICATOR VARCHAR(1),
	 */
	@Override
	public double fetchFTCRecordCountForDateAndEnvironment(String date, String env) throws EmptyResultDataAccessException {
		String sql = "select count(*) from APP.Process_Data_Success " + "where PROCESS_DATA_DATE=? and ENVIRONMENT=?";

		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		Object[] obj = new Object[] { date, env };
		int count = jdbcTemplate.queryForObject(sql, obj, types, Integer.class);
		return count;
	}

	@Override
	public void deleteFTCData(String date, String env) throws DataAccessException {
		String DELETE_SQL_FTC_QUEUE_INFO_TABLE = "delete from App.FTC_Queue_Info_Table where FTC_QUEUE_LOG_DATE=? and QUEUE_ENVIRONMENT=?";
		String DELETE_SQL_FTC_TRANSACTION_TABLE = "delete from App.FTC_Transaction_Table where FTC_TX_LOG_DATE=? and ENVIRONMENT=?";
		String DELETE_SQL_PROCESS_DATA_SUCCESS = "delete from App.Process_Data_Success where PROCESS_DATA_DATE=? and ENVIRONMENT=?";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		Object[] obj = new Object[] { date, env };
		jdbcTemplate.update(DELETE_SQL_FTC_QUEUE_INFO_TABLE, obj, types);
		jdbcTemplate.update(DELETE_SQL_FTC_TRANSACTION_TABLE, obj, types);
		jdbcTemplate.update(DELETE_SQL_PROCESS_DATA_SUCCESS, obj, types);
	}

	/*--create table for process data success:--
	PROCESS_DATA_DATE varchar(20),
	ENVIRONMENT varchar(20),
	PROCESS_DATA_INDICATOR VARCHAR(1),
	PROCESSED_BY varchar(20) not null,
	PROCESSED_DATE_TIME DATE not null,
	);*/
	@Override
	public List<FTCAvailableDateEnvForm> fetchProcessedFTCDates(String env) throws EmptyResultDataAccessException {
		String sql = "select * from App.Process_Data_Success where ENVIRONMENT=?";
		Object[] obj = new Object[] { env };
		int[] types = new int[] { Types.VARCHAR };
		return jdbcTemplate.query(sql, obj, types, new FTCAvailableDateEnvFormMapper());
	}

	@Override
	public List<String> retrieveProcessedDate(String env) {
		String sql = "select PROCESS_DATA_DATE from APP.Process_Data_Success where ENVIRONMENT=?";
		int[] types = new int[] { Types.VARCHAR };
		Object[] obj = new Object[] { env };
		List<String> dateList = jdbcTemplate.queryForList(sql, obj, types, String.class);
		return dateList;
	}

	/**/

	@Override
	public double retrievePNRCountForBookingType(String date, String env, String bookingType) {
		String sql = "select count(distinct PNR) from APP.FTC_Transaction_Table "
				+ "where FTC_TX_LOG_DATE=? and ENVIRONMENT=? and BOOKING_TYPE=?";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		Object[] obj = new Object[] { date, env, bookingType };
		double count = jdbcTemplate.queryForObject(sql, obj, types, Integer.class);

		return count;
	}

	@Override
	public double retrievePnrCountForSuccessFullBookingType(String date, String env, String bookingType) {
		String sql = "select count(distinct PNR) from APP.FTC_Transaction_Table "
				+ "where FTC_TX_LOG_DATE=? and ENVIRONMENT=? and BOOKING_TYPE=? " + "and SUCCESS_STATUS='Y'";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		Object[] obj = new Object[] { date, env, bookingType };
		double count = jdbcTemplate.queryForObject(sql, obj, types, Integer.class);
		return count;
	}

	@Override
	public double retrieveQueuedPnrCount(String date, String env, String bookingType) {
		String sql = "select count(distinct PNR) from APP.FTC_Transaction_Table "
				+ "where FTC_TX_LOG_DATE=? and ENVIRONMENT=? " + "and BOOKING_TYPE=? and QUEUED='Y'";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		Object[] obj = new Object[] { date, env, bookingType };
		double count = jdbcTemplate.queryForObject(sql, obj, types, Integer.class);
		return count;
	}

	@Override
	public double retrieveTotalQueuedPnrCount(String date, String env) {
		String sql = "select count(distinct PNR) from APP.FTC_Transaction_Table"
				+ " where FTC_TX_LOG_DATE=? and ENVIRONMENT=?" + " and QUEUED='Y'";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		Object[] obj = new Object[] { date, env };
		double count = jdbcTemplate.queryForObject(sql, obj, types, Integer.class);
		return count;
	}

	@Override
	public double retrievePNRCount(String env, String date) {
		String sql = "select count(distinct PNR) from APP.FTC_Transaction_Table where FTC_TX_LOG_DATE=? and ENVIRONMENT=?";
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		Object[] obj = new Object[] { date, env };
		double totalPnrCount = jdbcTemplate.queryForObject(sql, obj, types, Integer.class);
		return totalPnrCount;
	}

	@Override
	public String getGrepCommand() throws DataAccessException {
		String sql = "select * from APP.Grep_String_Table";
		return jdbcTemplate.queryForObject(sql, String.class);
	}
}
