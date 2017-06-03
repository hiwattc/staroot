package com.staroot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.staroot.model.User;

@Controller
public class HomeController {
	List<User> users = new ArrayList<User>();

	@GetMapping("/")
	public String index() {
		return "/index";
	}
}
