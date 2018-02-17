package com.abhi.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abhi.report.dao.LoginDao;
import com.abhi.report.exception.ChangeBookingException;
import com.abhi.report.model.CBGraphicalForm;
import com.abhi.report.model.GrepStringForm;
import com.abhi.report.model.LoginForm;
import com.abhi.report.model.ProcessDataForm;
import com.abhi.report.model.UserSecurityQuestionForm;
import com.abhi.report.model.UserValidator;
import com.abhi.report.service.LoginService;
import com.abhi.report.service.ProcessFTCDataService;
import com.abhi.report.service.bo.FTCTransactionDetailsResponse;
import com.abhi.report.service.bo.GrepResponse;
import com.fasterxml.jackson.annotation.JsonView;

@Controller
public class ApplicationRestController {

	@Autowired
	LoginDao loginDao;

	@Autowired
	private LoginService loginService;

	@Autowired
	private ProcessFTCDataService processFTCDataService;

	@RequestMapping(value = "/checkUsername", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@JsonView
	public UserValidator validateUsername(@RequestBody String username) {
		UserValidator userValidator = new UserValidator();
		try {
			LoginForm loginForm = loginDao.retrieveLoginDetails(username);
			if (null != loginForm) {
				userValidator.setMessage("username already exist");
				userValidator.setUsernameAvailable(false);

			}
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			userValidator.setMessage("username available");
			userValidator.setUsernameAvailable(true);
		}

		return userValidator;

	}

	@RequestMapping(value = "/retrieveSecurityQuestion", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@JsonView
	public UserSecurityQuestionForm reset_validateUser(@RequestBody String username) {
		return loginService.retrieveRandomSecurityQuestion(username);
	}

	@RequestMapping(value = "/processFTC", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@JsonView
	public FTCTransactionDetailsResponse processFTCData(@ModelAttribute ProcessDataForm processDataForm) {
		FTCTransactionDetailsResponse fTCTransactionDetailsResponse=null;
		/*
		 * FTCTransactionDetailsResponse detailsResponse=new
		 * FTCTransactionDetailsResponse(); detailsResponse.setInserted(true);
		 * try { Thread.sleep(5000); } catch (InterruptedException e) {
		 * e.printStackTrace(); } return detailsResponse;
		 */
		 try {
			 fTCTransactionDetailsResponse = processFTCDataService.processFTCData(processDataForm);
		} catch (ChangeBookingException e) {
			fTCTransactionDetailsResponse=new FTCTransactionDetailsResponse();
			fTCTransactionDetailsResponse.setInserted(false);
			fTCTransactionDetailsResponse.setMessage(e.getMessage());
		}
		return fTCTransactionDetailsResponse;
	}
	@RequestMapping(value = "/populateDateForEnv", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@JsonView
	public List<String> populateDateForEnv(@RequestBody String env) {
		return processFTCDataService.retrieveProcessDate(env);
	}
	
	@RequestMapping(value = "/populateDataForgraphicalRepresentation/{env}/{date}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@JsonView
	public CBGraphicalForm populateDataForgraphicalRepresentation(@PathVariable("env") String env,@PathVariable("date") String date) {
		return processFTCDataService.calulatePnrCountForGraphicalRepresentation(env, date);
	}

}
