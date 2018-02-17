package com.abhi.report.service;

import com.abhi.report.model.UserSecurityQuestionForm;
import com.abhi.report.service.bo.ResetPasswordUserResponse;

public interface ResetUserPasswordService {

	public ResetPasswordUserResponse resetUserPassword(UserSecurityQuestionForm userSecurityQuestionForm);
	
}
