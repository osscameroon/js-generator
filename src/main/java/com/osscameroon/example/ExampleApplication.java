package com.osscameroon.example;

import com.osscameroon.jsgenerator.exception.EmptyHTMLFilesListException;
import com.osscameroon.jsgenerator.model.JSVariableDeclaration;
import com.osscameroon.jsgenerator.service.ConvertService;
import com.osscameroon.jsgenerator.service.ConvertServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleApplication {
    private static final Logger logger = Logger.getLogger(ExampleApplication.class.getName());

    // Just choose between VAR or LET for your variable declarations

    static ConvertService convertService = new ConvertServiceImpl(JSVariableDeclaration.LET);

    // Should I present the 3 key features here ?

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
     * {@link com.osscameroon.example.ExampleApplication#convertAndPrintBuiltInCodeFromHtmlToJs()}
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
     *
     *
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

        logger.log(Level.INFO, " **** Self closing tags without slash   **** ");

        String selfClosingTagInputWithoutSlashHtml = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\">";

        logger.log(Level.INFO, "\n\n" + selfClosingTagInputWithoutSlashHtml + "\n");

        logger.log(Level.INFO, " **** generated js  **** ");

        System.out.println(convertService.convert(selfClosingTagInputWithoutSlashHtml));

        logger.log(Level.INFO, " **** ***********************  **** ");

        String selfClosingTagInputWithoutSlashHtml2 = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\"/>";

        logger.log(Level.INFO, "\n\n" + selfClosingTagInputWithoutSlashHtml2 + "\n");

        logger.log(Level.INFO, " **** generated js  **** ");

        System.out.println(convertService.convert(selfClosingTagInputWithoutSlashHtml2).replace("\n", ""));

        logger.log(Level.INFO, " **** Self closing tags with slash  **** ");

        String selfClosingTagInputWithSlashHtml = "<input type=\"text\"/>\r\n" + "<img src=\"#URL\" alt=\"image\"/>";

        logger.log(Level.INFO, "\n\n" + selfClosingTagInputWithSlashHtml + "\n");

        logger.log(Level.INFO, " **** generated js  **** ");

        System.out.println(convertService.convert(selfClosingTagInputWithSlashHtml).replace("\n", ""));

        logger.log(Level.INFO, " **** ***********************  **** ");

        String selfClosingTagInputWithSlashHtml2 = "<input type=\"text\"/>\r\n" + "<img src=\"#URL\" alt=\"image\">";

        logger.log(Level.INFO, "\n\n" + selfClosingTagInputWithSlashHtml2 + "\n");

        logger.log(Level.INFO, " **** generated js  **** ");

        System.out.println(convertService.convert(selfClosingTagInputWithSlashHtml2));

        logger.log(Level.INFO, " **** Input With No Tag  **** ");

        String inputWithNoTag = "DELETE *";

        System.out.println("Result of Input With No Tag -> " + convertService.convert(inputWithNoTag));

    }
}
