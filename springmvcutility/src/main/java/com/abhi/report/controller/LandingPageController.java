package com.abhi.report.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.abhi.report.dao.SecurityQuestionDao;
import com.abhi.report.model.LoginForm;
import com.abhi.report.model.ProcessDataForm;
import com.abhi.report.model.SecurityQuestionForm;
import com.abhi.report.model.UserSecurityQuestionForm;
import com.abhi.report.service.LoginService;
import com.abhi.report.service.ResetUserPasswordService;
import com.abhi.report.service.UserRegistrationService;
import com.abhi.report.service.bo.LoginResponse;
import com.abhi.report.service.bo.ResetPasswordUserResponse;
import com.abhi.report.service.bo.UserRegistrationResponse;

@Controller
public class LandingPageController {

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Autowired
	private SecurityQuestionDao securityQuestionDao;

	@Autowired
	private LoginService loginService;

	@Autowired
	private ResetUserPasswordService resetUserPasswordService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		// model.addAttribute("message",
		// "Welcome to the Changebooking Application Monitor Tool");
		return "landing";

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("loginForm") LoginForm loginForm,
			ModelMap model, HttpSession httpSession) {
		LoginResponse loginResponse = loginService.userLogin(loginForm);
		httpSession.setAttribute("isUserLoggedIn", loginResponse.isSuccess());
		model.addAttribute("loginSuccess", loginResponse.isSuccess());
		model.addAttribute("loginMessage", loginResponse.getMessage());
		return new ModelAndView("landing", "loginForm", new LoginForm());

	}

	@RequestMapping(value = "/showSignUpForm", method = RequestMethod.GET)
	public ModelAndView showSignUpForm(ModelMap modelMap) {
		modelMap.addAttribute("detailsInserted", false);
		List<SecurityQuestionForm> securityQuestionsDetailList = securityQuestionDao
				.retrieveListOfSecurityQuestions();
		modelMap.addAttribute("securityQuestionsDropdownlList",
				securityQuestionsDetailList);
		return new ModelAndView("signUp", "signUpForm", new LoginForm());
	}

	@RequestMapping(value = "/userRegistration", method = RequestMethod.POST)
	public ModelAndView registerUser(
			@ModelAttribute("signUpForm") LoginForm loginForm, ModelMap modelMap) {

		if (!loginForm.getPassword().equals(loginForm.getCpassword())) {
			modelMap.addAttribute("passwrdValidationFailed", true);
			loginForm = new LoginForm();
		} else {
			UserRegistrationResponse userRegistrationResponse = userRegistrationService
					.userRegistration(loginForm);
			modelMap.addAttribute("detailsInserted",
					userRegistrationResponse.isSuccess());
		}
		return new ModelAndView("signUp", "signUpForm", loginForm);
	}

	@RequestMapping(value = "/showResetPasswordForm", method = RequestMethod.GET)
	public ModelAndView showResetpasswordForm() {
		return new ModelAndView("resetpassword", "resetPasswordForm",
				new UserSecurityQuestionForm());
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ModelAndView resetUserPassword(
			@ModelAttribute("resetPasswordForm") UserSecurityQuestionForm userSecurityQuestionForm,
			ModelMap modelMap) {

		ResetPasswordUserResponse resetPasswordUserResponse = null;
		if (userSecurityQuestionForm.getNewpassword().equals(
				userSecurityQuestionForm.getNewcpassword())) {
			resetPasswordUserResponse = resetUserPasswordService
					.resetUserPassword(userSecurityQuestionForm);
			modelMap.addAttribute("errorMsg",
					resetPasswordUserResponse.getMessage());
			modelMap.addAttribute("isPasswordUpdated",
					resetPasswordUserResponse.isSuccess());

		} else {
			modelMap.addAttribute("message", "Password mismatch!");
		}
		return new ModelAndView("resetpassword", "resetPasswordForm",
				new UserSecurityQuestionForm());
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView userLogout(HttpSession httpSession) {
		httpSession.removeAttribute("isUserLoggedIn");
		return new ModelAndView("landing", "loginForm", new LoginForm());
	}

	@RequestMapping(value = "/processdatalanding", method = RequestMethod.GET)
	public ModelAndView showProcessDataForm() {
		return new ModelAndView("processdata", "processDataForm",
				new ProcessDataForm());

	}

	@RequestMapping(value = "/changeBookingReportViewLanding", method = RequestMethod.GET)
	public String showReportViewForm() {

		return "changeBookingReportView";
	}

	@RequestMapping(value = "/grepStringLanding", method = RequestMethod.GET)
	public String showGrepStringForm() {
		return "grepFile";

	}

}
