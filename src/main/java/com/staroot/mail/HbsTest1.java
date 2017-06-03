package com.staroot.mail;

import java.io.IOException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

public class HbsTest1 {
	public static void main(String[] args) throws IOException {
		Handlebars handlebars = new Handlebars();
		Template template = handlebars.compileInline("Hello {{this}}!");
		System.out.println(template.apply("Handlebars.java"));
	}
}