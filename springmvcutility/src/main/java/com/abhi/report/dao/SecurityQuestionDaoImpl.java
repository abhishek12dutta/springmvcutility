package com.abhi.report.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.abhi.report.dao.mapper.SecurityQuestionsMapper;
import com.abhi.report.dao.mapper.UserSecurityQuestionFormMapper;
import com.abhi.report.model.SecurityQuestionForm;
import com.abhi.report.model.UserSecurityQuestionForm;

@Repository
public class SecurityQuestionDaoImpl implements SecurityQuestionDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public List<SecurityQuestionForm> retrieveListOfSecurityQuestions() {
		//create table APP.Security_Question(
		//SECURITY_QUES_ID varchar(20) not null,
		//SECURITY_QUES varchar(70) not null
		//);
		String sql="Select * from APP.Security_Question";
		List<SecurityQuestionForm> securityQuestionFormList=jdbcTemplate.query(sql, new SecurityQuestionsMapper());
		
		return securityQuestionFormList;
	}

	@Override
	public List<UserSecurityQuestionForm> retrieveUserSecurityDetails(String username){
		
		String sql="select usq.USER_NAME,usq.SECURITY_QUES_ID,usq.SECURITY_QUES_ANSWER,sq.SECURITY_QUES"
				+ " from APP.User_Security_Question usq join APP.Security_Question sq"
				+ " on usq.SECURITY_QUES_ID=sq.SECURITY_QUES_ID where usq.USER_NAME=?";
		
		List<UserSecurityQuestionForm> userSecurityQuestionFormList = new ArrayList<>();
		try {
			userSecurityQuestionFormList = jdbcTemplate.query(sql,new Object[]{username}, new UserSecurityQuestionFormMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return userSecurityQuestionFormList;
	}

	
	
}
