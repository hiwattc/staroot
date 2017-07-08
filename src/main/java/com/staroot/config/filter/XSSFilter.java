package com.staroot.config.filter;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class XSSFilter
  implements Filter
{
  private FilterConfig config = null;
  private static boolean no_init = true;
  private static final String CPR = "(c) Coldbeans mailto:info@servletsuite.com";
  private static final String VERSION = "ver. 1.1";

  public void init(FilterConfig paramFilterConfig)
    throws ServletException
  {
    this.config = paramFilterConfig;
    no_init = false;
    System.out.println("XSS filter init");

  }
 
  public void destroy()
  {
    this.config = null;
  } 

  public FilterConfig getFilterConfig()
  {
    return this.config;
  }

  public void setFilterConfig(FilterConfig paramFilterConfig)
  {
    if (no_init)
    {
      no_init = false;
      this.config = paramFilterConfig;
      System.out.println("XSS filter setFilterConfig");
    }
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
    throws IOException, ServletException
  {
	  //System.out.println("paramServletRequest.getPathInfo() :"+((HttpServletRequest)paramServletRequest).getPathInfo());
	  //System.out.println("paramServletRequest.getContextPath() :"+((HttpServletRequest)paramServletRequest).getContextPath());
	  //System.out.println("paramServletRequest.getQueryString() :"+((HttpServletRequest)paramServletRequest).getQueryString());
	  //System.out.println("paramServletRequest.getRequestURI() :"+((HttpServletRequest)paramServletRequest).getRequestURI());
	  
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String requestURI = request.getRequestURI();
	  
	if(		 requestURI.startsWith("/images") ||
			 requestURI.startsWith("/js") ||
	         requestURI.startsWith("/h2") ||
			 requestURI.startsWith("/api") ||
			 requestURI.startsWith("/ckeditor") ||
			 requestURI.startsWith("/board") ||
			 requestURI.startsWith("/user/login") ||
             requestURI.startsWith("/webjar")	){
		System.out.println("XSS Filter Ignored!");
		chain.doFilter(servletRequest, servletResponse);
		
	}else if(requestURI.startsWith("/board/modify")){
		//Filter예외적용대상
		System.out.println("Weak XSS Filter Applied!");
		chain.doFilter(servletRequest, servletResponse);
		//chain.doFilter(new RequestWrapperEx((HttpServletRequest)servletRequest), servletResponse);
	}else{
		//Filter적용대상
		System.out.println("Normal XSS Filter Applied!");
		chain.doFilter(new RequestWrapper((HttpServletRequest)servletRequest), servletResponse);
	}
  }
}