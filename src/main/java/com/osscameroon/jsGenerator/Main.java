/*
 * 
 */
package com.osscameroon.jsGenerator;

import com.osscameroon.jsGenerator.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.osscameroon.jsGenerator.service.JsElementService;

/**
 * @author osscameroon
 * @version 1.0
 * @since 1.0
 * Main class
 */
public class Main {
	public static void main(String[] args) {
		String htmlFile;

		if(args.length == 0) htmlFile = "sample.html";
		else htmlFile = args[0];

		System.out.println("Converting " + htmlFile + " to js file");
		convertFiles(htmlFile);
		System.out.println("Conversion complete");
	}

	public static void fanonMain() {
		// TODO Auto-generated method stub

		String sampleHtml = "<!-- Button trigger modal -->\n" +
				"<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#exampleModal\">\n" +
				"  Launch demo modal\n" +
				"</button>\n" +
				"\n" +
				"<!-- Modal -->\n" +
				"<div class=\"modal fade\" id=\"exampleModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
				"  <div class=\"modal-dialog\" role=\"document\">\n" +
				"    <div class=\"modal-content\">\n" +
				"      <div class=\"modal-header\">\n" +
				"        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Modal title</h5>\n" +
				"        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
				"          <span aria-hidden=\"true\">&times;</span>\n" +
				"        </button>\n" +
				"      </div>\n" +
				"      <div class=\"modal-body\">\n" +
				"        ...\n" +
				"      </div>\n" +
				"      <div class=\"modal-footer\">\n" +
				"        <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Close</button>\n" +
				"        <button type=\"button\" class=\"btn btn-primary\">Save changes</button>\n" +
				"      </div>\n" +
				"    </div>\n" +
				"  </div>\n" +
				"</div>";

		convert(sampleHtml);

	}

	/**
	 * Converts the html string to js string and prints it out.
	 * @param content the html string
	 */
	public static void convert(String content) {
		Element doc = Jsoup.parse(content, "", Parser.xmlParser());
		System.out.println(JsElementService.parseElement(doc));
	}

	/**
	 * Converts the html file to js file
	 * @param pathToHtml the path to the html file
	 */
	public static void convertFiles(String pathToHtml) {
		String jsFileName = pathToHtml.split(".html")[0] + ".js";

		String htmlContent = FileUtil.readHtmlFile(pathToHtml).toString();
		Element htmlDoc = Jsoup.parse(htmlContent, "", Parser.xmlParser());


		String jsContent = JsElementService.parseElement(htmlDoc);
		FileUtil.writeJsFile(jsContent, jsFileName);



	}
}
