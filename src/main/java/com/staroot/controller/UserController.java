package com.staroot.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.staroot.StarootApplication;
import com.staroot.domain.UserPictureRepository;
import com.staroot.domain.Message;
import com.staroot.domain.MessageRepository;
import com.staroot.domain.User;
import com.staroot.domain.UserPicture;
import com.staroot.domain.UserRepository;
import com.staroot.util.web.HttpSessionUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	List<User> users = new ArrayList<User>();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserPictureRepository fileRepository;
	
	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}

	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		int loginFailCnt = 0;
		if (session.getAttribute("loginFailCnt") != null) {
			loginFailCnt = (int) session.getAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY);
		}
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			loginFailCnt = loginFailCnt + 1;
			System.out.println("Login Fail!(User doesn't exists!)");
			session.setAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY, loginFailCnt);
			return "redirect:/user/login";
		}

		if (!user.matchPassword(password)) {
			loginFailCnt = loginFailCnt + 1;
			System.out.println("Login Fail!(User's Password doesn't match!)");
			session.setAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY, loginFailCnt);
			return "redirect:/user/login";
		}

		System.out.println("Login Success!");
		session.removeAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY);
		session.setAttribute(HttpSessionUtil.USER_SESSION_KEY, user);

		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(HttpSessionUtil.USER_SESSION_KEY);
		session.removeAttribute(HttpSessionUtil.LOGIN_FAILED_CNT_KEY);
		return "/user/login";
	}

	@GetMapping("/register")
	public String registerForm() {
		return "/user/register";
	}

	@PostMapping("/register")
	public String register(User user, Model model, HttpSession session) {
		System.out.println(user.toString());
		User tempUser = userRepository.findByUserId(user.getUserId());
		if (tempUser == null) {
			userRepository.save(user);
		} else {
			System.out.println("User already exists! register Failed!");
			// model.addAttribute("user",user);//이렇게 하면 세션 사용자 명과 중복됨
			model.addAttribute("formData", user);
			// session.removeAttribute("user");
			// return "redirect:/user/register"; //redirect로 하면 모델 객체가 전달 안되는듯
			return "/user/register";
		}
		return "redirect:/user/member";

	}

	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {
		
		//사용자사진정보를 User프러퍼티 추가해서 가져오는걸로 변경함 
		/*
		User sessionUser = HttpSessionUtil.getUserFromSession(session);
		Map<String, String> userInfoMap = new HashMap<String, String>();
		com.staroot.domain.File file = fileRepository.findByUser(sessionUser);
		if(file != null){
			userInfoMap.put("picFileNm",file.getOrigFileNm());
		}else{
			userInfoMap.put("picFileNm","");
		}
		model.addAttribute("additionalUserInfo",userInfoMap);
		*/
		return "/user/profile";
	}

	@PostMapping("/update")
	public String update(User user, String password, HttpSession session, MultipartFile picfile) {
		System.out.println(user.toString());
		System.out.println("******************81111111111111111");
		User sessionUser = HttpSessionUtil.getUserFromSession(session);

		// ==========File Upload===============
		System.out.println("file.getName()...::"+picfile.getName());
		System.out.println("file.getOriginalFilename()...::"+picfile.getOriginalFilename());
		System.out.println("file.getSize()...::"+picfile.getSize());

		com.staroot.domain.UserPicture userPicFile = null;
		if (!picfile.isEmpty()) {
			try {
				System.out.println("UploadFileController fileUpload 33333333333333333333...");

				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(StarootApplication.UPLOAD_DIR + "/" + sessionUser.getUserId()+"_"+picfile.getOriginalFilename())));
				FileCopyUtils.copy(picfile.getInputStream(), stream);
				stream.close();
				
				userPicFile = new com.staroot.domain.UserPicture(sessionUser
						, picfile.getOriginalFilename()
						, sessionUser.getUserId()+"_"+picfile.getOriginalFilename()
						, StarootApplication.UPLOAD_DIR
						, picfile.getSize());
				
				UserPicture oldUserPicFile = fileRepository.findByUser(sessionUser);//중복저장오류 방지하기위해 수정시 기존 데이터 삭제함
				if(oldUserPicFile != null){
					oldUserPicFile.update(userPicFile);
					fileRepository.save(oldUserPicFile);
					sessionUser.update(user, password, oldUserPicFile);
				}else{
					fileRepository.save(userPicFile);
					sessionUser.update(user, password, userPicFile);
				}
				System.out.println("UploadFileController fileUpload success????...");
			} catch (Exception e) {
				System.out.println("UploadFileController fileUpload Fail??..." + e.getMessage());
			}
		} else {
			System.out.println("UploadFileController fileUpload Empty File...");
		}
		userRepository.save(sessionUser);

		// ==========File Upload===============

		return "redirect:/user/profile";

	}
    
	
	//getImage : reference URL : https://stackoverflow.com/questions/20907133/how-to-display-uploaded-images-in-spring-mvc-application
	//file extension Pathvariable issue : https://stackoverflow.com/questions/16332092/spring-mvc-pathvariable-with-dot-is-getting-truncated
    @RequestMapping(value = "/profile/image/{imageName:.+}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {
    	System.out.println("getImage().............");
        File serverFile = new File(StarootApplication.UPLOAD_DIR +"/"+ imageName);
        return Files.readAllBytes(serverFile.toPath());
    }

	@RequestMapping("/member")
	public String list(Model model) {
		List<User> users = new ArrayList<User>();
		users = userRepository.findAll();
		model.addAttribute("users", users);

		return "/user/member";
	}

	@GetMapping("/alert")
	public String msgList(Model model) {
		List<Message> msgList = new ArrayList<Message>();

		// msgList = messageRepository.getMsgLists();
		// msgList = messageRepository.findAll(); //(arg0, arg1);
		// msgList = messageRepository.findAll();//잘됨
		// msgList = messageRepository.findAllByOrderByIdDesc();//잘됨
		// msgList = messageRepository.findTop2ByOrderByIdDesc();//잘됨
		// msgList = messageRepository.findAll(Pageable a)
		Page<Message> messages = messageRepository.findAll(new PageRequest(0, 5)); // int
																					// page,
																					// int
																					// size

		System.out.println("messages.getSize():" + messages.getSize());
		System.out.println("messages.getTotalElements():" + messages.getTotalElements());
		System.out.println("messages.getTotalPages():" + messages.getTotalPages());
		System.out.println("messages.getSize():" + messages.getSize());
		System.out.println("messages.getNumber():" + messages.getNumber());
		System.out.println("messages.getNumberOfElements():" + messages.getNumberOfElements());
		msgList = messages.getContent();
		System.out.println("msgList.size():" + msgList.size());
		System.out.println("msgList.toString():" + msgList.toString());

		for (Iterator iterator = msgList.iterator(); iterator.hasNext();) {
			Message message = (Message) iterator.next();
			System.out.println("message :::" + message.getMsgTitle());

		}

		model.addAttribute("msgList", msgList);
		return "/user/alert";

	}

}
