package com.abhi.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.abhi.report.constant.ApplicationConstant;
import com.abhi.report.exception.ChangeBookingException;
import com.abhi.report.model.FTCTransactionDetailsForm;
import com.abhi.report.model.GrepStringForm;
import com.abhi.report.model.ProcessDataForm;
import com.abhi.report.service.ProcessFTCDataService;
import com.abhi.report.service.bo.GrepResponse;

@Controller
public class FTCTransactionController {

	@Autowired
	private ProcessFTCDataService processFtcDataService;

	@RequestMapping(value = "/changeBookingReportView", method = RequestMethod.POST)
	public ModelAndView showFTcTxDetails(
			@ModelAttribute("changeBookingReportForm") ProcessDataForm processDataForm,
			ModelMap modelMap) {

		String env = ApplicationConstant.envMap.get(processDataForm.getEnv());
		// String formattedDate =
		// ChangeBookingReportHelper.formatDates(processDataForm.getDate());
		List<FTCTransactionDetailsForm> ftcTransactionDetailsFormList = processFtcDataService
				.retrieveFTCTxDetail(processDataForm.getDate(), env);
		modelMap.addAttribute("ftcTransactionDetailsFormList",
				ftcTransactionDetailsFormList);
		modelMap.addAttribute("check", true);
		modelMap.addAttribute("environment", env);
		modelMap.addAttribute("date", processDataForm.getDate());
		return new ModelAndView("changeBookingReportView",
				"changeBookingReportForm", new ProcessDataForm());
	}

	@RequestMapping(value = "/viewProcessedFTCLogDate", method = RequestMethod.GET)
	public ModelAndView viewAvailableDatesForProcessedFTCLog(ModelMap modelMap) {
		modelMap.addAttribute("LivedateList",
				processFtcDataService.fetchFTCProcessDataTableList("Live"));
		return new ModelAndView("viewProcessedFTCLog");
	}

	@RequestMapping(value = "/deleteProcessData", method = RequestMethod.POST)
	public ModelAndView deleteFTCProcessedData(ModelMap modelMap,
			@ModelAttribute("envDate") String envDate) {
		String[] arry = envDate.split("\\|");
		processFtcDataService.deleteFTCData(arry[1], arry[0]);
		modelMap.addAttribute("LivedateList",
				processFtcDataService.fetchFTCProcessDataTableList("Live"));
		return new ModelAndView("viewProcessedFTCLog");
	}

	@RequestMapping(value = "/processLogForStr", method = RequestMethod.POST, produces = "application/json")
	public ModelAndView viewGrepLog(
			@ModelAttribute("grepViewForm") GrepStringForm grepStringForm,
			ModelMap modelMap) throws ChangeBookingException {
		GrepResponse grepResponse = processFtcDataService
				.processLogForString(grepStringForm);
		List<String> logFile = grepResponse.getLogFile();
		modelMap.addAttribute("logFile", logFile);
		return new ModelAndView("grepFile", "grepStringForm",
				new GrepStringForm());
	}
}
