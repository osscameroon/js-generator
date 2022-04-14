package com.osscameroon.jsgenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.osscameroon.jsgenerator.service.ConvertService;
import com.osscameroon.jsgenerator.service.ConvertServiceImpl;

/**
 * Main class responsible to launch the app.
 *
 * @author osscameroon
 */
public class Main {

	private static final Logger logger = Logger.getLogger(Main.class.getName());

	static ConvertService convertService = new ConvertServiceImpl();

	// Should I present the 3 key features here ?

	/**
	 * <p>
	 * This method launches the app.
	 * </p>
	 * <p>
	 * First, if there are no arguments, then the app will convert a built-in Html
	 * file "src/main/resources/htmlFilesInput/sample.html" to a Js file. Else, the
	 * app will convert all arguments representing Html files. <b>The program
	 * supposes that these Html files are already located in
	 * "src/main/resources/htmlFilesInput/" folder, so make sure to put the html
	 * file to convert into that folder. If this input folder doesn't exist on a
	 * project using our library jsgenerator then it will be created. The generated
	 * Js files will be located in "src/main/resources/jsFilesOutput/" folder.
	 * Concerning this library, by default, the input folder exists but the output
	 * folder don't. When a project will use jsgenerator as dependency or plugin, an
	 * input folder will be created as soon as possible its classpath will contain
	 * jsgenerator.</b> The method
	 * {@link com.osscameroon.jsgenerator.service.ConvertService#convertHtmlFiletoJsFileFromCommandLineInterface(String)}
	 * is responsible to convert the Html to Js file.
	 * </p>
	 * <p>
	 * Then, the method
	 * {@link com.osscameroon.jsgenerator.Main#convertAndPrintBuiltInCodeFromHtmlToJs()}
	 * converts a built-in code from Html to Js and prints the result.
	 * </p>
	 *
	 * @param args the arguments from command line
	 */
	public static void main(String[] args) {

		if (args.length == 0)

			convertService.convertHtmlFiletoJsFileFromCommandLineInterface("sample.html");

		else {

			List<String> argList = Arrays.asList(args);

			// TODO: verify that all files are named correctly with .html, create a method
			// in ConvertService

			// throw exception because there are list 2 files with same name
			// inform the user and work with unique file names

			argList.stream().forEach(s -> {

				if (Collections.frequency(argList, s) == 1) {

					convertService.convertHtmlFiletoJsFileFromCommandLineInterface(s);

				} else {

					// TODO: throw exception because there are 2 files with same name

					logger.log(Level.INFO, "There are at least 2 files with same name on CLI : " + s);

				}
			});

		}

		// The final release will not contain this line, TODO: create an example branch

		convertAndPrintBuiltInCodeFromHtmlToJs();

	}

	/**
	 * Converts a built-in code from Html to Js and prints the result.
	 *
	 */
	static void convertAndPrintBuiltInCodeFromHtmlToJs() {

		// Use log instead of system.out.println to show steps
		logger.log(Level.INFO, " **** Converting built-in code from html to js **** ");
		logger.log(Level.INFO, " **** Html to convert:  **** ");

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

		logger.log(Level.INFO, "\n\n" + sampleHtml + "\n");

		logger.log(Level.INFO, " **** generated js:  **** ");

		System.out.println(convertService.convert(sampleHtml.toString()));

		// Self closing tags

		/*
		 * When input doesn't end with a slash /> at the end of the tag , it doesn't
		 * work. img looks like a child of input but it's false.
		 */

		/* Jsoup consider self closing tag as the one with a slash /> at the end */

		/*
		 * The program should throw an exception if a non self closing tag like div is
		 * considered as self closing tag ?
		 */

		logger.log(Level.INFO, " **** Self closing tags Not working  **** ");

		String selfClosingTagsNotWorking = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\">";

		logger.log(Level.INFO, "\n\n" + selfClosingTagsNotWorking + "\n");

		logger.log(Level.INFO, " **** generated js:  **** ");

		System.out.println(convertService.convert(selfClosingTagsNotWorking));

		logger.log(Level.INFO, " **** ***********************  **** ");

		String selfClosingTagsNotWorking2 = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\"/>";

		logger.log(Level.INFO, "\n\n" + selfClosingTagsNotWorking2 + "\n");

		logger.log(Level.INFO, " **** generated js:  **** ");

		System.out.println(convertService.convert(selfClosingTagsNotWorking2));

		/* When input ends with a slash /> at the end of the tag , it works */
		logger.log(Level.INFO, " **** Self closing tags working  **** ");

		String selfClosingTagsWorking = "<input type=\"text\"/>\r\n" + "<img src=\"#URL\" alt=\"image\"/>";

		logger.log(Level.INFO, "\n\n" + selfClosingTagsWorking + "\n");

		logger.log(Level.INFO, " **** generated js:  **** ");

		System.out.println(convertService.convert(selfClosingTagsWorking));

		logger.log(Level.INFO, " **** ***********************  **** ");

		String selfClosingTagsWorking2 = "<input type=\"text\"/>\r\n" + "<img src=\"#URL\" alt=\"image\">";

		logger.log(Level.INFO, "\n\n" + selfClosingTagsWorking2 + "\n");

		logger.log(Level.INFO, " **** generated js:  **** ");

		System.out.println(convertService.convert(selfClosingTagsWorking2));

	}

}
