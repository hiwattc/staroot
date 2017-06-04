package com.staroot.config.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.stereotype.Component;

@Component 
public class WebApplicationInitializer implements org.springframework.web.WebApplicationInitializer {


    @Override 
    public void onStartup(ServletContext servletContext) throws ServletException {
    	System.out.println("##### WebApplicationInitializer onStartup #####"+servletContext.toString());
//        log.debug("##### WebApplicationInitializer onStartup #####"+servletContext.toString());
//        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
//        ctx.register(WebMvcConfigurerAdapter.class);
//        ctx.setServletContext(servletContext);
//        Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
//        dynamic.addMapping("/");
//        dynamic.setLoadOnStartup(1);

    }
}
