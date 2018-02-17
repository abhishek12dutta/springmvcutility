package com.abhi.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.abhi.report.dao.LoginDao;
import com.abhi.report.model.LoginForm;
import com.abhi.report.model.UserSecurityQuestionForm;
import com.abhi.report.security.PasswordUtils;
import com.abhi.report.service.bo.ResetPasswordUserResponse;

@Service
public class ResetUserPasswordServiceImpl implements ResetUserPasswordService {

	@Autowired
	private LoginDao loginDao;

	@Override
	public ResetPasswordUserResponse resetUserPassword(UserSecurityQuestionForm userSecurityQuestionForm) {
		ResetPasswordUserResponse resetPasswordUserResponse = new ResetPasswordUserResponse();
		boolean isResponseCorrect = false;

		try {
			LoginForm loginForm = loginDao.retrieveLoginDetails(userSecurityQuestionForm.getReset_username());
			if (loginForm.getSeqQes1().equals(userSecurityQuestionForm.getSecurity_question_Id())) {
				if (loginForm.getSecurityresponse1().equals(userSecurityQuestionForm.getSecurity_answer())) {
					isResponseCorrect = true;
				}
			} else if (loginForm.getSeqQes2().equals(userSecurityQuestionForm.getSecurity_question_Id())) {
				if (loginForm.getSecurityresponse2().equals(userSecurityQuestionForm.getSecurity_answer())) {
					isResponseCorrect = true;
				}
			}
			if (!isResponseCorrect) {
				resetPasswordUserResponse.setSuccess(false);
				resetPasswordUserResponse.setMessage("Please enter correct answer");
				
			}
		} catch (EmptyResultDataAccessException e) {
			resetPasswordUserResponse.setSuccess(false);
			resetPasswordUserResponse.setMessage("User Does not exists");
		}
		
		if (isResponseCorrect) {
			// update password in database
			try {
				String salt=loginDao.retrieveSalt(userSecurityQuestionForm.getReset_username());
				String password=PasswordUtils.generateSecurePassword(userSecurityQuestionForm.getNewpassword(), salt);
				boolean isPasswordUpdated = loginDao.updatePassword(password,
						userSecurityQuestionForm.getReset_username());
				resetPasswordUserResponse.setSuccess(isPasswordUpdated);
				if(isPasswordUpdated){
					resetPasswordUserResponse.setMessage("You have reset your password successfully!");
				}
				
			} catch (DataAccessException e) {
				resetPasswordUserResponse.setSuccess(false);
				resetPasswordUserResponse.setMessage("Sorry some problem is going on");
			}
		}
		return resetPasswordUserResponse;
	}

}
