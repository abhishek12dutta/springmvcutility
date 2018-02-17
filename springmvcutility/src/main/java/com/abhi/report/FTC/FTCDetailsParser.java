package com.abhi.report.FTC;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.abhi.report.constant.ApplicationConstant;
import com.abhi.report.model.FTCQueueDetailsForm;
import com.abhi.report.model.FTCTransactionDetailsForm;

@Component
public class FTCDetailsParser {


	public List<FTCTransactionDetailsForm> parseFTCDetails(
			List<String> logList, String date, String env) {

		Map<String, FTCTransactionDetailsForm> map = new HashMap<>();

		if (logList.isEmpty()) {
			BufferedReader bufferedReader = null;
			String fileName = "H:\\Java_work\\Mona\\changebookingreport\\src\\main\\resources\\"
					+ date + "_FTC.txt";

			String strline = null;
			try {
				FileReader fileReader = new FileReader(fileName);
				bufferedReader = new BufferedReader(fileReader);
				while ((strline = bufferedReader.readLine()) != null) {
					logList.add(strline);
					System.out.println(strline);
				}
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		String ftcLogDate=date.substring(0, 2)+"-"+date.substring(2, 4)+"-"+date.substring(4);
		List<FTCTransactionDetailsForm> ftcTxDetailList = new ArrayList<FTCTransactionDetailsForm>();
		for (String line : logList) {
			if (line.contains("CHANGE_BOOKING_FTC attempting Change booking for the PNR:")) {
				parseFTCDetailsForChangeBookingAttempting(line, map);
			} else if (line
					.contains("CHANGE_BOOKING_FTC is successful for the PNR:")) {
				parseFTCDetailsForChangeBookingSuccessful(line, map);
			} else if (line
					.contains("CHANGE_BOOKING_FTC attempting UPGRADE for the PNR:")) {
				parseFTCDetailsForChangeBookingAttemptingUpgrade(line, map);
			} else if (line
					.contains("CHANGE_BOOKING_FTC UPGRADE successful the PNR:")) {
				parseFTCDetailsForChangeBookingUpgradeSuccessful(line, map);
			} else if (line.contains("CHANGE_BOOKING_FTC Queue Office ID:")) {
				parseFTCDetailsForChangeBookingQueueOfficeId(line, map);
			} else if (line
					.contains("CHANGE_BOOKING_FTC Total addition collection information:")) {
				parseFTCDetailsForChangeBookingTotalAddAmount(line, map);
			} else if (line
					.contains("CHANGE_BOOKING_FTC Residual refund information:")) {
				parseFTCDetailsForChangeBookingResidualInformation(line, map);
			} else if (line.contains("CHANGE_BOOKING_FTC Queue Reason:")) {
				parseFTCDetailsForChangeBookingQueueReason(line, map);
			} else if (line
					.contains("CHANGE_BOOKING_FTC Booking is a NDC Booking")) {
				parseFTCDetailsForNDCBooking(line, map);
			}

		}
		Set<Entry<String, FTCTransactionDetailsForm>> entrySet = map.entrySet();
		Iterator<Entry<String, FTCTransactionDetailsForm>> itr = entrySet
				.iterator();
		while (itr.hasNext()) {
			FTCTransactionDetailsForm detailsForm = itr.next().getValue();

			if (null == detailsForm.getPnr()
					|| detailsForm.getPnr().equalsIgnoreCase("376J3H")
					|| detailsForm.getPnr().equalsIgnoreCase("ABC")) {
				itr.remove();
			} else {
				detailsForm.setEnv(ApplicationConstant.envMap.get(env));
				detailsForm.setFtcTxLogDate(ftcLogDate);
				if (detailsForm.isQueued()) {
					detailsForm.getFtcQueueDetailsForm().setPnr(
							detailsForm.getPnr());
					detailsForm.getFtcQueueDetailsForm().setQueuedPnrEnv(ApplicationConstant.envMap.get(env));
					detailsForm.getFtcQueueDetailsForm().setFtcQueueLogDate(ftcLogDate);
				}
				ftcTxDetailList.add(detailsForm);
			}
		}
		return ftcTxDetailList;
	}

	private void parseFTCDetailsForNDCBooking(String input,
			Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_BOOKING_IS_A_NDC_BOOKING_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		if (matcher.matches()) {
			txId = matcher.group(1);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setNdcBooking(true);
		map.put(txId, ftcTransactionDetailsForm);
	}

	private static void parseFTCDetailsForChangeBookingAttempting(String input,
			Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		String logFilename = "";
		String pnr = "";
		String txDateTime = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_ATTEMPTING_CHANGE_BOOKING_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		if (matcher.matches()) {
			logFilename = matcher.group(1);
			txDateTime = matcher.group(2);
			txId = matcher.group(3);
			pnr = matcher.group(4);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}

		ftcTransactionDetailsForm.setLogFilename(logFilename);
		ftcTransactionDetailsForm.setTxDateTime(txDateTime);
		ftcTransactionDetailsForm.setPnr(pnr);
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setTypeOfBooking("change");
		if (ftcTransactionDetailsForm.getPnr() != "376J3H") {
			map.put(txId, ftcTransactionDetailsForm);
		}

	}

	private static void parseFTCDetailsForChangeBookingSuccessful(String input,
			Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_IS_SUCCESSFUL_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		if (matcher.matches()) {
			txId = matcher.group(1);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setSuccessStatus(true);
		map.put(txId, ftcTransactionDetailsForm);
	}

	private static void parseFTCDetailsForChangeBookingAttemptingUpgrade(
			String input, Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_ATTEMPTING_UPGRADE_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		if (matcher.matches()) {
			txId = matcher.group(1);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setTypeOfBooking("upgrade");
		map.put(txId, ftcTransactionDetailsForm);
	}

	private static void parseFTCDetailsForChangeBookingUpgradeSuccessful(
			String input, Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_UPGRADE_SUCCESSFUL_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		if (matcher.matches()) {
			txId = matcher.group(1);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setSuccessStatus(true);
		map.put(txId, ftcTransactionDetailsForm);
	}

	private static void parseFTCDetailsForChangeBookingQueueOfficeId(
			String input, Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		String queueOfficeId = "";
		String queueNumber = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_QUEUE_OFFICE_ID_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		FTCQueueDetailsForm ftcQueueDetailsForm = null;
		if (matcher.matches()) {
			txId = matcher.group(1);
			queueOfficeId = matcher.group(2);
			queueNumber = matcher.group(3);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
			if (null != ftcTransactionDetailsForm.getFtcQueueDetailsForm()) {
				ftcQueueDetailsForm = ftcTransactionDetailsForm
						.getFtcQueueDetailsForm();
			} else {
				ftcQueueDetailsForm = new FTCQueueDetailsForm();
			}
		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setQueued(true);
		ftcQueueDetailsForm.setQueueOfficeId(queueOfficeId);
		ftcQueueDetailsForm.setQueueNumber(queueNumber);
		ftcQueueDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setFtcQueueDetailsForm(ftcQueueDetailsForm);
		map.put(txId, ftcTransactionDetailsForm);
	}

	private static void parseFTCDetailsForChangeBookingTotalAddAmount(
			String input, Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		String amount = "";
		String currency = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_TOTAL_ADDITION_COLLECTION_INFORMATION_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		if (matcher.matches()) {
			txId = matcher.group(1);
			amount = matcher.group(2);
			currency = matcher.group(3);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setAddtionalCollectionAmount(amount);
		ftcTransactionDetailsForm.setCurrency(currency);
		map.put(txId, ftcTransactionDetailsForm);
	}

	private static void parseFTCDetailsForChangeBookingResidualInformation(
			String input, Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		String refundAmount = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_RESIDUAL_REFUND_INFORMATION_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		if (matcher.matches()) {
			txId = matcher.group(1);
			refundAmount = matcher.group(2);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setRefundAmount(refundAmount);
		map.put(txId, ftcTransactionDetailsForm);
	}

	private static void parseFTCDetailsForChangeBookingQueueReason(
			String input, Map<String, FTCTransactionDetailsForm> map) {
		String txId = "";
		String queueReason = "";
		String queuePos = "";
		Pattern pattern = Pattern
				.compile(ApplicationConstant.CHANGE_BOOKING_FTC_QUEUE_REASON_PATTERN);
		Matcher matcher = pattern.matcher(input);
		FTCTransactionDetailsForm ftcTransactionDetailsForm = null;
		FTCQueueDetailsForm ftcQueueDetailsForm = null;
		if (matcher.matches()) {
			txId = matcher.group(1);
			queueReason = matcher.group(2);
			queuePos = matcher.group(3);
		}
		if (map.containsKey(txId)) {
			ftcTransactionDetailsForm = map.get(txId);
			if (null != ftcTransactionDetailsForm.getFtcQueueDetailsForm()) {
				ftcQueueDetailsForm = ftcTransactionDetailsForm
						.getFtcQueueDetailsForm();
			} else {
				ftcQueueDetailsForm = new FTCQueueDetailsForm();
			}

		} else {
			ftcTransactionDetailsForm = new FTCTransactionDetailsForm();
		}
		ftcTransactionDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setQueued(true);
		ftcQueueDetailsForm.setQueueReason(queueReason);
		ftcQueueDetailsForm.setQueuePOS(queuePos);
		ftcQueueDetailsForm.setTxId(txId);
		ftcTransactionDetailsForm.setFtcQueueDetailsForm(ftcQueueDetailsForm);
		map.put(txId, ftcTransactionDetailsForm);
	}

}
