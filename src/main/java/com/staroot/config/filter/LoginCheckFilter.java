package com.staroot.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.staroot.domain.User;
import com.staroot.util.web.HttpSessionUtil;

import java.io.IOException;
  
@Component
public class LoginCheckFilter implements Filter {
	  public static final String X_HEADER_TEST = "X_HEADER_TEST";

	  @Override
	  public void doFilter(ServletRequest req, ServletResponse res,
	      FilterChain chain) throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;

			

			//security area session check
			if(
					request.getRequestURI().equals("/") ||
					request.getRequestURI().startsWith("/user/login") ||
					request.getRequestURI().startsWith("/user/register") ||
					request.getRequestURI().startsWith("/images") ||
					request.getRequestURI().startsWith("/h2") ||
					request.getRequestURI().startsWith("/webjars") ||
					request.getRequestURI().startsWith("/board/list")
					){
				//DO NOT LoginCheck
				System.out.println("Login check ignored::"+request.getRequestURI());
			    chain.doFilter(req, res);
			}else{
		    	System.out.println("=========================request.getRequestURI().startsWith(/user/member)======");
				//System.out.println(":::TestFilter doFilter() called");
				//System.out.println("req.getServletContext()::"+req.getServletContext());
				//System.out.println("req.getContentType()::"+req.getContentType());
				//System.out.println("req.getLocalAddr()::"+req.getLocalAddr());
				//System.out.println("req.getLocale()::"+req.getLocale());

				//getRemoteAddr - Returns the IP address of the client or last proxy that sent the request
				//getLocalAddr - Returns the IP address of the interface on which the request was received.		  
				//System.out.println("req.getScheme()::"+req.getScheme());
		    	
			    //response.setHeader(X_HEADER_TEST, "STAROOT HIWATT");
				System.out.println("req.getRemoteAddr()::"+req.getRemoteAddr());
				System.out.println("req.getCharacterEncoding()::"+req.getCharacterEncoding());
			    HttpSession session = request.getSession(false);
			    if(session != null){
				    User user = HttpSessionUtil.getUserFromSession(session);
				    if(user != null){
						System.out.println("Login requred!::"+request.getRequestURI());
				    	System.out.println("Login User ::"+user.toString());
					    chain.doFilter(req, res);
				    }else{
						System.out.println("Login requred!::"+request.getRequestURI());
				    	System.out.println("Session exists! But Not Logined yet!");
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
