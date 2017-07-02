package com.staroot.config.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class RequestWrapperEx extends HttpServletRequestWrapper
{
  public RequestWrapperEx(HttpServletRequest paramHttpServletRequest)
  {
    super(paramHttpServletRequest);
  }
  public String[] getParameterValues(String paramString)
  {
    String[] arrayOfString1 = super.getParameterValues(paramString);
    if (arrayOfString1 == null)
      return null;
    int i = arrayOfString1.length;
    String[] arrayOfString2 = new String[i];
    for (int j = 0; j < i; j++)
      arrayOfString2[j] = cleanXSS(arrayOfString1[j]);
    return arrayOfString2;
  }
 
  public String getParameter(String paramString)
  {
    String str = super.getParameter(paramString);
    if (str == null)
      return null;
    return cleanXSS(str);
  }

  public String getHeader(String paramString)
  {
    String str = super.getHeader(paramString);
    if (str == null)
      return null;
    return cleanXSS(str);
  }

  private String cleanXSS(String paramString)
  {
    System.out.println("cleanXSS[a]:" + paramString);
    //paramString = paramString.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    //paramString = paramString.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
    //paramString = paramString.replaceAll("'", "&#39;");
    //paramString = paramString.replaceAll("eval\\((.*)\\)", "");
    paramString = paramString.replaceAll("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']", "\"\"");
    paramString = paramString.replaceAll("script", "");
    System.out.println("cleanXSS[b]:" + paramString);
    return paramString;
  }
}