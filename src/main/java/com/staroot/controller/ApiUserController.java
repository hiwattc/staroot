package com.staroot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staroot.domain.User;
import com.staroot.domain.UserRepository;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/{id}")
	public User show(@PathVariable Long id){
		User user = userRepository.findOne(id);
		return user;
	}
}
