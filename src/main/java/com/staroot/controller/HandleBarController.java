package com.staroot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.staroot.domain.Board;
import com.staroot.domain.BoardRepository;
import com.staroot.domain.Reply;
import com.staroot.domain.ReplyRepository;
import com.staroot.domain.User;
import com.staroot.util.web.HttpSessionUtil;

@Controller
@RequestMapping("/handlebar")
public class HandleBarController {
	@RequestMapping("/hbtest1")
	public String hbtest1() {

		return "/handlebar/hbtest1";
	}


}
