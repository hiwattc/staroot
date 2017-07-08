package com.staroot.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

public class HbsTest4 {
	public static void main(String[] args) throws IOException {
		TemplateLoader loader = new ClassPathTemplateLoader();
		loader.setPrefix("/handlebars");
		loader.setSuffix(".html");
		Handlebars handlebars = new Handlebars(loader);
		Template template = handlebars.compile("handlebarTemplate3");
		//Template template = handlebars.compile("handlebarTemplate2");
		Map map = new HashMap();
		String jsonData = "[{price:1000}]";
		map.put("name", "starootmaster33");
		map.put("json", jsonData);

		List list = new ArrayList();
		List list2 = new ArrayList();
		Map map1 = new HashMap();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map map3 = new HashMap();
		map3.put("description", "제품설명입니다1 ");
		list2.add(map3);
		list2.add(map3);
		list2.add(map3);
		list2.add(map3);

		map1.put("name", "product1");
		map1.put("price", "1000");
		map1.put("features", list2);
		map2.put("name", "product2");
		map2.put("price", "2000");
		map2.put("features", list2);
		list.add(map1);
		list.add(map2);

		map.put("items", list);
		
		List userList = new ArrayList();
		map1.put("userid", "starootmaster1");
		map1.put("name", "seojungbae");
		map1.put("desc", "developer");
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		userList.add(map1);
		map.put("users", userList);
		
		
		System.out.println("11111111111");
		System.out.println("11111111111");
		System.out.println("11111111111");
		System.out.println("11111111111");
		System.out.println("11111111111");
		System.out.println("11111111111");

		
		String mailContent = template.apply(map);
		System.out.println(template.apply(map));
		MailSender mailSender = new MailSender();
		try {
			mailSender.sendMail(mailContent);
		} catch (Exception e) {
			System.out.println(e.getMessage()); 
		}
	}

}