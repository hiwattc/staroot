package com.staroot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.staroot.domain.Message;
import com.staroot.domain.User;
import com.staroot.domain.UserRepository;
import com.staroot.util.web.HttpSessionUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	List<User> users = new ArrayList<User>();
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		int loginFailCnt = 0;
		if(session.getAttribute("loginFailCnt") != null){
			loginFailCnt = (int) session.getAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY);
		}
		User user = userRepository.findByUserId(userId);
		if(user == null){
			loginFailCnt = loginFailCnt+1;
			System.out.println("Login Fail!(User doesn't exists!)");
			session.setAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY, loginFailCnt);
			return "redirect:/user/login";
		}
		
		if(!user.matchPassword(password)){
			loginFailCnt = loginFailCnt+1;
			System.out.println("Login Fail!(User's Password doesn't match!)");
			session.setAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY, loginFailCnt);
			return "redirect:/user/login";
		}
		
		System.out.println("Login Success!");
		session.removeAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY);
		session.setAttribute(HttpSessionUtil.USER_SESSION_KEY, user);
		
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(HttpSessionUtil.USER_SESSION_KEY);
		session.removeAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY);
		return "/user/login";
	}

	@GetMapping("/register")
	public String registerForm() {
		return "/user/register";
	}

	@PostMapping("/register")
	public String register(User user,Model model, HttpSession session) {
		System.out.println(user.toString());
		User tempUser = userRepository.findByUserId(user.getUserId());
		if(tempUser == null){
			userRepository.save(user);
		}else{
			System.out.println("User already exists! register Failed!");
			//model.addAttribute("user",user);//이렇게 하면 세션 사용자 명과 중복됨 
			model.addAttribute("formData",user);
			//session.removeAttribute("user");
			//return "redirect:/user/register"; //redirect로 하면 모델 객체가 전달 안되는듯
			return "/user/register";
		}
		return "redirect:/user/member";

	}
	@GetMapping("/profile")
	public String profile(HttpSession session) {
		return "/user/profile";
	}

	@PostMapping("/update")
	public String update(User user, HttpSession session) {
		System.out.println(user.toString());
		User sessionUser = HttpSessionUtil.getUserFromSession(session);
		sessionUser.update(user);
		userRepository.save(sessionUser);
		return "redirect:/";

	}

	@RequestMapping("/member")
	public String list(Model model) {
		List<User> users = new ArrayList<User>();
		users = userRepository.findAll();
		model.addAttribute("users", users);

		return "/user/member";
	}
	
	@GetMapping("/alert")
	public String msgList(Model model) {
		List<Message> msgList = new ArrayList<Message>();
		Message msg1 = new Message();
		Message msg2 = new Message();
		Message msg3 = new Message();
		msg1.setMsgId("1");
		msg1.setMsgTitle("Hello");
		msgList.add(msg1);

		msg2.setMsgId("2");
		msg2.setMsgTitle("Hello");
		msgList.add(msg2);

		msg3.setMsgId("3");
		msg3.setMsgTitle("Hello");
		msgList.add(msg3);

		model.addAttribute("msgList", msgList);
		return "/user/alert";

	}

}
