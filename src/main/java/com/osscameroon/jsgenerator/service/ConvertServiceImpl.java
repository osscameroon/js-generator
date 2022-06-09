package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.Constants.HTML_SRC_DIR;
import static com.osscameroon.jsgenerator.util.Constants.JS_DEST_DIR;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

import com.osscameroon.jsgenerator.exception.DuplicatedHTMLFileNameException;
import com.osscameroon.jsgenerator.exception.EmptyHTMLFilesListException;
import com.osscameroon.jsgenerator.exception.HTMLFileNotFoundException;
import com.osscameroon.jsgenerator.exception.HTMLUnknownElementException;
import com.osscameroon.jsgenerator.exception.IncorrectHTMLFileNameException;
import com.osscameroon.jsgenerator.exception.NoHTMLCodeException;
import com.osscameroon.jsgenerator.exception.NoHTMLFileNameException;
import com.osscameroon.jsgenerator.model.JsElement;
import com.osscameroon.jsgenerator.model.JsVariableDeclaration;
import com.osscameroon.jsgenerator.util.FileUtil;

/**
 * Provides an implementation of {@link ConvertService} interface.
 *
 * @author Fanon Jupkwo
 * @author Elroy Kanye
 *
 */
public class ConvertServiceImpl implements ConvertService {

    private static final Logger logger = Logger.getLogger(ConvertServiceImpl.class.getName());

    private JsVariableDeclaration jsVariableDeclaration;

    public ConvertServiceImpl(JsVariableDeclaration jsVariableDeclaration) {

	this.jsVariableDeclaration = jsVariableDeclaration;

    }

    /**
     * Tags present in the document to convert
     */

    private List<String> usedTags = new ArrayList<>();

    /**
     * {@inheritDoc}
     *
     *
     */

    @Override
    public String convert(String content) throws NoHTMLCodeException {

	if (content == null) {

	    throw new NoHTMLCodeException("There is no html content.");
	}

	if (content.isBlank()) {

	    throw new NoHTMLCodeException("The content has nothing to translate.");

	}

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
	    throws NoHTMLFileNameException, IncorrectHTMLFileNameException, HTMLFileNotFoundException {

	if (htmlFileName == null) {

	    throw new NoHTMLFileNameException("There is no Html file name.");
	}

	if (!isHTMLFileNameCorrect(htmlFileName)) {

	    throw new IncorrectHTMLFileNameException("The HTML file's name \"" + htmlFileName + "\" is incorrect.");
	}

	// get the full supposed path to the html file

	String pathToHtml = HTML_SRC_DIR.getFolder().concat(htmlFileName);

	File htmlFile = new File(pathToHtml);

	if (!htmlFile.exists()) {

	    throw new HTMLFileNotFoundException("Html file \"" + htmlFileName + "\" not found");
	}

	// Use log instead of system.out.println to show steps

	/*
	 * If HTML_SRC_DIR folder doesn't exist then it will create them
	 *
	 * new File(HTML_SRC_DIR.getFolder()).createNewFile();
	 */

	logger.log(Level.INFO, " **** Converting " + htmlFileName + " to js file **** ");

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

    @Override
    public void convertHtmlFiletoJsFileFromCommandLineInterface(String[] args)
	    throws NoHTMLFileNameException, EmptyHTMLFilesListException, DuplicatedHTMLFileNameException {

	if (args == null) {

	    throw new NoHTMLFileNameException("There is no list of Html files.");

	}

	if (args.length == 0) {

	    throw new EmptyHTMLFilesListException("The list of Html files to translate is empty");
	}

	List<String> argList = Arrays.asList(args);

	// TODO: verify that all files are named correctly with .html, create a method
	// in ConvertService

	// throw exception because there are list 2 files with same name
	// inform the user and work with unique file names

	argList.stream().forEach(s -> {

	    if (Collections.frequency(argList, s) == 1) {

		convertHtmlFiletoJsFileFromCommandLineInterface(s);

	    } else {

		// TODO: throw exception because there are 2 files with same name

		logger.log(Level.INFO, "There are at least 2 files with same name on CLI : " + s);

		throw new DuplicatedHTMLFileNameException("There are at least 2 files with same name on CLI : " + s);

	    }
	});

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
     * @throws HTMLUnknownElementException if an invalid HTML tag is used
     *
     */

    private String parseElement(Element element) throws HTMLUnknownElementException {

	logger.log(Level.INFO, " **** METHOD -- parseElement(Element element) -- Analyzing element : tagName = "
		+ element.tagName() + " -> " + element + "\n" + "---------------" + "\n");

	/*
	 * If the element is not the root and is unknown then there is a problem.
	 * Without the first condition "!element.root().equals(element)", there will be
	 * an exception thrown if the element is the root
	 */

	if (!element.root().equals(element) && !element.tag().isKnownTag()) {

	    throw new HTMLUnknownElementException(
		    "\"" + element + " -> " + element.tagName() + "\"" + " is not a valid HTML Element.");
	}

	StringBuilder generatedCode = new StringBuilder();

	for (Element child : element.children()) {
	    generatedCode.append(parseElement(child)).append("\n"); // recursive

	    JsElement childJsElement = new JsElement(child, jsVariableDeclaration);

	    generatedCode.append(parse(usedTags, childJsElement));

	    // parse this current element

	    String appends = appendChild(childJsElement); // append this current element's children code to parent code

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
     * @throws HTMLUnknownElementException if an invalid HTML tag is used
     */

    private String parse(List<String> usedTags, JsElement jsElement) throws HTMLUnknownElementException {

	if (!jsElement.getElement().tag().isKnownTag()) {

	    throw new HTMLUnknownElementException(
		    "\"" + jsElement.getElement().tagName() + "\"" + " is not a valid HTML Element.");
	}

	// search tag name among used tags
	usedTags.stream().filter(s -> s.equals(jsElement.getElement().tagName()))
		.forEach(s -> jsElement.getElement().tagName(jsElement.getElement().tagName() + "_"));

	// tag name
	String tag = jsElement.getElement().tagName();

	usedTags.add(tag);

	/*
	 * Tag name should not contain _ TODO: Check that
	 */

	StringBuilder generatedCode = new StringBuilder(jsElement.getJsVariableDeclaration().getKeyword() + " " + tag
		+ " = document.createElement(\"" + tag.replace("_", "") + "\");\n");

	// attributes: attributes of the element
	Attributes attributes = jsElement.getElement().attributes();

	// Given an element, it adds the attributes to the element

	for (Attribute attribute : attributes) {
	    generatedCode.append(tag).append(".setAttribute(\"").append(attribute.getKey()).append("\", \"")
		    .append(attribute.getValue()).append("\");\n");
	}

	return generatedCode.toString();
    }

    /*
     * TODO: We have to add the update info (date, author,...) everywhere when it is
     * needed
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

	logger.log(Level.INFO, " **** METHOD -- appendChild(JsElement jsElement) --  Analyzing jsElement :"
		+ jsElement.getElement().tag().getName() + " -> isEmpty : " + jsElement.getElement().tag().isEmpty()
		+ " -> isSelfClosing : " + jsElement.getElement().tag().isSelfClosing() + " -> isKnown : "
		+ Tag.isKnownTag(jsElement.getElement().tagName().replace("_", "")) + " -> hasChild : " + hasChld
		+ " **** " + "\n" + "---------------" + "\n");

	logger.log(Level.INFO,
		" **** METHOD -- appendChild(JsElement jsElement) --  Analyzing jsElement childNodes : tagName = "
			+ jsElement.getElement().tagName() + " -> " + jsElement.getElement()
			+ " ->  List of child Nodes -> " + jsElement.getElement().childNodes() + "\n"
			+ "---------------" + "\n");

	logger.log(Level.INFO,
		" **** METHOD -- appendChild(JsElement jsElement) --  Analyzing jsElement children : tagName = "
			+ jsElement.getElement().tagName() + " -> " + jsElement.getElement()
			+ " ->  List of children -> " + jsElement.getElement().children() + "\n" + "---------------"
			+ "\n");

	/*
	 *
	 * if (!jsElement.getElement().tag().isSelfClosing()) {
	 *
	 * if (jsElement.getElement().children().size() > 0) {
	 *
	 * for (Element child : jsElement.getElement().children()) {
	 *
	 * generatedCode.append(jsElement.getElement().tagName()).append(
	 * ".appendChild(") .append(child.tagName()).append(");\n"); }
	 *
	 * }
	 *
	 * }
	 *
	 */

	// tag name
	String tag = jsElement.getElement().tagName();

	// If the tag is not self closing

	if (!jsElement.getElement().tag().isSelfClosing()) {

	    if (jsElement.getElement().childNodes().size() > 0) {

		for (Node childNode : jsElement.getElement().childNodes()) {

		    if (childNode instanceof Element) {

			Element childElement = (Element) childNode;

			generatedCode.append(jsElement.getElement().tagName()).append(".appendChild(")
				.append(childElement.tagName()).append(");\n");

		    }

		    // text nodes: text nodes of the element (content in between tags)

		    if (childNode instanceof TextNode) {

			TextNode textNode = (TextNode) childNode;

			if (!textNode.isBlank()) {
			    generatedCode.append(tag).append(".appendChild(document.createTextNode(\"")
				    .append(textNode.toString().replace("\n", "").trim()).append("\"));\n");
			}

		    }

		}

	    }

	}

	return generatedCode.toString();
    }

    /**
     * Given an html file's name, it verifies if the name is correct or not
     *
     * @param htmlFileName HTML file's name
     *
     * @return true if the name is correct; false otherwise
     */

    /*
     * https://www.javacodeexamples.com/java-regex-validate-file-name-extension/3504
     *
     * - Start of the string [a-zA-Z0-9._-] - Any character between a to z or A to
     * Z, any digit between 0 to 9, a dot, an underscore, a hyphen One or more times
     * - Followed by a . (html) - "html" - End of the string
     *
     * .html FALSE / test FALSE
     *
     */

    protected boolean isHTMLFileNameCorrect(String htmlFileName) {

	String regex = "^[a-zA-Z0-9._-]+\\.(html)$";

	return htmlFileName.matches(regex);
    }

}
