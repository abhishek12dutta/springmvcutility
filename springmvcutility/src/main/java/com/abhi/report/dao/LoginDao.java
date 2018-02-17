package com.abhi.report.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.abhi.report.model.LoginForm;
import com.abhi.report.model.SecurityQuestionForm;

public interface LoginDao {

	public LoginForm retrieveLoginDetails(String username) throws EmptyResultDataAccessException;
	public boolean insertUserDetails(LoginForm loginForm) throws DataAccessException;
	public boolean updatePassword(String password,String username) throws DataAccessException;
	public void insertUserSecurityQuesDetails(LoginForm loginForm) throws DataAccessException;
	public boolean insertUserSalt(String username,String salt) throws DataAccessException;
	public String retrieveSalt(String username) throws EmptyResultDataAccessException;
}
