package com.staroot.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.staroot.domain.Board;
import com.staroot.domain.BoardRepository;
import com.staroot.domain.User;
import com.staroot.util.web.HttpSessionUtil;

@Controller
@RequestMapping("/board")
public class BoardController {
	//List<Board> baards = new ArrayList<Board>();
	
	@Autowired
	private BoardRepository boardRepository;

	@GetMapping("/list")
	public String boardList(Model model) {
		List<Board> baards = new ArrayList<Board>();
		System.out.println("1-11111111111111111111111111111");
		baards = boardRepository.findAll();
		System.out.println("1-22222222222222222222222222222");
		model.addAttribute("boardList", baards);
		System.out.println("1-3333333333333333333333333333");
		return "/board/list";
	}
	@GetMapping("/list/{id}")
	public String boardListAndDetail(@PathVariable Long id, Model model) {
		List<Board> baards = new ArrayList<Board>();
		baards = boardRepository.findAll();
		model.addAttribute("boardList", baards);
		
		Board board = new Board();
		System.out.println("2-11111111111111111111111111111");
		board = boardRepository.findOne(id);
		System.out.println("2-22222222222222222222222222222");
		model.addAttribute("boardDetail", board);
		System.out.println("2-3333333333333333333333333333");
		
		return "/board/list";
	}
	@GetMapping("/createForm")
	public String boardCreateForm() {
		return "/board/createForm";
	}
	@PostMapping("/create")
	public String create(String title, String contents, HttpSession session) {
		User writer = HttpSessionUtil.getUserFromSession(session);
		if(writer == null){
			return "redirect:/user/login";
		}
		Board board = new Board(writer, title,contents);
	    boardRepository.save(board);
		return "redirect:/board/list";
	}
	@GetMapping("/modify/{id}")
	public String modify(@PathVariable Long id, Model model, HttpSession session) {
		User writer = HttpSessionUtil.getUserFromSession(session);
		Board board = new Board();
		board = boardRepository.findOne(id);
		
		if(!board.isSameWriter(writer)){
			return "/user/login";
		}
		model.addAttribute("boardDetail", board);
		return "/board/modifyForm";
	}
	@PutMapping("/modify/{id}")
	public String saveModify(@PathVariable Long id, String title, String contents, HttpSession session) {
		User writer = HttpSessionUtil.getUserFromSession(session);
		Board board = new Board();
		board = boardRepository.findOne(id);
		if(!board.isSameWriter(writer)){
			return "/user/login";
		}
		board.update(title, contents);
		board = boardRepository.save(board);
		return String.format("redirect:/board/list/%d", id);
	}
	@DeleteMapping("/modify/{id}")
	public String saveModify(@PathVariable Long id, HttpSession session) {
		User writer = HttpSessionUtil.getUserFromSession(session);
		Board board = new Board();
		board = boardRepository.findOne(id);
		if(!board.isSameWriter(writer)){
			return "/user/login";
		}
		boardRepository.delete(board);
		return String.format("redirect:/board/list");
	}
	
}
