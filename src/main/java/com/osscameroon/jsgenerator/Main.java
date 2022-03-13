package com.osscameroon.jsgenerator;

import static com.osscameroon.jsgenerator.service.ConvertService.convertAndPrintBuiltInCodeFromHtmlToJs;
import static com.osscameroon.jsgenerator.service.ConvertService.convertHtmlFiletoJsFile;

import java.util.Arrays;

import com.osscameroon.jsgenerator.service.ConvertService;

/**
 * Main class responsible to launch the app.
 *
 * @author osscameroon
 */
public class Main {

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
	 * file to convert into that folder. If this input folder doesn't exist then the
	 * program will create that. The generated Js files will be located in
	 * "src/main/resources/jsFilesOutput/" folder. By default, the input folder
	 * exists but the output folder don't.</b> The method
	 * {@link com.osscameroon.jsgenerator.service.ConvertService#convertHtmlFiletoJsFile(String)}
	 * is responsible to convert the Html to Js file.
	 * </p>
	 * <p>
	 * Then, the method
	 * {@link com.osscameroon.jsgenerator.service.ConvertService#convertAndPrintBuiltInCodeFromHtmlToJs()}
	 * converts a built-in code from Html to Js and prints the result.
	 * </p>
	 *
	 * @param args the arguments from command line
	 */
	public static void main(String[] args) {

		if (args.length == 0)

			convertHtmlFiletoJsFile("sample.html");

		else

			Arrays.asList(args).stream().forEach(ConvertService::convertHtmlFiletoJsFile);

		convertAndPrintBuiltInCodeFromHtmlToJs();
	}

}
