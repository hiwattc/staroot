package com.staroot.mail;

import java.io.IOException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

public class HbsTest2 {
	public static void main(String[] args) throws IOException {
		Handlebars handlebars = new Handlebars();
		Template template = handlebars.compile("template1");
		System.out.println(template.apply("Handlebars.java"));	
		}
}