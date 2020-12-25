package com.osscameroon.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.osscameroon.model.JsElement;

public class JsElementService {

	private static List<String> usedTags = new ArrayList<>();

	public static String parseElement(Element element) {
		String generatedCode = new String();

		for (Element child : element.children()) {
			generatedCode += parseElement(child) + "\n";
			JsElement parent = new JsElement(child);
			generatedCode += parent.parse(usedTags);
			String appends = parent.appendChild();
			if (!appends.equals(new String()))
				generatedCode += appends;
		}

		return generatedCode;
	}

}
