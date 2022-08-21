package com.staroot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.staroot.domain.Board;
import com.staroot.domain.BoardRepository;
import com.staroot.domain.User;
import com.staroot.util.web.HttpSessionUtil;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/handlebar")
public class HandleBarController {
	@Autowired
	private BoardRepository boardRepository;

	@RequestMapping("/hbtest1")
	public String hbtest1(Model model, HttpSession session) {
		User writer = HttpSessionUtil.getUserFromSession(session);
		Board board = new Board();
        long id = 61;
		board = boardRepository.findOne(id);
		
	    System.out.println("로그인사용자(세션):"+writer.getId()+"/"+writer.getName()+"/"+writer.getUserId());
	    System.out.println("게시판글쓴이 :"+board.getWriter().getId()+"/"+board.getWriter().getName()+board.getWriter().getUserId());
	    System.out.println("게시판제목 :"+board.getTitle());
	    System.out.println("게시판내용 :"+board.getContents());
		
		if(!board.isSameWriter(writer)){
			System.out.println("HandleBarController 동일사용자가 아닙니다 ");
			return "redirect:/user/login";
		}
		System.out.println("HandleBarController 동일사용자입니다");
		model.addAttribute("boardDetail", board);

		return "/handlebar/hbtest1";
	}
}
