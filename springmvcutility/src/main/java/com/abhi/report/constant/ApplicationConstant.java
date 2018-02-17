package com.abhi.report.constant;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConstant {

	
	public static Map<String, String> envMap = new HashMap<>();
	
	static {

		envMap.put("caws07-e2e01", "E2E");
		envMap.put("caws07-pint01", "PINT");
		envMap.put("c109au08", "Live");
		envMap.put("Prelive", "Pre-live");
		envMap.put("caws00-reg07", "Regression");
	}
	
	public static String CHANGE_BOOKING_FTC_ATTEMPTING_CHANGE_BOOKING_PATTERN = "^(.*?):(.*?),.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC attempting Change booking for the PNR:(.*?)]";
	public static String CHANGE_BOOKING_FTC_IS_SUCCESSFUL_PATTERN = "^.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC is successful for the PNR.*";
	public static String CHANGE_BOOKING_FTC_ATTEMPTING_UPGRADE_PATTERN = "^.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC attempting UPGRADE for the PNR.*";
	public static String CHANGE_BOOKING_FTC_UPGRADE_SUCCESSFUL_PATTERN = "^.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC UPGRADE successful the PNR.*";
	public static String CHANGE_BOOKING_FTC_QUEUE_OFFICE_ID_PATTERN = "^.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC Queue Office ID: (.*?)\\** Queue Number: (.*?)\\*.*";
	public static String CHANGE_BOOKING_FTC_TOTAL_ADDITION_COLLECTION_INFORMATION_PATTERN = "^.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC Total addition collection information: Amount:(.*?) Currency:(.*?)]";
	public static String CHANGE_BOOKING_FTC_RESIDUAL_REFUND_INFORMATION_PATTERN = "^.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC Residual refund information: Amount:(.*?) Currency:(.*?)]";
	public static String CHANGE_BOOKING_FTC_QUEUE_REASON_PATTERN = "^.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC Queue Reason: (.*?)\\,.*Queue: (.*?)]";
	public static String CHANGE_BOOKING_FTC_BOOKING_IS_A_NDC_BOOKING_PATTERN = "^.*txId=(.*?)\\|.*CHANGE_BOOKING_FTC Booking is a NDC Booking]";
	public static String GREP_STRING ="^.*log.(.*?)$";
}
