package com.staroot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staroot.domain.Board;
import com.staroot.domain.BoardRepository;
import com.staroot.domain.Reply;
import com.staroot.domain.ReplyRepository;
import com.staroot.domain.User;
import com.staroot.util.web.HttpSessionUtil;

//@Controller
@RestController  // JSON처리를 위해서 RestController 사용해야한다 
@RequestMapping("/api/board/{boardId}/reply")
public class ApiReplyController {
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@PostMapping("/create")
	public Reply create(@PathVariable Long boardId, String contents, HttpSession session) {
		User writer = HttpSessionUtil.getUserFromSession(session);
		if(writer == null){
			return null;
		}
		Board board = (Board) boardRepository.findOne(boardId);
		Reply reply = new Reply(writer,board, contents);
	    
		return replyRepository.save(reply);
	}

}
