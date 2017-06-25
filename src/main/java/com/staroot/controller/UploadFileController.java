package com.staroot.controller;

import com.staroot.StarootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/board/file")
public class UploadFileController {
    @RequestMapping(method = RequestMethod.GET, value = "/files")
    public String provideUploadInfo(Model model) {
        File rootFolder = new File(StarootApplication.UPLOAD_DIR);

        List<String> fileNames = Arrays.stream(rootFolder.listFiles())
                .map(f -> f.getName())
                .collect(Collectors.toList());

        model.addAttribute("files",
                Arrays.stream(rootFolder.listFiles())
                        .sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
                        .map(f -> f.getName())
                        .collect(Collectors.toList())
        );

        return "/test/upload/upload_result";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/single_upload")
    public String fileUpload(@RequestParam("name") String name,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
    	
    	System.out.println("UploadFileController fileUpload called...");
    	
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
            return "redirect:/board/file/files";
        }
    	System.out.println("UploadFileController fileUpload 1111111111111111111111...");
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
            return "redirect:/board/file/files";
        }
    	System.out.println("UploadFileController fileUpload 2222222222222222222222...");

        if (!file.isEmpty()) {
            try {
            	System.out.println("UploadFileController fileUpload 33333333333333333333...");

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File(StarootApplication.UPLOAD_DIR + "/" + name)));
                FileCopyUtils.copy(file.getInputStream(), stream);
                stream.close();
            	System.out.println("UploadFileController fileUpload success????...");
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + name + "!");
            }
            catch (Exception e) {
            	System.out.println("UploadFileController fileUpload Fail??..."+e.getMessage());
                redirectAttributes.addFlashAttribute("message",
                        "You failed to upload " + name + " => " + e.getMessage());
            }
        }
        else {
        	System.out.println("UploadFileController fileUpload Fail??(22222)...");
            redirectAttributes.addFlashAttribute("message",
                    "You failed to upload " + name + " because the file was empty");
        }

    	System.out.println("UploadFileController fileUpload 4444444444444444444...");
        return "redirect:/board/file/files";
    }

    @RequestMapping(value="/multi_upload_form", method=RequestMethod.GET)
    public String showMultiUploadForm() {
        return "/test/upload/multi_upload_form";
    }

    @RequestMapping(value="/single_upload_form", method = RequestMethod.GET)
    public String singleUploadForm() {
        System.out.println("single_upload_form called!..");
        return "/test/upload/single_upload_form";
    }

    /**
     *
     * http://www.concretepage.com/spring-4/spring-4-mvc-single-multiple-file-upload-example-with-tomcat
     */
    @RequestMapping(value="/multi_upload", method=RequestMethod.POST )
    public @ResponseBody
    String multipleSave(@RequestParam("file") MultipartFile[] files){
        String fileName = null;
        String msg = "";
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    if (true == files[i].isEmpty()) {
                        continue;
                    }

                    fileName = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(StarootApplication.UPLOAD_DIR + "/" + fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName +"<br/>";
                } catch (Exception e) {
                    return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            return msg;
        } else {
            return "Unable to upload. File is empty.";
        }
    }
}

