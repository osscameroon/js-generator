package com.osscameroon.jsGenerator.model;

import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

/**
 * @author osscameroon
 * @version 1.0
 * Represents a JS Element along with methods
 */
public class JsElement {

	private final Element element;

	public JsElement(Element element) {

		this.element = element;
	}

//	WITH USED TAGS LIST

	/**
	 * For this element, it returns the code to append the element to the parent
	 * @param usedTags List of used tags in the document
	 * @return code to append the element to the parent
	 */
	public String parse(List<String> usedTags) {
		// attributes: attributes of the element
		Attributes attributes = this.element.attributes();

		// text nodes: text nodes of the element (content in between tags)
		List<TextNode> innerHTML = this.element.textNodes();

		// search tag name among used tags
		usedTags.stream()
				.filter(s -> s.equals(this.element.tagName()))
				.forEach(s -> this.element.tagName(this.element.tagName() + "_"));

		// tag name
		String tag = this.element.tagName();
		usedTags.add(tag);

		StringBuilder generatedCode;

		// This block is for self-closing tags
		// TODO Review this with Fanon and implement accordingly
		/*
		if(this.element.tag().isSelfClosing()) {

			generatedCode = new StringBuilder("var " + tag + " = document.createElement(\"" + tag + "\");\n");
			return addAttributeToElement(this.element.attributes(), List.of(), this.element.tagName(), generatedCode);

		}
		 */

		// generation of code
		generatedCode = new StringBuilder("var " + tag + " = document.createElement(\"" + tag.replace("_", "") + "\");\n");

		return addAttributeToElement(attributes, innerHTML, tag, generatedCode);
	}

	//	WITHOUT USED TAGS LIST
	public String parse() {
		// attributes
		Attributes attributes = this.element.attributes();
		// text nodes
		List<TextNode> innerHTML = this.element.textNodes();
		// tag name
		String tag = this.element.tagName();

		// generation of code
		StringBuilder generatedCode = new StringBuilder("var " + tag + " = document.createElement(\"" + tag + "\");\n");

		return addAttributeToElement(attributes, innerHTML, tag, generatedCode);

	}

	/**
	 * Given an element, it adds the attributes to the element
	 * @param attributes Attributes of the element
	 * @param innerHTML Text nodes of the element
	 * @param tag Tag name of the element
	 * @param generatedCode Code generated in JS
	 * @return generated code for the element
	 */
	private String addAttributeToElement(Attributes attributes, List<TextNode> innerHTML, String tag, StringBuilder generatedCode) {
		for (Attribute attribute : attributes) {
			generatedCode.append(tag).append(".setAttribute(\"").append(attribute.getKey()).append("\", \"").append(attribute.getValue()).append("\");\n");
		}

		for (TextNode textNode : innerHTML) {
			if (!textNode.isBlank())
				generatedCode.append(tag).append(".appendChild(document.createTextNode(\"").append(textNode.toString().replace("\n", "").trim()).append("\"));\n");
		}

		return generatedCode.toString();
	}

	public String appendChild() {
		StringBuilder generatedCode = new StringBuilder();

		if (this.element.children().size() > 0) {
			for (Element child : this.element.children()) {
				generatedCode.append(this.element.tagName()).append(".appendChild(").append(child.tagName()).append(");\n");
			}
		}

		return generatedCode.toString();
	}
}
