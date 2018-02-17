package com.abhi.report.service;

import com.abhi.report.model.LoginForm;
import com.abhi.report.model.UserSecurityQuestionForm;
import com.abhi.report.service.bo.LoginResponse;

public interface LoginService {
	
	public LoginResponse userLogin(LoginForm loginForm);
	public UserSecurityQuestionForm retrieveRandomSecurityQuestion(String username);

}
