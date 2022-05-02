package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.Constants.HTML_SRC_DIR;
import static com.osscameroon.jsgenerator.util.Constants.JS_DEST_DIR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

import com.osscameroon.jsgenerator.exception.HTMLUnknownElementException;
import com.osscameroon.jsgenerator.model.JsElement;
import com.osscameroon.jsgenerator.util.FileUtil;

/**
 * Provides an implementation of {@link ConvertService} interface.
 *
 * @author osscameroon
 *
 */
public class ConvertServiceImpl implements ConvertService {

    private static final Logger logger = Logger.getLogger(ConvertServiceImpl.class.getName());

    /**
     * Tags present in the document to convert
     */

    private List<String> usedTags = new ArrayList<>();

    /**
     * {@inheritDoc}
     *
     * @throws HTMLUnknownElementException
     */

    @Override
    public String convert(String content) throws HTMLUnknownElementException {
	Element htmlDoc = Jsoup.parse(content, "", Parser.xmlParser());

	/*
	 * trim() is added to delete leading and trailing space. Before adding that, the
	 * generated Js code contained these not important spaces. It was difficult to
	 * test this method.
	 */

	String result = parseElement(htmlDoc).trim();

	/*
	 * Without this line, the result of convert method with same parameter changed
	 * everytime if we used the same object to call this function. The result of
	 * convert with same parameter was constant if we used different objects. To
	 * avoid this issue, we were forced to create different objects but that's not
	 * how it should be done.
	 *
	 * Now, the program clears the list of used tags in order to always get an empty
	 * list when we call this method.
	 */
	usedTags.clear();

	return result;
    }

    /**
     * {@inheritDoc}
     *
     *
     */

    @Override
    public void convertHtmlFiletoJsFileFromCommandLineInterface(String htmlFileName)
	    throws HTMLUnknownElementException {

	// Use log instead of system.out.println to show steps

	/*
	 * If HTML_SRC_DIR folder doesn't exist then it will create them
	 *
	 * new File(HTML_SRC_DIR.getFolder()).createNewFile();
	 */

	logger.log(Level.INFO, " **** Converting " + htmlFileName + " to js file **** ");

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

	logger.log(Level.INFO, " **** Conversion complete **** ");

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
     * @throws HTMLUnknownElementException
     */

    private String parseElement(Element element) throws HTMLUnknownElementException {
	StringBuilder generatedCode = new StringBuilder();

	for (Element child : element.children()) {
	    generatedCode.append(parseElement(child)).append("\n"); // recursive

	    JsElement parent = new JsElement(child);

	    generatedCode.append(parse(usedTags, parent));

	    // parse this current element

	    String appends = appendChild(parent); // append this current element's children code to parent code

	    if (!appends.equals("")) {
		generatedCode.append(appends);
	    }
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
     * @throws HTMLUnknownElementException
     */

    private String parse(List<String> usedTags, JsElement jsElement) throws HTMLUnknownElementException {

	if (!jsElement.getElement().tag().isKnownTag()) {

	    throw new HTMLUnknownElementException(
		    "\"" + jsElement.getElement().tagName() + "\"" + " is not a valid HTML Element.");
	}

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

	/*
	 * It means that tag name should not contain _ TODO: Check that
	 */

	StringBuilder generatedCode = new StringBuilder(
		"var " + tag + " = document.createElement(\"" + tag.replace("_", "") + "\");\n");

	return addAttributeToElement(attributes, innerHTML, tag, generatedCode);
    }

    /*
     * TODO: We have to add the update info (date, author,...) everywhere when it is
     * needed
     */

    /**
     * Updated on April 30th, 2022 by Fanon Jupkwo
     */

    private String appendChild(JsElement jsElement) {

	StringBuilder generatedCode = new StringBuilder();

	/*
	 * Please, think twice before deleting this log, it helps to understand what is
	 * going on under the hood. It is really helpful to understand that there is a
	 * bug in Jsoup library.
	 *
	 * Let's take an example,
	 *
	 *
	 * <input type="text"> <img src="#URL" alt="image">
	 *
	 * ------------------------------------------------------
	 *
	 * <input type="text"/> <img src="#URL" alt="image">
	 *
	 * Jsoup considers <input> and <input/> as self closing tags.
	 *
	 * Consequently, they should not have children.
	 *
	 * But there is something weird with Jsoup, <input> could have children if there
	 * is another html tag close to its position, this behavior is incorrect.
	 * <input/> could not, this is correct.
	 *
	 * How could you verify that ? Just run the tests and look the logs.
	 *
	 * Here is the previous code with self closing tag issue :
	 * https://github.com/osscameroon/js-generator/tree/self-closing-tag-issue
	 *
	 * In order to correct that, we created a condition to test if the tag is self
	 * closing then we do nothing. If it is not self closing and if there are
	 * children, only then we append children.
	 *
	 * Before this change, the only condition was if the element has children. This
	 * is why we had self closing tag with the possibility of having children,
	 * that's completely wrong.
	 *
	 * Useful links:
	 *
	 * https://www.educba.com/types-of-tags-in-html/
	 *
	 * https://www.tutorialstonight.com/self-closing-tags-in-html.php#:~:text=HTML%
	 * 20Self%20Closing%20Tag,%2C%20etc.
	 *
	 */

	boolean hasChld = jsElement.getElement().childrenSize() > 0;

	logger.log(Level.INFO,
		" **** Analyze jsElement :" + jsElement.getElement().tag().getName() + " -> isEmpty : "
			+ jsElement.getElement().tag().isEmpty() + " -> isSelfClosing : "
			+ jsElement.getElement().tag().isSelfClosing() + " -> isKnown : "
			+ Tag.isKnownTag(jsElement.getElement().tagName().replace("_", "")) + " -> hasChild : "
			+ hasChld + " **** ");

	if (!jsElement.getElement().tag().isSelfClosing() && jsElement.getElement().children().size() > 0) {

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
	    if (!textNode.isBlank()) {
		generatedCode.append(tag).append(".appendChild(document.createTextNode(\"")
			.append(textNode.toString().replace("\n", "").trim()).append("\"));\n");
	    }
	}

	return generatedCode.toString();
    }

}
