package com.osscameroon.jsGenerator.model;

import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class JsElement {

	private Element element = null;

	public JsElement(Element element) {

		this.element = element;
	}

//	WITH USED TAGS LIST
	public String parse(List<String> usedTags) {
		// atrributes
		Attributes attributes = this.element.attributes();
		// text nodes
		List<TextNode> innerHTML = this.element.textNodes();

		// search tag name
		usedTags.stream().filter(s -> s.equals(this.element.tagName()))
				.forEach(s -> this.element.tagName(this.element.tagName() + "_"));

		// tag name
		String tag = this.element.tagName();
		usedTags.add(tag);

		// generation of code
		String generatedCode = "var " + tag + " = document.createElement(\"" + tag.replace("_", "") + "\");\n";

		for (Attribute attribute : attributes) {
			generatedCode += tag + ".setAttribute(\"" + attribute.getKey() + "\", \"" + attribute.getValue() + "\");\n";
		}

		for (TextNode textNode : innerHTML) {
			if (!textNode.isBlank())
				generatedCode += tag + ".appendChild(document.createTextNode(\""
						+ textNode.toString().replace("\n", "").trim() + "\"));\n";
		}

		return generatedCode;

	}

//	WITHOUT USED TAGS LIST
	public String parse() {
		// atrributes
		Attributes attributes = this.element.attributes();
		// text nodes
		List<TextNode> innerHTML = this.element.textNodes();
		// tag name
		String tag = this.element.tagName();

		// generation of code
		String generatedCode = "var " + tag + " = document.createElement(\"" + tag + "\");\n";

		for (Attribute attribute : attributes) {
			generatedCode += tag + ".setAttribute(\"" + attribute.getKey() + "\", \"" + attribute.getValue() + "\");\n";
		}

		for (TextNode textNode : innerHTML) {
			if (!textNode.isBlank())
				generatedCode += tag + ".appendChild(document.createTextNode(\""
						+ textNode.toString().replace("\n", "").trim() + "\"));\n";
		}

		return generatedCode;

	}

	public String appendChild() {
		String generatedCode = new String();

		if (this.element.children().size() > 0) {
			for (Element child : this.element.children()) {
				generatedCode += this.element.tagName() + ".appendChild(" + child.tagName() + ");\n";
			}
		}

		return generatedCode;
	}

}
