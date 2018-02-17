package com.abhi.report.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abhi.report.model.LoginForm;
import com.abhi.report.model.SecurityQuestionForm;

public class SecurityQuestionsMapper implements RowMapper<SecurityQuestionForm> {

	public SecurityQuestionForm mapRow(ResultSet rs, int rowNum) throws SQLException {
		//create table APP.Security_Question(
				//SECURITY_QUES_ID varchar(20) not null,
				//SECURITY_QUES varchar(70) not null
				//);

		SecurityQuestionForm  securityQuestionForm = new SecurityQuestionForm();
		securityQuestionForm.setSecurityQuestionId(rs.getString("SECURITY_QUES_ID"));
		securityQuestionForm.setSecurityQuestion(rs.getString("SECURITY_QUES"));
		
		return securityQuestionForm;
	}

}
