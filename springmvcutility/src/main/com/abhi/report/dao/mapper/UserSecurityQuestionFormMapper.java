package com.abhi.report.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abhi.report.model.UserSecurityQuestionForm;

public class UserSecurityQuestionFormMapper implements RowMapper<UserSecurityQuestionForm> {

	public UserSecurityQuestionForm mapRow(ResultSet rs, int rowNum) throws SQLException {
		/*create table APP.User_Security_Question(
				USER_NAME varchar(20) not null,
				SECURITY_QUES_ID varchar(20) not null, 
				SECURITY_QUES_ANSWER varchar(20) not null, primary key(USER_NAME,SECURITY_QUES_ID), 
				constraint FK_UserName foreign key (USER_NAME) references APP.Login_User(USER_NAME),
				constraint FK_SecurityQuestionId foreign key (SECURITY_QUES_ID) references APP.Security_Question(SECURITY_QUES_ID)
				);*/
		UserSecurityQuestionForm userSecurityQuestionForm=new UserSecurityQuestionForm();
		userSecurityQuestionForm.setReset_username(rs.getString("USER_NAME"));
		userSecurityQuestionForm.setSecurity_question(rs.getString("SECURITY_QUES"));
		userSecurityQuestionForm.setSecurity_answer(rs.getString("SECURITY_QUES_ANSWER"));
		userSecurityQuestionForm.setSecurity_question_Id(rs.getString("SECURITY_QUES_ID"));
		
		return userSecurityQuestionForm;
	}

}
