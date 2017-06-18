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
@RequestMapping("/board/{boardId}/reply")
public class ReplyController {
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@PostMapping("/create")
	public String create(@PathVariable Long boardId, String contents, HttpSession session) {
		User writer = HttpSessionUtil.getUserFromSession(session);
		if(writer == null){
			return "redirect:/user/login";
		}
		Board board = boardRepository.findOne(boardId);
		Reply reply = new Reply(writer,board, contents);
	    replyRepository.save(reply);
		return String.format("redirect:/board/list/%d", boardId);
	}

}
