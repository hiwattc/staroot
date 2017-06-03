package com.staroot.mail;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class MustacheJavaTest1 {

	Map items() {
	Map map = new HashMap();
	List list = new ArrayList();
	list = Arrays.asList(
		      new Item("Item 1", "$19.99", Arrays.asList(new Feature("New!"), new Feature("Awesome!"))),
		      new Item("Item 2", "$29.99", Arrays.asList(new Feature("Old."), new Feature("Ugly.")))
		    );	
	map.put("items", list);
	map.put("name","starootmaster");
	map.put("price","starootmaster");
    return map;
  }
	
	/*
	 * List<Item> items() { return Arrays.asList( new Item("Item 1", "$19.99",
	 * Arrays.asList(new Feature("New!"), new Feature("Awesome!"))), new
	 * Item("Item 2", "$29.99", Arrays.asList(new Feature("Old."), new
	 * Feature("Ugly."))), new Item("Item 2", "$29.99", Arrays.asList(new
	 * Feature("Old."), new Feature("Ugly."))), new Item("Item 2", "$29.99",
	 * Arrays.asList(new Feature("Old."), new Feature("Ugly."))), new
	 * Item("Item 2", "$29.99", Arrays.asList(new Feature("Old."), new
	 * Feature("Ugly."))) ); }
	 */
	static class Item {
		Item(String name, String price, List<Feature> features) {
			this.name = name;
			this.price = price;
			this.features = features;
		}

		String name, price;
		List<Feature> features;
	}

	static class Feature {
		Feature(String description) {
			this.description = description;
		}

		String description;
	}

	public static void main(String[] args) throws IOException {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("template2.mustache");
		mustache.execute(new PrintWriter(System.out), new MustacheJavaTest1()).flush();
	}
}