package com.staroot.controller;

import com.staroot.StarootApplication;
import com.staroot.domain.User;
import com.staroot.domain.UserFile;
import com.staroot.domain.UserFileRepository;
import com.staroot.domain.UserPicture;
import com.staroot.domain.UserPictureRepository;
import com.staroot.util.web.HttpSessionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/ckeditor/file")
public class CKEditorFileController {

	@Autowired
	private UserFileRepository userFileRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/files")
	public String provideUploadInfo(Model model) {
		File rootFolder = new File(StarootApplication.UPLOAD_DIR_CKEDITOR);

		List<String> fileNames = Arrays.stream(rootFolder.listFiles()).map(f -> f.getName())
				.collect(Collectors.toList());

		model.addAttribute("files",
				Arrays.stream(rootFolder.listFiles()).sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
						.map(f -> f.getName()).collect(Collectors.toList()));

		return "/test/upload/upload_result";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/userFileList")
	public String userFileList(Model model, HttpSession session) {

		User sessionUser = HttpSessionUtil.getUserFromSession(session);
		List<UserFile> userFileList = userFileRepository.findByUser(sessionUser);

		System.out.println("userFileList:" + userFileList);

		model.addAttribute("userFileList", userFileList);

		return "/board/filelist";
	}

	
	
	@RequestMapping(method = RequestMethod.GET, value = "/editorFileList")
	public String editorFileList(Model model, HttpSession session) {

		User sessionUser = HttpSessionUtil.getUserFromSession(session);
		List<UserFile> userFileList = userFileRepository.findByUser(sessionUser);

		System.out.println("userFileList:" + userFileList);

		model.addAttribute("userFileList", userFileList);

		return "/board/ckeditorfilelist";
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/{uploadType}/upload")
	// @ResponseBody
	public String fileUpload(@PathVariable(value = "uploadType") String uploadType 
			               , @RequestParam("upload") MultipartFile file
			               , HttpSession session
			               , RedirectAttributes redirectAttributes) {

		System.out.println("UploadFileController fileUpload called...");

		System.out.println("file.getName()::" + file.getOriginalFilename());
		String name = file.getOriginalFilename();


		if (!file.isEmpty()) {
			try {
				System.out.println("UploadFileController fileUpload 33333333333333333333...");
				String randmeFileName = java.util.UUID.randomUUID().toString();
				User sessionUser = HttpSessionUtil.getUserFromSession(session);

				// user file folder create
				// -----------------------------------------------------------------------------------------------
				String userFolderLocation = "";
				if("private".equals(uploadType)){
					userFolderLocation = StarootApplication.UPLOAD_DIR_CKEDITOR + "/" + sessionUser.getUserId();
				}else if("public".equals(uploadType)){
					userFolderLocation = StarootApplication.UPLOAD_DIR_CKEDITOR + "/" + "public";
				}
				File theDir = new File(userFolderLocation);
				if (!theDir.exists()) {
					System.out.println("creating directory: " + theDir.getName());
					boolean result = false;
					try {
						theDir.mkdir();
						result = true;
					} catch (SecurityException se) {
					}
					if (result) {
						System.out.println("DIR created");
					}
				}
				// -----------------------------------------------------------------------------------------------

				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(userFolderLocation + "/" + randmeFileName)));
				FileCopyUtils.copy(file.getInputStream(), stream);
				stream.close();

				// save info to database
				// ----------------------------------------------------------------------
				UserFile userFile = null;

				userFile = new UserFile(sessionUser, name, randmeFileName, userFolderLocation,file.getSize(),uploadType);

				userFileRepository.save(userFile);
				// ----------------------------------------------------------------------

				System.out.println("UploadFileController fileUpload success????...");
				redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + name + "!");
			} catch (Exception e) {
				System.out.println("UploadFileController fileUpload Fail??..." + e.getMessage());
			}
		} else {
			System.out.println("UploadFileController fileUpload Fail??(22222)...");
		}

		System.out.println("UploadFileController fileUpload 4444444444444444444...");
		String redirectUrl = "";
		if ("private".equals(uploadType)){
			redirectUrl = "redirect:/ckeditor/file/userFileList";
		}else{
			redirectUrl = "redirect:/ckeditor/file/editorFileList";
		}
		return redirectUrl;
	}

	@RequestMapping(value = "/{uploadType}/download/{fileId:.+}")
	@ResponseBody
	public byte[] getFile(@PathVariable(value = "uploadType") String uploadType
			            , @PathVariable(value = "fileId") Long fileId
			            , HttpSession session
			            , HttpServletRequest request
			            , HttpServletResponse response) throws IOException {
		System.out.println("getFile().............");

		User sessionUser = HttpSessionUtil.getUserFromSession(session);
		String userFolderLocation = "";
		// user file folder create
		// -----------------------------------------------------------------------------------------------
		if("private".equals(uploadType)){
			userFolderLocation = StarootApplication.UPLOAD_DIR_CKEDITOR + "/" + sessionUser.getUserId();
		}else if ("public".equals(uploadType)){
			userFolderLocation = StarootApplication.UPLOAD_DIR_CKEDITOR + "/" + "public";
		}
		String fileUUIDNm = "";
		UserFile userFile = userFileRepository.findOne(fileId);
		if (userFile == null) {
			System.out.println("no file exists!");
		} else {
			//파일저장정보 존재시 해당 저장경로사용
			userFolderLocation = userFile.getFilePath();
			fileUUIDNm = userFile.getChngFileNm();
		}

		File serverFile = new File(userFolderLocation + "/" + fileUUIDNm);

		System.out.println("userFile.getOrigFileNm()::" + userFile.getOrigFileNm());
		String fileName = userFile.getOrigFileNm();

		// response.setHeader("Content-Disposition", "attachment;
		// filename=\""+origFileNm+"\"");

		String mime = request.getSession().getServletContext().getMimeType(serverFile.getName());
		if (mime == null || mime.length() == 0) {
			mime = "application/octet-stream;charset=euc-kr";
		}
		response.setContentType(mime);

		String userAgent = request.getHeader("User-Agent");
		if (userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
			response.setHeader("Content-Disposition",
					"filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
		} else if (userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
		} else if (userAgent.indexOf("Trident") > -1) { // MS IE 11
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
		} else { // 모질라나 오페라
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(fileName.getBytes("UTF-8"), "latin1").replaceAll("\\+", "\\ ") + ";");
		}
		if (serverFile.length() > 0) {
			response.setHeader("Content-Length", "" + serverFile.length());
		}

		return Files.readAllBytes(serverFile.toPath());
	}
	
	@RequestMapping(value = "/multi_upload_form", method = RequestMethod.GET)
	public String showMultiUploadForm() {
		return "/test/upload/multi_upload_form";
	}

	@RequestMapping(value = "/single_upload_form", method = RequestMethod.GET)
	public String singleUploadForm() {
		System.out.println("single_upload_form called!..");
		return "/test/upload/single_upload_form";
	}

	/**
	 *
	 * http://www.concretepage.com/spring-4/spring-4-mvc-single-multiple-file-upload-example-with-tomcat
	 */
	@RequestMapping(value = "/multi_upload", method = RequestMethod.POST)
	public @ResponseBody String multipleSave(@RequestParam("file") MultipartFile[] files) {
		String fileName = null;
		String msg = "";
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				try {
					if (true == files[i].isEmpty()) {
						continue;
					}

					fileName = files[i].getOriginalFilename();
					byte[] bytes = files[i].getBytes();
					BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(new File(StarootApplication.UPLOAD_DIR_CKEDITOR + "/" + fileName)));
					buffStream.write(bytes);
					buffStream.close();
					msg += "You have successfully uploaded " + fileName + "<br/>";
				} catch (Exception e) {
					return "You failed to upload " + fileName + ": " + e.getMessage() + "<br/>";
				}
			}
			return msg;
		} else {
			return "Unable to upload. File is empty.";
		}
	}

}
