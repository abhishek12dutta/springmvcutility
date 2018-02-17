package com.abhi.report.model;

public class UserSecurityQuestionForm {
    
	private String newcpassword;
	private String newpassword;
	private String reset_username;
	private String security_answer;
	private String security_question;
	private String security_question_Id;

	public String getNewcpassword() {
		return newcpassword;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public String getReset_username() {
		return reset_username;
	}
	public String getSecurity_answer() {
		return security_answer;
	}

	public String getSecurity_question() {
		return security_question;
	}

	public String getSecurity_question_Id() {
		return security_question_Id;
	}

	public void setNewcpassword(String newcpassword) {
		this.newcpassword = newcpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public void setReset_username(String reset_username) {
		this.reset_username = reset_username;
	}

	public void setSecurity_answer(String security_answer) {
		this.security_answer = security_answer;
	}

	public void setSecurity_question(String security_question) {
		this.security_question = security_question;
	}

	public void setSecurity_question_Id(String security_question_Id) {
		this.security_question_Id = security_question_Id;
	}

}
