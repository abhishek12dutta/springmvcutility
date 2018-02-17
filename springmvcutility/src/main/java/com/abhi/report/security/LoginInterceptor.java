package com.abhi.report.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
 
		public static final List<String> BYPASS_URL_LIST = new ArrayList<>();
			static {
				BYPASS_URL_LIST.add("/changebookingreport/");
				BYPASS_URL_LIST.add("/changebookingreport/login");
				BYPASS_URL_LIST.add("/changebookingreport/showSignUpForm");
				BYPASS_URL_LIST.add("/changebookingreport/userRegistration");
				BYPASS_URL_LIST.add("/changebookingreport/showResetPasswordForm");
				BYPASS_URL_LIST.add("/changebookingreport/processdatalanding");
				BYPASS_URL_LIST.add("/changebookingreport/processFTC");
				BYPASS_URL_LIST.add("/changebookingreport/resetPassword");
				BYPASS_URL_LIST.add("/changebookingreport/checkUsername");
				BYPASS_URL_LIST.add("/changebookingreport/retrieveSecurityQuestion");
				BYPASS_URL_LIST.add("/changebookingreport/logOut"); 
		 
			}
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("Calling preHandle");
		boolean isUserLoggedIn = false;
		
		if(BYPASS_URL_LIST.contains(request.getRequestURI())){
		return true;
		}
		if (null != request.getSession().getAttribute("isUserLoggedIn")) {
			isUserLoggedIn = (boolean) request.getSession().getAttribute("isUserLoggedIn");
		}
		
		if (!isUserLoggedIn) {
			response.sendRedirect("/changebookingreport/");
			return false;
		}
		return true;
	}



}
