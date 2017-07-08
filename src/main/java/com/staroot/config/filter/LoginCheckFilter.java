package com.staroot.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.staroot.controller.UserController;
import com.staroot.domain.LoginHistRepository;
import com.staroot.domain.User;
import com.staroot.util.web.HttpSessionUtil;

import java.io.IOException;
  
@Component
public class LoginCheckFilter implements Filter {
	  public static final String X_HEADER_TEST = "X_HEADER_TEST";

      @Autowired
	  private UserController userController;
	  
	  @Override
	  public void doFilter(ServletRequest req, ServletResponse res,
	      FilterChain chain) throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;

			String requestURI = request.getRequestURI();
			

			//security area session check
			if(
					requestURI.equals("/") ||
					requestURI.startsWith("/api/users") ||
					requestURI.startsWith("/user/login") ||
					requestURI.startsWith("/user/register") ||
					requestURI.startsWith("/user/profile/image") ||
					requestURI.startsWith("/images") ||
					requestURI.startsWith("/js") ||
					requestURI.startsWith("/h2") ||
					requestURI.startsWith("/ckeditor") ||
					requestURI.startsWith("/webjars") ||
					requestURI.startsWith("/board/list") ||
					requestURI.startsWith("/board/file")
					){
				//DO NOT LoginCheck
				System.out.println("Login check ignored::"+request.getRequestURI());
			    chain.doFilter(req, res);
			}else{
		    	//System.out.println("=========================request.getRequestURI().startsWith(/user/member)======");
				//System.out.println(":::TestFilter doFilter() called");
				//System.out.println("req.getServletContext()::"+req.getServletContext());
				//System.out.println("req.getContentType()::"+req.getContentType());
				//System.out.println("req.getLocalAddr()::"+req.getLocalAddr());
				//System.out.println("req.getLocale()::"+req.getLocale());

				//getRemoteAddr - Returns the IP address of the client or last proxy that sent the request
				//getLocalAddr - Returns the IP address of the interface on which the request was received.		  
				//System.out.println("req.getScheme()::"+req.getScheme());
		    	
			    //response.setHeader(X_HEADER_TEST, "STAROOT HIWATT");
				//System.out.println("req.getRemoteAddr()::"+req.getRemoteAddr());
				//System.out.println("req.getCharacterEncoding()::"+req.getCharacterEncoding());
			    HttpSession session = request.getSession(false);
			    if(session != null){
				    User user = HttpSessionUtil.getUserFromSession(session);
				    if(user != null){
						System.out.println("Login required!::"+request.getRequestURI());
				    	System.out.println("Login User ::"+user.toString());
				    	System.out.println("111111111111111111111111");
				    	userController.saveLoginHist(request, user, "SUCCESS");
				    	System.out.println("222222222222222222222222");
					    chain.doFilter(req, res);
				    	System.out.println("333333333333333333333333");
				    }else{
						System.out.println("Login required!::"+request.getRequestURI());
				    	System.out.println("Session exists! But Not Logined yet!");
				    	//17.07.02 use try catch for unexpected Exception
				    	/*
				    	try{
				    	userController.saveLoginHist(request, user, "FAIL(Not Login)");
				    	}catch(Exception e){
				    		System.out.println(e.toString());
				    	}
				    	*/
				    	response.sendRedirect("/user/login");
				    }
			    }else{
					System.out.println("Login requred!::"+request.getRequestURI());
			    	System.out.println("Session doesn't exists! And Not Logined yet!");
			    	response.sendRedirect("/user/login");
			    }
			}
	  }

	  @Override
	  public void destroy() {
			System.out.println(":::TestFilter destroy() called");
	  }

	  @Override
	  public void init(FilterConfig arg0) throws ServletException {
			System.out.println(":::TestFilter init() called");		  
	  }
}
