package com.staroot.mail;

import java.io.StringWriter;
import java.util.HashMap;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class MustacheJavaTest2 {
	public static void main(String[] args) {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = mf.compile("test.mustache");

		StringWriter writer = new StringWriter();
		HashMap<String, String> map = new HashMap<>();
		map.put("userid", "starootmaster");
		map.put("name", "ACTIVE");

		template.execute(writer, map);

		System.out.println(writer.toString());

	}
}