package com.abhi.report.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.abhi.report.FTC.FTCDetailsParser;
import com.abhi.report.SFTP.DownloadFileSFTP;
import com.abhi.report.constant.ApplicationConstant;
import com.abhi.report.dao.FTCTxDetailsDao;
import com.abhi.report.exception.ChangeBookingException;
import com.abhi.report.helper.ChangeBookingReportHelper;
import com.abhi.report.helper.DecimalUtils;
import com.abhi.report.model.CBGraphicalForm;
import com.abhi.report.model.FTCAvailableDateEnvForm;
import com.abhi.report.model.FTCTransactionDetailsForm;
import com.abhi.report.model.GrepStringForm;
import com.abhi.report.model.ProcessDataForm;
import com.abhi.report.service.bo.FTCTransactionDetailsResponse;
import com.abhi.report.service.bo.GrepResponse;

@Service
public class ProcessFTCDataServiceImpl implements ProcessFTCDataService {
	@Autowired
	private FTCTxDetailsDao ftcTxDetailsDao;

	@Value("${download.sftp.enabled}")
	private String isSFTPEnabled;

	@Autowired
	private FTCDetailsParser ftcDetailsParser;

	@Autowired
	private PlatformTransactionManager transactionManager;

	static SimpleDateFormat ddMMyyyy_format = new SimpleDateFormat("ddMMyyyy");
	static SimpleDateFormat dateHyphon_format = new SimpleDateFormat("yyyy-MM-dd");

	@Transactional
	public FTCTransactionDetailsResponse processFTCData(ProcessDataForm processDataForm) throws ChangeBookingException {
		FTCTransactionDetailsResponse ftcTransactionDetailsResponse = new FTCTransactionDetailsResponse();
		List<String> logList = new ArrayList<>();
		TransactionDefinition def = new DefaultTransactionDefinition(
				DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		String prDate = ChangeBookingReportHelper.formatDate(processDataForm.getDate());

		validateProcessData(processDataForm);

		if (isSFTPEnabled.equalsIgnoreCase("Y")) {
			List<String> finalCommandList = new ArrayList<>();
			String extn = "";
			String grepType = "";
			List<String> commandsList = new ArrayList<String>();
			try {
				commandsList = ftcTxDetailsDao.getAllFTCCommands();
			} catch (DataAccessException e1) {
				e1.printStackTrace();
				throw new ChangeBookingException("Sorry System is down, Please try again later");
			}
			if (isPastDate(processDataForm.getDate())) {
				extn = ".gz";
				grepType = "z";
			}

			if (commandsList.isEmpty()) {
				throw new ChangeBookingException("Sorry System is down, Please try again later");
			}
			for (String str : commandsList) {
				finalCommandList.add(String.format(str, prDate, processDataForm.getSide(), extn, grepType));
			}
			List<FutureTask<List<String>>> futureTaskList = new ArrayList<>();
			for (String command : finalCommandList) {
				futureTaskList.add(createFutureTask(command, processDataForm));
			}
			ExecutorService executor = Executors.newFixedThreadPool(4);
			System.out.println("before execute");
			for (FutureTask<List<String>> futureTask : futureTaskList) {
				executor.execute(futureTask);
			}
			System.out.println("after execute");

			for (FutureTask<List<String>> futureTask : futureTaskList) {
				try {
					List<String> strList = futureTask.get();
					if (null != strList) {
						logList.addAll(strList);
					}
				} catch (InterruptedException e) {
					executor.shutdown();
					e.printStackTrace();
				} catch (ExecutionException e) {
					executor.shutdown();
					throw new ChangeBookingException(e.getCause().getMessage());
				}
			}
			executor.shutdown();
		}
		System.out.println("final res" + logList);

		List<FTCTransactionDetailsForm> ftcTxDetailList = ftcDetailsParser.parseFTCDetails(logList, prDate,
				processDataForm.getEnv());
		try {
			ftcTxDetailsDao.insertFTCTxDetails(ftcTxDetailList);
			ftcTxDetailsDao.insertedProcessDataTable(processDataForm);
			transactionManager.commit(status);
			ftcTransactionDetailsResponse.setInserted(true);
		} catch (DataAccessException e) {
			e.printStackTrace();
			status.setRollbackOnly();
			transactionManager.rollback(status);
			ftcTransactionDetailsResponse.setMessage("Please try again..");
			ftcTransactionDetailsResponse.setInserted(false);
		}

		return ftcTransactionDetailsResponse;

	}

	private void validateProcessData(ProcessDataForm processDataForm) throws ChangeBookingException {

		double count = ftcTxDetailsDao.fetchFTCRecordCountForDateAndEnvironment(processDataForm.getDate(),
				ApplicationConstant.envMap.get(processDataForm.getEnv()));

		if (count > 0) {
			throw new ChangeBookingException("Data present for selected environment and date");
		}

	}

	private static boolean isPastDate(String inDate) {
		Date date = new Date();
		Date parsedInDate = null;
		try {
			parsedInDate = dateHyphon_format.parse(inDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diffInMillies = (date.getTime() - parsedInDate.getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (diff > 0) {
			return true;
		}
		return false;
	}

	private FutureTask<List<String>> createFutureTask(final String command, final ProcessDataForm processDataForm) {
		FutureTask<List<String>> futureTask = new FutureTask<List<String>>(new Callable<List<String>>() {
			@Override
			public List<String> call() throws ChangeBookingException {
				return new DownloadFileSFTP().retrieveLogFile(processDataForm, command);
			}
		});
		return futureTask;
	}

	

	@Override
	@Transactional
	public boolean deleteFTCData(String date, String env) {
		TransactionDefinition def = new DefaultTransactionDefinition(
				DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		boolean success = false;
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			ftcTxDetailsDao.deleteFTCData(date, env);
			transactionManager.commit(status);
			success = true;
		} catch (DataAccessException e) {
			status.setRollbackOnly();
			transactionManager.rollback(status);
		}
		return success;
	}

	@Override
	public List<FTCAvailableDateEnvForm> fetchFTCProcessDataTableList(String env) {
		return ftcTxDetailsDao.fetchProcessedFTCDates(env);
	}

	@Override
	public List<String> retrieveProcessDate(String env) {
		List<String> dateList = ftcTxDetailsDao.retrieveProcessedDate(env);
		return dateList;
	}

	@Override
	public CBGraphicalForm calulatePnrCountForGraphicalRepresentation(String env, String date) {
		CBGraphicalForm cbGraphicalForm = new CBGraphicalForm();

		double totalPnrCount = ftcTxDetailsDao.retrievePNRCount(env, date);
		double changeSuccessPNRCount = ftcTxDetailsDao.retrievePnrCountForSuccessFullBookingType(date, env, "change");
		double totalQueuedCount = ftcTxDetailsDao.retrieveTotalQueuedPnrCount(date, env);
		double upgradeSuccessPNRCount = ftcTxDetailsDao.retrievePnrCountForSuccessFullBookingType(date, env, "upgrade");
		double pnrCountForChangeAttempting = ftcTxDetailsDao.retrievePNRCountForBookingType(date, env, "change");
		double pnrCountForUpgradeAttempting = ftcTxDetailsDao.retrievePNRCountForBookingType(date, env, "upgrade");
		double failPNRCount = totalPnrCount - (changeSuccessPNRCount + upgradeSuccessPNRCount);
		double changeBookingSuccessPercentage = DecimalUtils
				.round(((changeSuccessPNRCount * 100) / pnrCountForChangeAttempting), 2);
		double upgradeBookingSuccessPercentage = DecimalUtils
				.round(((upgradeSuccessPNRCount * 100) / pnrCountForUpgradeAttempting), 2);
		double queuedPercentage = DecimalUtils.round(((totalQueuedCount * 100) / totalPnrCount), 2);

		cbGraphicalForm.setChangeBookingSuccessCount(changeSuccessPNRCount);
		cbGraphicalForm.setUpgradeBookingSuccessCount(upgradeSuccessPNRCount);
		cbGraphicalForm.setQueuedCount(totalQueuedCount);
		cbGraphicalForm.setFailedCount(failPNRCount);
		cbGraphicalForm.setChangeBookingSuccessPercentage(changeBookingSuccessPercentage);
		cbGraphicalForm.setUpgradeBookingSuccessPercentage(upgradeBookingSuccessPercentage);
		cbGraphicalForm.setQueuedPercentage(queuedPercentage);
		cbGraphicalForm.setChangeBookingAttemptingCount(pnrCountForChangeAttempting);
		cbGraphicalForm.setUpgradeBookingAttemptingCount(pnrCountForUpgradeAttempting);

		return cbGraphicalForm;
	}

	@Override
	public List<FTCTransactionDetailsForm> retrieveFTCTxDetail(String date, String env) {
		List<FTCTransactionDetailsForm> ftcTxDetailList = ftcTxDetailsDao.retrieveFTCTxDetails(date, env);
		return ftcTxDetailList;
	}

	@Override
	public GrepResponse processLogForString(GrepStringForm grepStringForm) throws ChangeBookingException {
		List<String> logFile = null;
		GrepResponse grepResponse = new GrepResponse();
		String command = "";
	
			String grepType = "";
			String extn = "";
			try {
				command = ftcTxDetailsDao.getGrepCommand();
			} catch (DataAccessException e1) {
				e1.printStackTrace();
				throw new ChangeBookingException("Sorry System is down, Please try again later");
			}
			if (grepStringForm.getLogFileName().contains("gz")) {
				grepType = "z";
			}
			 command = String.format(command,grepType, grepStringForm.getGrepStr(),
					grepStringForm.getLogFileName());
			logFile = DownloadFileSFTP.retrieveGrepStringLogFile(grepStringForm, command);
			System.out.println("final res" + logFile);
			grepResponse.setLogFile(logFile);		
		return grepResponse;
	}

}
