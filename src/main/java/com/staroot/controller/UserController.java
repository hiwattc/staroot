package com.staroot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.staroot.model.Message;
import com.staroot.model.User;

@Controller
@RequestMapping("/user")
public class UserController {
	List<User> users = new ArrayList<User>();

	@GetMapping("/login")
	public String login(String userid, String password, HttpSession session) {
		return "/user/login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "/user/login";
	}

	@GetMapping("/register")
	public String registerForm() {
		return "/user/register";
	}

	@PostMapping("/register")
	public String register(User user) {
		System.out.println(user.getUserId());
		System.out.println(user.getPassword());
		System.out.println(user.getName());
		System.out.println(user.getEmail());
		users.add(user);
		return "redirect:/user/list";

	}

	@RequestMapping("/member")
	public String list(Model model) {
		//List<User> users = new ArrayList<User>();

		User user1 = new User();
		user1.setUserId("starootmaster");
		user1.setPassword("pass123");
		user1.setName("starootmaster");
		user1.setEmail("starootmaster@gmail.com");
		users.add(user1);
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
