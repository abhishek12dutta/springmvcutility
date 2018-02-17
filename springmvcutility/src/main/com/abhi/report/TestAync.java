package com.abhi.report;

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

import com.abhi.report.SFTP.DownloadFileSFTP;
import com.abhi.report.model.ProcessDataForm;

public class TestAync {

	static SimpleDateFormat ddMMyyyy_format = new SimpleDateFormat("ddMMyyyy");
	static SimpleDateFormat dateOblique_format = new SimpleDateFormat("dd/MM/yyyy");

	public static void main(String[] args) {
		List<String> finalRes=new ArrayList<>();
		
		ProcessDataForm processDataForm = new ProcessDataForm();
		processDataForm.setDate("09/01/2018");
		processDataForm.setEnv("c109au08");
		processDataForm.setPassword("Nove2017");
		processDataForm.setUserId("n463790");
		processDataForm.setSide("*");
		List<String> commandList = saveFTCCommands(processDataForm);
		List<FutureTask<List<String>>> futureTaskList=new ArrayList<>();
		TestAync testAync=new TestAync();
		for (String command : commandList) {
			futureTaskList.add(testAync.createFutureTask(command, processDataForm));
		}
		ExecutorService executor = Executors.newFixedThreadPool(4);
		System.out.println("before execute");
		for(FutureTask<List<String>> futureTask : futureTaskList){
			executor.execute(futureTask);
		}
		System.out.println("after execute");
		
		for(FutureTask<List<String>> futureTask : futureTaskList){
			try {
				List<String>  strList=futureTask.get();
				if(null!=strList){
					finalRes.addAll(strList);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
		System.out.println("final res"+finalRes);
		
	}

	private static List<String> saveFTCCommands(ProcessDataForm processDataForm) {
		List<String> finalCommandList = new ArrayList<>();
		List<String> commandList = new ArrayList<>();
		String extn = "";
		String grepType = "";
		String command1="find /q0/logs/%1$s/ -name 'sse-cbma1%2$s-info-*.log%3$s' -print | "
				+ "xargs %4$sgrep -H 'CHANGE_BOOKING_FTC attempting Change booking for the PNR'";
		String command2="find /q0/logs/%1$s/ -name 'sse-cbma1%2$s-info-*.log%3$s' -print | "
				+ "xargs %4$sgrep -H 'CHANGE_BOOKING_FTC is successful for the PNR'";
		String command3="find /q0/logs/%1$s/ -name 'sse-cbma1%2$s-info-*.log%3$s' -print | "
				+ "xargs %4$sgrep -H 'CHANGE_BOOKING_FTC attempting UPGRADE for the PNR'";
		String command4="find /q0/logs/%1$s/ -name 'sse-cbma1%2$s-info-*.log%3$s' -print | "
				+ "xargs %4$sgrep -H 'CHANGE_BOOKING_FTC UPGRADE successful the PNR'";
		String command5="find /q0/logs/%1$s/ -name 'sse-cbma1%2$s-info-*.log%3$s' -print | "
				+ "xargs %4$sgrep -H 'CHANGE_BOOKING_FTC Queue Office ID'";
		String command6="find /q0/logs/%1$s/ -name 'sse-cbma1%2$s-info-*.log%3$s' -print | "
				+ "xargs %4$sgrep -H 'CHANGE_BOOKING_FTC Total addition collection information'";
		String command7="find /q0/logs/%1$s/ -name 'sse-cbma1%2$s-info-*.log%3$s' -print | "
				+ "xargs %4$sgrep -H 'CHANGE_BOOKING_FTC Residual refund information'";
		String command8="find /q0/logs/%1$s/ -name 'sse-cbma1%2$s-info-*.log%3$s' -print | "
				+ "xargs %4$sgrep -H 'CHANGE_BOOKING_FTC Queue Reason'";
		commandList.add(command1);
		commandList.add(command2);
		commandList.add(command3);
		commandList.add(command4);
		commandList.add(command5);
		commandList.add(command6);
		commandList.add(command7);
		commandList.add(command8);
		if (isPastDate(processDataForm.getDate())) {
			extn = ".gz";
			grepType = "z";
		}
		for (String str : commandList) {
			finalCommandList.add(String.format(str, processDataForm.getDate().replaceAll("/", ""), processDataForm.getSide(), extn,
					grepType));
		}
		return finalCommandList;
	}

	private static boolean isPastDate(String inDate) {
		Date date = new Date();
		Date parsedInDate = null;
		try {
			parsedInDate = dateOblique_format.parse(inDate);
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
	
	
	private FutureTask<List<String>> createFutureTask(final String command,final ProcessDataForm processDataForm){
		
		
		FutureTask<List<String>> futureTask = new FutureTask<List<String>>(new Callable<List<String>>() {

			@Override
			public List<String> call() throws Exception {

				return new DownloadFileSFTP().retrieveLogFile(processDataForm, command);

			}
		});
		
		
		return futureTask;
	}

}
