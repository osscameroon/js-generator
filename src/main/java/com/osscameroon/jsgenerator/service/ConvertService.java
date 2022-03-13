package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.Constants.HTML_SRC_DIR;
import static com.osscameroon.jsgenerator.util.Constants.JS_DEST_DIR;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.osscameroon.jsgenerator.util.FileUtil;

/**
 * Provides the convertion logic from Html to Js.
 *
 * @author osscameroon
 *
 */
public class ConvertService {

	/**
	 * Converts the Html string to Js string and prints it out.
	 *
	 * @param content the Html string
	 */
	private static void convert(String content) {
		Element doc = Jsoup.parse(content, "", Parser.xmlParser());
		System.out.println(" **** generated js:  **** ");
		System.out.println(JsElementService.parseElement(doc));
	}

	/**
	 * Converts the Html file already located in
	 * "src/main/resources/htmlFilesInput/" to Js file generated in
	 * "src/main/resources/jsFilesOutput/" folder.
	 *
	 * @param htmlFileName the Html file name
	 */

	public static void convertHtmlFiletoJsFile(String htmlFileName) {

		/*
		 * If HTML_SRC_DIR folder doesn't exist then it will create them
		 *
		 * new File(HTML_SRC_DIR.getFolder()).createNewFile();
		 */

		System.out.println(" **** Converting " + htmlFileName + " to js file **** ");

		// get the full supposed path to the html file

		String pathToHtml = HTML_SRC_DIR.getFolder().concat(htmlFileName);

		// get the full supposed path to the js file

		String jsFilePath = JS_DEST_DIR.getFolder().concat(htmlFileName.split(".html")[0] + ".js");

		String htmlContent = FileUtil.readHtmlFile(pathToHtml).toString();
		Element htmlDoc = Jsoup.parse(htmlContent, "", Parser.xmlParser());

		String jsContent = JsElementService.parseElement(htmlDoc);
		FileUtil.writeJsFile(jsContent, jsFilePath);

		System.out.println(" **** Conversion complete **** ");
	}

	/**
	 * Converts a built-in code from Html to Js and prints the result. This method
	 * calls {@link #convert(String)} to convert the built-in code.
	 *
	 */

	public static void convertAndPrintBuiltInCodeFromHtmlToJs() {
		// TODO Auto-generated method stub

		System.out.println("\n" + " **** Converting built-in code from html to js **** ");
		System.out.println(" **** Html to convert:  **** " + "\n");

		StringBuilder sampleHtml = new StringBuilder();

		sampleHtml.append("<!-- Button trigger modal -->\n").append(
				"<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#exampleModal\">\n")
				.append("  Launch demo modal\n").append("</button>\n").append("\n").append("<!-- Modal -->\n")
				.append("<div class=\"modal fade\" id=\"exampleModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n")
				.append("  <div class=\"modal-dialog\" role=\"document\">\n")
				.append("    <div class=\"modal-content\">\n").append("      <div class=\"modal-header\">\n")
				.append("        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Modal title</h5>\n")
				.append("        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n")
				.append("          <span aria-hidden=\"true\">&times;</span>\n").append("        </button>\n")
				.append("      </div>\n").append("      <div class=\"modal-body\">\n").append("        ...\n")
				.append("      </div>\n").append("      <div class=\"modal-footer\">\n")
				.append("        <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Close</button>\n")
				.append("        <button type=\"button\" class=\"btn btn-primary\">Save changes</button>\n")
				.append("      </div>\n").append("    </div>\n").append("  </div>\n").append("</div>");

		System.out.println(sampleHtml + "\n");

		convert(sampleHtml.toString());

	}

}
