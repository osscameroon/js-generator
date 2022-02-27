/*
 *
 */
package com.osscameroon.jsGenerator;

import static com.osscameroon.jsGenerator.service.ConvertService.convertAndPrintBuiltInCodeFromHtmlToJs;
import static com.osscameroon.jsGenerator.service.ConvertService.convertFiles;;

/**
 * @author osscameroon
 * @version 1.0
 * @since 1.0 Main class
 */
public class Main {
	public static void main(String[] args) {
		String htmlFile;

		if (args.length == 0)
			htmlFile = "sample.html";
		else
			htmlFile = args[0];

		System.out.println("Converting " + htmlFile + " to js file");
		convertFiles(htmlFile);
		System.out.println("Conversion complete");

		convertAndPrintBuiltInCodeFromHtmlToJs();
	}

}
