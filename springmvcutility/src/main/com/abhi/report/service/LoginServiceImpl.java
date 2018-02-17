package com.abhi.report.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.abhi.report.dao.LoginDao;
import com.abhi.report.dao.SecurityQuestionDao;
import com.abhi.report.model.LoginForm;
import com.abhi.report.model.UserSecurityQuestionForm;
import com.abhi.report.security.PasswordUtils;
import com.abhi.report.service.bo.LoginResponse;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private SecurityQuestionDao securityQuestionDao;

	@Override
	public LoginResponse userLogin(LoginForm loginForm) {

		LoginResponse loginResponse = new LoginResponse();
		LoginForm loginForm1 = null;
		try {
			loginForm1 = loginDao.retrieveLoginDetails(loginForm.getUsername());
			String password=loginForm.getPassword();
			String salt=loginDao.retrieveSalt(loginForm.getUsername());
			password=PasswordUtils.generateSecurePassword(password, salt);
			if (loginForm1.getPassword().equalsIgnoreCase(password)) {
				loginResponse.setSuccess(true);
				loginResponse.setMessage("Congratulations!!! You have successfully logged in.");
			} else {
				loginResponse.setSuccess(false);
				loginResponse.setMessage("You have entered wrong password!");
			}

		} catch (EmptyResultDataAccessException e) {
			loginResponse.setSuccess(false);
			loginResponse.setMessage("User does not exist.");
		}

		return loginResponse;
	}

	@Override
	public UserSecurityQuestionForm retrieveRandomSecurityQuestion(String username) {

		List<UserSecurityQuestionForm> userSecurityQuestionFormList = securityQuestionDao
				.retrieveUserSecurityDetails(username);
		for (UserSecurityQuestionForm form : userSecurityQuestionFormList) {
			form.setSecurity_answer(null);
		}
		UserSecurityQuestionForm userSecurityQuestionForm = null;
		if(userSecurityQuestionFormList.size()>0){
			Random random = new Random();
			int rand = 1;
			while (true) {
				rand = random.nextInt(40);
				if (rand != 0) {
					break;
				}
			}
			int index = 0;
			if (rand > 20) {
				index = 0;
			} else {
				index = 1;
			}
			userSecurityQuestionForm= userSecurityQuestionFormList.get(index);
		}else{
			userSecurityQuestionForm=new UserSecurityQuestionForm();
		}
		
		
		return userSecurityQuestionForm;
	}

}
