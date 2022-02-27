/*
 *
 */
package com.osscameroon.jsGenerator;

import static com.osscameroon.jsGenerator.service.ConvertService.convertAndPrintBuiltInCodeFromHtmlToJs;
import static com.osscameroon.jsGenerator.service.ConvertService.convertFile;;

/**
 * @author osscameroon
 * @version 1.0
 * @since 1.0 Main class
 */
public class Main {
	public static void main(String[] args) {
		String htmlFileName;

		if (args.length == 0)
			htmlFileName = "sample.html";
		else
			htmlFileName = args[0];

		System.out.println("Converting " + htmlFileName + " to js file");
		convertFile(htmlFileName);
		System.out.println("Conversion complete");

		convertAndPrintBuiltInCodeFromHtmlToJs();
	}

}
