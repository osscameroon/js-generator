package com.osscameroon.jsgenerator.service;

/**
 * Provides the convertion logic from Html to Js.
 *
 * @author osscameroon
 *
 */
public interface ConvertService {

	/**
	 * Converts the Html file already located in
	 * "src/main/resources/htmlFilesInput/" to Js file generated in
	 * "src/main/resources/jsFilesOutput/" folder. Concerning this library, by
	 * default, the input folder exists but the output folder don't. Likewise, When
	 * a project will use jsgenerator as dependency or plugin, an input folder will
	 * be created as soon as possible its classpath will contain jsgenerator.
	 *
	 * @param htmlFileName the Html file name
	 */

	void convertHtmlFiletoJsFile(String htmlFileName);

	/**
	 * Converts a built-in code from Html to Js and prints the result. This method
	 * calls {@link #convert(String)} to convert the built-in code.
	 *
	 */

	void convertAndPrintBuiltInCodeFromHtmlToJs();

}