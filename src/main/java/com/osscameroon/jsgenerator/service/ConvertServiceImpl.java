package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.Constants.HTML_SRC_DIR;
import static com.osscameroon.jsgenerator.util.Constants.JS_DEST_DIR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import com.osscameroon.jsgenerator.model.JsElement;
import com.osscameroon.jsgenerator.util.FileUtil;

/**
 * Provides an implementation of {@link ConvertService} interface.
 *
 * @author osscameroon
 *
 */
public class ConvertServiceImpl implements ConvertService {

	private List<String> usedTags = new ArrayList<>();

	/**
	 * {@inheritDoc}
	 */

	@Override
	public String convert(String content) {
		Element htmlDoc = Jsoup.parse(content, "", Parser.xmlParser());

		/*
		 * trim() is added to delete leading and trailing space. Before adding that, the
		 * generated Js code contained these not important spaces. It was difficult to
		 * test this method.
		 */

		return parseElement(htmlDoc).trim();
	}

	/**
	 * {@inheritDoc}
	 *
	 */

	@Override
	public void convertHtmlFiletoJsFileFromCommandLineInterface(String htmlFileName) {

		// Use log instead of system.out.println to show steps

		/*
		 * If HTML_SRC_DIR folder doesn't exist then it will create them
		 *
		 * new File(HTML_SRC_DIR.getFolder()).createNewFile();
		 */

		System.out.println(" **** Converting " + htmlFileName + " to js file **** ");

		// get the full supposed path to the html file

		String pathToHtml = HTML_SRC_DIR.getFolder().concat(htmlFileName);

		/*
		 * By default, the input folder exists but the output folder don't. So, if
		 * JS_DEST_DIR folder doesn't exist then it will be created in order to receive
		 * generated Js files. If this output folder doesn't exist when this method is
		 * called, an exception will be thrown.
		 */

		File outputFolder = new File(JS_DEST_DIR.getFolder());

		if (outputFolder.exists() && outputFolder.isDirectory()) {

		} else {

			outputFolder.mkdir();
		}

		// get the full supposed path to the js file

		String jsFilePath = JS_DEST_DIR.getFolder().concat(htmlFileName.split(".html")[0] + ".js");

		String htmlContent = FileUtil.readHtmlFile(pathToHtml).toString();

		String jsContent = convert(htmlContent);

		FileUtil.writeJsFile(jsContent, jsFilePath);

		System.out.println(" **** Conversion complete **** ");
	}

	/**
	 * {@inheritDoc}
	 *
	 *
	 */

	// Will be implemented soon

	@Override
	public void convertHtmlFiletoJsFileFromWeb(String htmlFileName, String outputFolder) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Goes through the Jsoup Elements and converts them to JsElement objects.
	 *
	 * @param element Jsoup Element
	 * @return the generated code in JS
	 */

	private String parseElement(Element element) {
		StringBuilder generatedCode = new StringBuilder();

		for (Element child : element.children()) {
			generatedCode.append(parseElement(child)).append("\n"); // recursive

			JsElement parent = new JsElement(child);

			generatedCode.append(parse(usedTags, parent)); // parse this current element

			String appends = appendChild(parent); // append this current element's children code to parent code

			if (!appends.equals(""))
				generatedCode.append(appends);
		}
		return generatedCode.toString();
	}

//	WITH USED TAGS LIST

	/**
	 * For this element, it returns the code to append the element to the parent
	 *
	 * @param usedTags  List of used tags in the document
	 * @param jsElement
	 * @return code to append the element to the parent
	 */

	private String parse(List<String> usedTags, JsElement jsElement) {
		// attributes: attributes of the element
		Attributes attributes = jsElement.getElement().attributes();

		// text nodes: text nodes of the element (content in between tags)
		List<TextNode> innerHTML = jsElement.getElement().textNodes();

		// search tag name among used tags
		usedTags.stream().filter(s -> s.equals(jsElement.getElement().tagName()))
				.forEach(s -> jsElement.getElement().tagName(jsElement.getElement().tagName() + "_"));

		// tag name
		String tag = jsElement.getElement().tagName();
		usedTags.add(tag);

		StringBuilder generatedCode;

		// This block is for self-closing tags
		// TODO Review this with Fanon and implement accordingly
		/*
		 * if(jsElement.element.tag().isSelfClosing()) {
		 *
		 * generatedCode = new StringBuilder("var " + tag +
		 * " = document.createElement(\"" + tag + "\");\n"); return
		 * addAttributeToElement(jsElement.element.attributes(), List.of(),
		 * jsElement.element.tagName(), generatedCode);
		 *
		 * }
		 */

		// generation of code
		generatedCode = new StringBuilder(
				"var " + tag + " = document.createElement(\"" + tag.replace("_", "") + "\");\n");

		return addAttributeToElement(attributes, innerHTML, tag, generatedCode);
	}

	// WITHOUT USED TAGS LIST
	private String parse(JsElement jsElement) {
		// attributes
		Attributes attributes = jsElement.getElement().attributes();
		// text nodes
		List<TextNode> innerHTML = jsElement.getElement().textNodes();
		// tag name
		String tag = jsElement.getElement().tagName();

		// generation of code
		StringBuilder generatedCode = new StringBuilder("var " + tag + " = document.createElement(\"" + tag + "\");\n");

		return addAttributeToElement(attributes, innerHTML, tag, generatedCode);

	}

	private String appendChild(JsElement jsElement) {
		StringBuilder generatedCode = new StringBuilder();

		if (jsElement.getElement().children().size() > 0) {
			for (Element child : jsElement.getElement().children()) {
				generatedCode.append(jsElement.getElement().tagName()).append(".appendChild(").append(child.tagName())
						.append(");\n");
			}
		}

		return generatedCode.toString();
	}

	/**
	 * Given an element, it adds the attributes to the element
	 *
	 * @param attributes    Attributes of the element
	 * @param innerHTML     Text nodes of the element
	 * @param tag           Tag name of the element
	 * @param generatedCode Code generated in JS
	 * @return generated code for the element
	 */
	private String addAttributeToElement(Attributes attributes, List<TextNode> innerHTML, String tag,
			StringBuilder generatedCode) {
		for (Attribute attribute : attributes) {
			generatedCode.append(tag).append(".setAttribute(\"").append(attribute.getKey()).append("\", \"")
					.append(attribute.getValue()).append("\");\n");
		}

		for (TextNode textNode : innerHTML) {
			if (!textNode.isBlank())
				generatedCode.append(tag).append(".appendChild(document.createTextNode(\"")
						.append(textNode.toString().replace("\n", "").trim()).append("\"));\n");
		}

		return generatedCode.toString();
	}

}
