package com.abhi.report.service;

import com.abhi.report.model.LoginForm;
import com.abhi.report.service.bo.UserRegistrationResponse;

public interface UserRegistrationService {

	public UserRegistrationResponse userRegistration(LoginForm loginForm);
}
