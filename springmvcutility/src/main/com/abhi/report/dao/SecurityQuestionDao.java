package com.abhi.report.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.abhi.report.model.SecurityQuestionForm;
import com.abhi.report.model.UserSecurityQuestionForm;

public interface SecurityQuestionDao {

	public List<SecurityQuestionForm> retrieveListOfSecurityQuestions();
	public List<UserSecurityQuestionForm> retrieveUserSecurityDetails(String username);
}
