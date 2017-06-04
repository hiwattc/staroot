package com.staroot.util.web;

import javax.servlet.http.HttpSession;

import com.staroot.domain.User;

public class HttpSessionUtil {
	public static final String USER_SESSION_KEY = "sessionUser";
	public static final String LOGIN_FAILED_CNT_KEY = "loginFailCnt";
	
	public static boolean isLoginUser(HttpSession session){
		Object sessionUser = session.getAttribute(USER_SESSION_KEY);
		if(sessionUser == null){
			return false;
		}
		return true;
	}
	
	public static User getUserFromSession(HttpSession session){
		if(!isLoginUser(session)){
			return null;
		}
		User sessionUser = (User)session.getAttribute(USER_SESSION_KEY);
		return sessionUser;
	}
}
