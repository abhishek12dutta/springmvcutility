package com.abhi.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.abhi.report.dao.LoginDao;
import com.abhi.report.model.LoginForm;
import com.abhi.report.security.PasswordUtils;
import com.abhi.report.service.bo.UserRegistrationResponse;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
	@Autowired
	private LoginDao loginDao;
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	@Transactional
	public UserRegistrationResponse userRegistration(LoginForm loginForm) {

		TransactionDefinition def = new DefaultTransactionDefinition(
				DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);

		TransactionStatus status = transactionManager.getTransaction(def);

		UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
		String password = loginForm.getPassword();
		String salt = PasswordUtils.getSalt(30);
		password = PasswordUtils.generateSecurePassword(password, salt);
		loginForm.setPassword(password);

		try {

			boolean isDetailsInserted = loginDao.insertUserDetails(loginForm);
			if (isDetailsInserted) {
				isDetailsInserted = loginDao.insertUserSalt(loginForm.getUsername(), salt);
			}
			if (isDetailsInserted) {
				loginDao.insertUserSecurityQuesDetails(loginForm);
				userRegistrationResponse.setSuccess(true);
			}
			if (!isDetailsInserted) {
				userRegistrationResponse.setSuccess(false);
			}
			
			transactionManager.commit(status);
		} catch (DataAccessException e) {
			userRegistrationResponse.setSuccess(false);
			status.setRollbackOnly();
			transactionManager.rollback(status);
		}
		return userRegistrationResponse;
	}

}
