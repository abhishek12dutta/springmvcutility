package com.abhi.report.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.abhi.report.dao.mapper.LoginFormMapper;
import com.abhi.report.dao.mapper.UserSecurityQuestionFormMapper;
import com.abhi.report.model.LoginForm;
import com.abhi.report.model.UserSecurity;
import com.abhi.report.model.UserSecurityQuestionForm;
@Repository
public class LoginDaoImpl implements LoginDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public LoginForm retrieveLoginDetails(String username) throws EmptyResultDataAccessException {

		String sql = "Select * from APP.Login_User where USER_NAME=?";
		LoginForm loginForm = jdbcTemplate.queryForObject(sql, new Object[] { username }, new LoginFormMapper());
		String sql1 = "select l.USER_NAME,u.SECURITY_QUES_ID,u.SECURITY_QUES_ANSWER,s.SECURITY_QUES"
				+ " from APP.Login_User l join APP.User_Security_Question u "
				+ "on l.USER_NAME=u.USER_NAME join APP.Security_Question s"
				+ " on u.SECURITY_QUES_ID=s.SECURITY_QUES_ID where l.USER_NAME=?";
		
		List<UserSecurityQuestionForm>  userSecurityQuestionForms=jdbcTemplate.query(sql1, new Object[]{username},new UserSecurityQuestionFormMapper());
		
		if(userSecurityQuestionForms.size()>0){
			UserSecurityQuestionForm userSecurityQuestion1=userSecurityQuestionForms.get(0);
			loginForm.setSeqQes1(userSecurityQuestion1.getSecurity_question_Id());
			loginForm.setSecurityresponse1(userSecurityQuestion1.getSecurity_answer());
		}
		
		if(userSecurityQuestionForms.size()>1){
			UserSecurityQuestionForm userSecurityQuestion2=userSecurityQuestionForms.get(1);
			loginForm.setSeqQes2(userSecurityQuestion2.getSecurity_question_Id());
			loginForm.setSecurityresponse2(userSecurityQuestion2.getSecurity_answer());
		}
		
		
		System.out.println("Created Record Name = " + loginForm.getUsername());

		return loginForm;
	}
	/*
	 * create table APP.Login_User( USER_NAME varchar(20) not null, FIRST_NAME
	 * varchar(20) not null, LAST_NAME varchar(20) not null, EMAIL varchar(20)
	 * not null, CONTACT numeric(10) not null, USER_PASSWORD varchar(12) not
	 * null, primary key(USER_NAME) );
	 */

	@Override
	public boolean insertUserDetails(LoginForm loginForm) throws DataAccessException {

		boolean isDetailsInserted = false;
		String sql = "insert into APP.Login_User Values(?,?,?,?,?,?)";
		Object[] object = new Object[] { loginForm.getUsername(), loginForm.getFirstName(), loginForm.getLastName(),
				loginForm.getEmail(), loginForm.getContact(), loginForm.getPassword() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.NUMERIC,
				Types.VARCHAR };
		int row = jdbcTemplate.update(sql, object, types);
		System.out.println("number of inserted rows:" + row);
		if (row > 0) {
			isDetailsInserted = true;
		}

		return isDetailsInserted;
	}

	@Override
	public boolean updatePassword(String password, String username) throws DataAccessException {
		boolean isDetailsUpdated = false;
		String sql = "update APP.Login_User set USER_PASSWORD=? where USER_NAME=?";
		Object[] objects = new Object[] { password, username };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		int row = jdbcTemplate.update(sql, objects, types);
		System.out.println("number of inserted rows:" + row);
		if (row > 0) {
			isDetailsUpdated = true;
		}

		return isDetailsUpdated;
	}

	/*
	 * create table APP.User_Security_Question( USER_NAME varchar(20) not null,
	 * SECURITY_QUES_ID varchar(20) not null, SECURITY_QUES_ANSWER varchar(20)
	 * not null, primary key(USER_NAME,SECURITY_QUES_ID), constraint FK_UserName
	 * foreign key (USER_NAME) references APP.Login_User(USER_NAME), constraint
	 * FK_SecurityQuestionId foreign key (SECURITY_QUES_ID) references
	 * APP.Security_Question(SECURITY_QUES_ID) );
	 */

	@Override
	public void insertUserSecurityQuesDetails(LoginForm loginForm) throws DataAccessException {
		String sql = "insert into APP.User_Security_Question values(?,?,?)";

		final List<UserSecurity> userSecurities = new ArrayList<>();

		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUserName(loginForm.getUsername());
		//userSecurity.setUserName(loginForm.getUsername());
		userSecurity.setSec_id(loginForm.getSeqQes1());
		userSecurity.setSec_value(loginForm.getSecurityresponse1());

		UserSecurity userSecurity1 = new UserSecurity();
		userSecurity1.setUserName(loginForm.getUsername());
		userSecurity1.setSec_id(loginForm.getSeqQes2());
		userSecurity1.setSec_value(loginForm.getSecurityresponse2());
		
		userSecurities.add(userSecurity);
		userSecurities.add(userSecurity1);

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserSecurity userSecurity = userSecurities.get(i);
				ps.setString(1, userSecurity.getUserName());
				ps.setString(2, userSecurity.getSec_id());
				ps.setString(3, userSecurity.getSec_value());
			}

			@Override
			public int getBatchSize() {
				return userSecurities.size();
			}
		});

	}

	@Override
	public boolean insertUserSalt(String username, String salt)
			throws DataAccessException {
		/*create table APP.User_Salt(
				USER_NAME varchar(20) not null,
				USER_SALT varchar(30) not null,
				constraint FK_Salt_UserName foreign key (USER_NAME) references APP.Login_User(USER_NAME)
				);*/
		String sql="insert into APP.User_Salt values(?,?)";
		Object[] obj=new Object[]{username,salt};
		int[] types=new int[]{Types.VARCHAR,Types.VARCHAR};
		int rows=jdbcTemplate.update(sql, obj, types);
		if(rows>0){
			return true;
		}
		return false;
	}
	public String retrieveSalt(String username){
		
		String sql = "Select USER_SALT from APP.User_Salt where USER_NAME=?";
		Object[] obj = new Object[] {username};
		String salt=jdbcTemplate.queryForObject(sql, obj, String.class);
		return salt;
	}

}
