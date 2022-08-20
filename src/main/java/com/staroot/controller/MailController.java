package com.staroot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.staroot.domain.Board;
import com.staroot.domain.BoardRepository;
import com.staroot.domain.User;

@Controller
@RequestMapping("/mail")
public class MailController {
	
	
	
	//@Autowired
    //private MailSender mailSender;

	@Autowired
	private BoardRepository boardRepository;

	List<User> users = new ArrayList<User>();

	
	@GetMapping("/send/{id}")
	public String index(@PathVariable Long id, Model model) {
		
		//mailSender를 Autowired로 가져오면 Default session access denied발생 
		//MailSender mailSender = new MailSender();
		
		Board board = new Board();
		board = boardRepository.findOne(id);
		
		
		//Template 적용 
		Handlebars handlebars = new Handlebars();
		Template template;
		String contents="";
		try {
			template = handlebars.compileInline(board.getContents());
			
			List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("userid", "starootmaster1");
			map1.put("name", "seojungbae");
			map1.put("desc", "developer");
			for(int i=0 ; i<20;i++){
				userList.add(map1);
			}
			dataMap.put("userinfo", userList);
			
			System.out.println(template.apply(dataMap));
			
			contents = template.apply(dataMap);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//System.out.println("1111111111111::메일전송시"+board.getContents()); 
		
		try {
			//mailSender.sendMail(board.getContents());
			sendMail(board.getTitle(),contents);
		} catch (Exception e) {
			System.out.println(e.getMessage()); 
		}
		
		return "/index";
	}
	
	
	
    public void sendMail(String subject, String mailContents) throws Exception{
        
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        System.out.println("11111111111111 mail");
         
        Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                //return new PasswordAuthentication("starootmaster@gmail.com", "pqfcsnbfyvkgffxw");
                return new PasswordAuthentication("starootmaster@gmail.com", "cippxhtgsfxsxcss");
				
            }
        };
 
        System.out.println("222222222222222 mail");

    	//17.07.08 에러발생 : getDefaultInstance Access to default session denied
        //Session session = Session.getDefaultInstance(props,auth);
        Session session = Session.getInstance(props,auth);
        System.out.println("333333333333333333 mail");
        System.out.println("메일세션생성완료!");
         
        MimeMessage message = new MimeMessage(session);
        message.setSender(new InternetAddress("starootmaster@gmail.com"));
        message.setSubject(subject);
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
