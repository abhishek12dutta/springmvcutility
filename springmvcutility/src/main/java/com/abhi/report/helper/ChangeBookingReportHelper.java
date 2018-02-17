package com.abhi.report.helper;

public class ChangeBookingReportHelper {

	public static String formatDates(String inDate) {

		// inDate= 2018-01-09
		// outputString=09012018
		String outputString = null;
		String[] parts = inDate.split("-");
		String part1 = parts[0];
		String part2 = parts[1];
		String part3 = parts[2];
		outputString = part3 +"-"+ part2 +"-" +part1;

		return outputString;

	}
	
	public static String formatDate(String inDate) {

		// inDate= 2018-01-09
		// outputString=09012018
		String outputString = null;
		String[] parts = inDate.split("-");
		String part1 = parts[0];
		String part2 = parts[1];
		String part3 = parts[2];
		outputString = part3 + part2 + part1;

		return outputString;

	}
}
