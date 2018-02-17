package com.abhi.report.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abhi.report.model.LoginForm;

public class LoginFormMapper implements RowMapper<LoginForm> {

	public LoginForm mapRow(ResultSet rs, int rowNum) throws SQLException {
		/*USER_NAME varchar(20) not null,
	FIRST_NAME varchar(20) not null,
	LAST_NAME varchar(20) not null,
	EMAIL varchar(20) not null,
	CONTACT numeric(10) not null,
	USER_PASSWORD varchar(12) not null,
	primary key(USER_NAME)
	);
	*/

		LoginForm  loginForm = new LoginForm();
		loginForm.setUsername(rs.getString("USER_NAME"));
		loginForm.setPassword(rs.getString("USER_PASSWORD"));
		loginForm.setFirstName(rs.getString("FIRST_NAME"));
		loginForm.setLastName(rs.getString("LAST_NAME"));
		loginForm.setEmail(rs.getString("EMAIL"));
		loginForm.setContact(rs.getString("CONTACT"));
		return loginForm;
	}

}
