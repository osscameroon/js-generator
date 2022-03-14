package com.osscameroon.jsgenerator;

import java.util.Arrays;

import com.osscameroon.jsgenerator.service.ConvertService;
import com.osscameroon.jsgenerator.service.ConvertServiceImpl;

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
	 * file to convert into that folder. If this input folder doesn't exist on a
	 * project using our library jsgenerator then it will be created. The generated
	 * Js files will be located in "src/main/resources/jsFilesOutput/" folder.
	 * Concerning this library, by default, the input folder exists but the output
	 * folder don't. When a project will use jsgenerator as dependency or plugin, an
	 * input folder will be created as soon as possible its classpath will contain
	 * jsgenerator.</b> The method
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

		ConvertService convertService = new ConvertServiceImpl();

		if (args.length == 0)

			convertService.convertHtmlFiletoJsFile("sample.html");

		else

			Arrays.asList(args).stream().forEach(convertService::convertHtmlFiletoJsFile);

		convertService.convertAndPrintBuiltInCodeFromHtmlToJs();
	}

}
