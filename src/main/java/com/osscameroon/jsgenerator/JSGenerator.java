package com.osscameroon.jsgenerator;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.osscameroon.jsgenerator.exception.EmptyHTMLFilesListException;
import com.osscameroon.jsgenerator.model.JSVariableDeclaration;
import com.osscameroon.jsgenerator.service.ConvertService;
import com.osscameroon.jsgenerator.service.ConvertServiceImpl;

/**
 * Main class responsible to launch the app.
 *
 * @author Sherlock Wisdom
 * @author Fanon Jupkwo
 * @author Elroy Kanye
 *
 *
 */
public class JSGenerator {

    private static final Logger logger = Logger.getLogger(JSGenerator.class.getName());

    // Just choose between VAR or LET for your variable declarations

    // static ConvertService convertService = new
    // ConvertServiceImpl(JsVariableDeclaration.VAR);

    static ConvertService convertService = new ConvertServiceImpl(JSVariableDeclaration.LET);

    /**
     * <p>
     * This method launches the app.
     * </p>
     *
     * <p>
     *
     * First, if there are no arguments, then the app will convert a built-in Html
     * file "src/main/resources/htmlFilesInput/sample.html" to a Js file. Else, the
     * app will convert all arguments representing Html files. <b>The program
     * supposes that these Html files are already located in
     * "src/main/resources/htmlFilesInput/" folder, so make sure to put the html
     * file to convert into that folder. If this input folder doesn't exist on a
     * project using our library jsgenerator then it will be created. The generated
     * Js files will be located in "src/main/resources/jsFilesOutput/" folder.
     * Concerning this library, by default, the input folder exists but the output
     * folder don't. When a project will use jsgenerator as dependency, an input
     * folder will be created as soon as possible its classpath will contain
     * jsgenerator.</b>
     *
     * The method
     * {@link com.osscameroon.jsgenerator.service.ConvertService#convertHtmlFiletoJsFileFromCommandLineInterface(String)}
     * is responsible to convert the Html to Js file.
     * </p>
     *
     * <p>
     * Then, the method
     * {@link com.osscameroon.jsgenerator.JSGenerator#convertAndPrintBuiltInCodeFromHtmlToJs()}
     * converts a built-in code from Html to Js and prints the result.
     * </p>
     *
     * @param args the arguments from command line
     */
    public static void main(String[] args) {

	try {

	    convertService.convertHtmlFiletoJsFileFromCommandLineInterface(args);

	} catch (EmptyHTMLFilesListException e) {

	    convertService.convertHtmlFiletoJsFileFromCommandLineInterface("sample.html");

	}

	// The final release will not contain this line, TODO: create an example branch

	convertAndPrintBuiltInCodeFromHtmlToJs();

    }

    /**
     * Converts built-in code from Html to Js and prints the result.
     */
    static void convertAndPrintBuiltInCodeFromHtmlToJs() {

	// Use log instead of system.out.println to show steps
	logger.log(Level.INFO, " **** Converting built-in code from html to js **** ");
	logger.log(Level.INFO, " **** Html to convert:  **** ");

	// Copy the html code into the variable named html then run the program.

	String html = "";

	logger.log(Level.INFO, "\n\n" + html + "\n");

	logger.log(Level.INFO, " **** generated js:  **** " + "\n");

	System.out.println(convertService.convert(html));

    }

}
