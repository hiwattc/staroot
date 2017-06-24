package com.staroot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.staroot.domain.Board;
import com.staroot.domain.BoardRepository;
import com.staroot.domain.User;

@Controller
public class HomeController {
	@Autowired
	private BoardRepository boardRepository;

	List<User> users = new ArrayList<User>();

	@GetMapping("/")
	public String index(Model model) {
		List<Board> baards = new ArrayList<Board>();
		Page<Board> page = boardRepository.findAll(
				new PageRequest(0
						, 3
						, new Sort(new Order(Direction.DESC,"id"))
						)); 
		baards = page.getContent();
		model.addAttribute("boardList", baards);

		page = boardRepository.findAll(
				new PageRequest(0
						, 3
						, new Sort(new Order(Direction.ASC,"id"))
						)); 
		baards = page.getContent();
		model.addAttribute("boardList2", baards);
		
		return "/index";
	}
}
