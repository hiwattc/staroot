package com.staroot.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
import com.staroot.domain.PageInfo;
import com.staroot.domain.User;
import com.staroot.util.web.HttpSessionUtil;

@Controller
@RequestMapping("/board")
public class BoardController {
	//List<Board> baards = new ArrayList<Board>();
	
	@Autowired
	private BoardRepository boardRepository;
	
	private int pageSize = 5; //rows Per a page
	private int pagesPerSection = 5; //page count per section (between prev and next)
	private String[] rowSizeArry  = {"5","10","30","50","100"};
	private static final int MIN_PAGE_SIZE = 5;
	private static final int MAX_PAGE_SIZE = 100;

	@GetMapping("/list")
	public String boardList(String selPageNo, String selPageSize, Model model, HttpSession session) {
		
		makeBoardInfoModel(selPageNo, selPageSize, model, session);
		
		return "/board/list";
	}
	
	@GetMapping("/list/{id}")
	public String boardListAndDetail(@PathVariable Long id, String selPageNo, String selPageSize, Model model, HttpSession session) {
		makeBoardInfoModel(selPageNo, selPageSize, model, session);
		//게시판상세정보조회 
		//---------------------------------------------------------------
		Board board = new Board();
		board = boardRepository.findOne(id);
		model.addAttribute("boardDetail", board);
		//---------------------------------------------------------------
		return "/board/list";
	}
	

	private void makeBoardInfoModel(String selPageNo, String selPageSize, Model model, HttpSession session) {
		List<Board> baards = new ArrayList<Board>();
		List<PageInfo> pageInfoList = new ArrayList<PageInfo>();
		
		//currentPage, currentSelectRowNum  Session Check
		//페이지당 보여질 리스트 개수 초기화  
		//===========================================================================
		String sessionSelPageSize = (String) session.getAttribute("sessionSelPageSize");
		if (selPageSize == null){
			if(sessionSelPageSize != null){
				selPageSize = sessionSelPageSize;
			}else{
				selPageSize = Integer.toString(MIN_PAGE_SIZE);
				session.setAttribute("sessionSelPageSize", selPageSize);
			}
		}else{
			session.setAttribute("sessionSelPageSize", selPageSize);
		}
		//===========================================================================

		//현재 선택된 페이지 유지 
		//===========================================================================
		String sessionSelPageNo = (String) session.getAttribute("sessionSelPageNo");
		if (selPageNo == null){
			if(sessionSelPageNo != null){
				selPageNo = sessionSelPageNo;
			}else{
				selPageNo = "1";
				session.setAttribute("sessionSelPageNo", selPageNo);
			}
		}else{
			session.setAttribute("sessionSelPageNo", selPageNo);
		}
		//===========================================================================
		
		
		//select tag for pageSize 
		List<Map<String,String>> rowNumOfPageList = new ArrayList<Map<String,String>>();
		getSelRowNumList(selPageSize, rowNumOfPageList);
		
		//baards = boardRepository.findAll();
		if(selPageSize != null){
			pageSize = Integer.parseInt(selPageSize);
			if(pageSize > MAX_PAGE_SIZE){
				pageSize = MAX_PAGE_SIZE;
			}
		}
		//if(selPageNo == null)	selPageNo = "1";
		int selectedPageNum = Integer.parseInt(selPageNo);
		Page<Board> page = boardRepository.findAll(
				new PageRequest(selectedPageNum-1
						, pageSize
						, new Sort(new Order(Direction.DESC,"id"))
						)); //int page, int size 

		//paging 정보처리
		//---------------------------------------------------------------
		baards = page.getContent();
		int totalPages = page.getTotalPages();
		pageInfoList = getPageInfoList(pageInfoList, selectedPageNum, totalPages);
		//---------------------------------------------------------------

		model.addAttribute("boardList", baards);
		model.addAttribute("pageInfoList", pageInfoList);
		model.addAttribute("rowNumOfPageList", rowNumOfPageList);
	}

	
	//게시판 페이징처리관련 (17.06.23)
	//TODO
	//01. Prev, Next 버튼처리
	//  a) pageInfoList를 감싸는 map 또는 list생성해서 list.html과 연계
	//  b) prev, next 도 java단에서 렌더링해서 던져주는 방법
	
	private List<PageInfo> getPageInfoList(List<PageInfo> pageInfoList, int selectedPageNum, int totalPages) {
		//selected page
		//secion
		
		//how many section
		int totalSection =  0;
		int selectedSection = 0;
		
		
		totalSection = totalPages / pagesPerSection + 1 ;
		System.out.println("totalPages : "+totalPages);
		System.out.println("pagesPerSection : "+pagesPerSection);
		System.out.println("totalSection : "+totalSection);
		
		//6 / (5)  = 1
		//11 / (5) = 1
		//10 / (5+1) = 1		
		selectedSection = (selectedPageNum-1) / (pagesPerSection);
		int startPageNoOfSection = selectedSection*(pagesPerSection);
		int endPageNoOfSection   = startPageNoOfSection + pagesPerSection;
		
		PageInfo pageInfo;
		
		//Previous Button
		if(startPageNoOfSection - 1 > 0){
			pageInfo = new PageInfo();
			pageInfo.setSelPageNo(startPageNoOfSection - 1);
			pageInfo.setPageText("<span aria-hidden='true'>&laquo;</span>");
			pageInfo.setActive("");
			pageInfo.setPageSize(pageSize);
			pageInfoList.add(pageInfo);
		}
		
		for(int i=startPageNoOfSection ;i<endPageNoOfSection;i++){
			if(i < totalPages){
				pageInfo = new PageInfo();
				pageInfo.setSelPageNo(i+1);
				pageInfo.setPageText(Integer.toString(i+1));
				if(selectedPageNum == i+1){
				    pageInfo.setActive("class='active'");
				}else{
					pageInfo.setActive("");
				}
				pageInfo.setPageSize(pageSize);
				pageInfoList.add(pageInfo);
			}
		}
		
		//Next Button
		if(endPageNoOfSection < totalPages){
			pageInfo = new PageInfo();
			pageInfo.setSelPageNo(endPageNoOfSection + 1);
			pageInfo.setPageText("<span aria-hidden='true'>&raquo;</span>");
			pageInfo.setActive("");
			pageInfo.setPageSize(pageSize);
			pageInfoList.add(pageInfo);
		}
		
		return pageInfoList;
	}
	private void getSelRowNumList(String selPageSize, List<Map<String, String>> rowNumOfPageList) {
		Map<String,String> rowMap;
		for (int i=0 ; i < rowSizeArry.length ; i++){
			rowMap = new HashMap<String,String>(); 
			if(rowSizeArry[i].equals(selPageSize)){
				rowMap.put("active", "class='active'");
			}else{
				rowMap.put("active", "");
			}
			rowMap.put("rowNumOfPage", rowSizeArry[i]);
			rowMap.put("rowNumText", rowSizeArry[i]);
			rowNumOfPageList.add(rowMap);
		}
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
			return "redirect:/user/login";
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
