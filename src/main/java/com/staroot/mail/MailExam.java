package com.staroot.mail;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
public class MailExam {
    public static void main(String[] args){
        try{
            MailExam("본문내용입니다.");
        }catch(Exception e){
            System.out.println("Error:"+e.getMessage());
        }
    }
    public static void MailExam(String mailContents) throws Exception{
    
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
         
        Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("starootmaster@gmail.com", "qwer1234!");
            }
        };
 

        Session session = Session.getDefaultInstance(props,auth);
        System.out.println("메일세션생성완료!");
         
        MimeMessage message = new MimeMessage(session);
        message.setSender(new InternetAddress("starootmaster@gmail.com"));
        message.setSubject("JavaMail 테스트입니다.");
        
 
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("starootmaster@gmail.com"));
         
        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText(mailContents,"UTF-8");
        mbp1.setHeader("content-Type", "text/html");
        mp.addBodyPart(mbp1);
 

        
        message.setContent(mp);
        System.out.println("step8 now sending mail...please wait!");
         
        Transport.send(message);
        System.out.println("메일전송완료!");
    }
}
