package com.osscameroon.jsgenerator.service;

import com.osscameroon.jsgenerator.exception.HTMLUnknownElementException;
import com.osscameroon.jsgenerator.model.JSVariableDeclaration;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.osscameroon.jsgenerator.util.ConstantsTest.HTML_SRC_DIR_TEST;
import static com.osscameroon.jsgenerator.util.ConstantsTest.JS_DEST_DIR_TEST;
import static org.junit.Assert.assertEquals;

/**
 * Provide methods to test {@link ConvertServiceImpl} methods.
 *
 * @author Fanon Jupkwo
 */
public class ConvertServiceTest {

    // Parameterized tests

    private static final Logger logger = Logger.getLogger(ConvertServiceTest.class.getName());

    File inputFile;

    File destFile;

    ConvertService convertWithVARService;

    @Before
    public void setUp() {

        String destFileName = "sample.js";
        String inputFileName = "sample.html";

        destFile = new File(JS_DEST_DIR_TEST.getFolder().concat(destFileName));

        inputFile = new File(HTML_SRC_DIR_TEST.getFolder().concat(inputFileName));

        convertWithVARService = new ConvertServiceImplTest(JSVariableDeclaration.VAR);

    }

    /**
     * Before, the result of convert method with same parameter changed everytime if
     * we used the same object to call this function. The result of convert with
     * same parameter was constant if we used different objects. To avoid this
     * issue, we were forced to create different objects but that's not how it
     * should be done.
     * <p>
     * Now, the program clears the list of used tags in order to always get an empty
     * list when we call this method.
     * <p>
     * usedTags.clear();
     * <p>
     * Just look at it here
     * {@link com.osscameroon.jsgenerator.service.ConvertServiceImpl#convert(String)}
     */
    @Test

    public void testResultFromConvertMethodIsConstant() {

        String divHtml = "<div></div>";

        String divWithChildHtml = "<div><div></div></div>";

        StringBuilder longHtml = new StringBuilder();

        longHtml.append("<!-- Button trigger modal -->\n").append(
                        "<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" " +
								"data-target=\"#exampleModal\">\n")
                .append("  Launch demo modal\n").append("</button>\n").append("\n").append("<!-- Modal -->\n")
                .append("<div class=\"modal fade\" id=\"exampleModal\" tabindex=\"-1\" role=\"dialog\" " +
						"aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n")
                .append("  <div class=\"modal-dialog\" role=\"document\">\n")
                .append("    <div class=\"modal-content\">\n").append("      <div class=\"modal-header\">\n")
                .append("        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Modal title</h5>\n")
                .append("        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" " +
						"aria-label=\"Close\">\n")
                .append("          <span aria-hidden=\"true\">&times;</span>\n").append("        </button>\n")
                .append("      </div>\n").append("      <div class=\"modal-body\">\n").append("        ...\n")
                .append("      </div>\n").append("      <div class=\"modal-footer\">\n")
                .append("        <button type=\"button\" class=\"btn btn-secondary\" " +
						"data-dismiss=\"modal\">Close</button>\n")
                .append("        <button type=\"button\" class=\"btn btn-primary\">Save changes</button>\n")
                .append("      </div>\n").append("    </div>\n").append("  </div>\n").append("</div>");

        Assert.assertTrue(convertWithVARService.convert(divHtml).equals(convertWithVARService.convert(divHtml)));

        Assert.assertTrue(convertWithVARService.convert(divWithChildHtml)
                .equals(convertWithVARService.convert(divWithChildHtml)));

        Assert.assertTrue(convertWithVARService.convert(longHtml.toString())
                .equals(convertWithVARService.convert(longHtml.toString())));

    }

    // Parameterize this test with other tags ?

    @Test
    public void testConvertDivTagWithNoChild() {

        String divHtml = "<div></div>";
        String divJs = "var div = document.createElement(\"div\");";

        Assert.assertTrue(convertWithVARService.convert(divHtml).equals(divJs));

    }

    // Parameterize this test with other tags ?

    @Test

    public void testConvertDivTagWithChild() {

        String divWithChildHtml = "<div><div></div></div>";

        String divWithChildJs = "var div = document.createElement(\"div\");var div_ = document.createElement(\"div\")" +
				";div_.appendChild(div);";

        logger.log(Level.INFO, "----------------testConvertDivTagWithChild with line breaks--------------------");

        System.out.println(convertWithVARService.convert(divWithChildHtml));

        logger.log(Level.INFO, "----------------testConvertDivTagWithChild without line breaks--------------------");

        System.out.println(convertWithVARService.convert(divWithChildHtml).replace("\n", ""));

        /*
         * As we all know in Js, line break between 2 instructions ended by comma have
         * no value, it's just for readability. In order to compare Strings easily we
         * delete line breaks
         */

        Assert.assertTrue(convertWithVARService.convert(divWithChildHtml).replace("\n", "").equals(divWithChildJs));

    }

    @Test
    public void testConvertFileFromCommandLineInterface() {

        Assert.assertTrue(inputFile.exists());

        // The generated Js file should not exist before the convertion but after

        Assert.assertFalse(destFile.exists());

        String srcFileName = "sample.html";

        convertWithVARService.convertHtmlFiletoJsFileFromCommandLineInterface(srcFileName);

        // The generated Js file exists after the convertion

        Assert.assertTrue(destFile.exists());

    }

    @Test
    public void testSelfClosingTagWithoutSlashIssue() {

        /*
         * Self closing tags Issue
         *
         * <input type="text"> <img src="#URL" alt="image">
         *
         * ------------------------------------------------------
         *
         * <input type="text"/> <img src="#URL" alt="image">
         *
         * When input doesn't end with a slash /> at the end of the tag , it doesn't
         * work because img looks like a child of input but it's false.
         *
         * Jsoup considers the ones with and without a slash /> at the end as self
         * closing tags.
         *
         * But the one without slash could have children and that's false.
         *
         * To correct that, we updated the method String appendChild(JsElement
         * jsElement) in ConvertServiceImpl, just take a look at it.
         *
         * Useful links:
         *
         * https://www.educba.com/types-of-tags-in-html/
         *
         * https://www.tutorialstonight.com/self-closing-tags-in-html.php#:~:text=HTML%
         * 20Self%20Closing%20Tag,%2C%20etc.
         *
         *
         */

        /*
         * When there is an element with child, the program creates the Js Children
         * variables before parent. In this case, it creates img before input
         */

        logger.log(Level.INFO, " **** Self closing tags without slash   **** ");

        String selfClosingTagInputWithoutSlashHtml = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\">";

        String selfClosingTagInputWithoutSlashJs = "var img = document.createElement(\"img\");img.setAttribute" +
				"(\"src\", \"#URL\");img.setAttribute(\"alt\", \"image\");var input = document.createElement" +
				"(\"input\");input.setAttribute(\"type\", \"text\");";

        logger.log(Level.INFO, "\n\n" + selfClosingTagInputWithoutSlashHtml + "\n");

        logger.log(Level.INFO, " **** generated js without line breaks:  **** ");

        System.out.println(convertWithVARService.convert(selfClosingTagInputWithoutSlashHtml).replace("\n", ""));

        assertEquals("error", selfClosingTagInputWithoutSlashJs,
                convertWithVARService.convert(selfClosingTagInputWithoutSlashHtml).replace("\n", ""));

        logger.log(Level.INFO, " **** ***********************  **** ");

        String selfClosingTagInputWithoutSlashHtml2 = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\"/>";

        logger.log(Level.INFO, "\n\n" + selfClosingTagInputWithoutSlashHtml2 + "\n");

        logger.log(Level.INFO, " **** generated js without line breaks:  **** ");

        System.out.println(convertWithVARService.convert(selfClosingTagInputWithoutSlashHtml2).replace("\n", ""));

        assertEquals("error", selfClosingTagInputWithoutSlashJs,
                convertWithVARService.convert(selfClosingTagInputWithoutSlashHtml2).replace("\n", ""));

        /*
         * When input ends with a slash /> at the end of the tag , it works perfectly.
         */

        /*
         * When there is an element without child, the program creates the Js variable
         * of this element before moving forward. In this case, it creates input before
         * img
         */
        logger.log(Level.INFO, " **** Self closing tags with slash  **** ");

        String selfClosingTagInputWithSlashHtml = "<input type=\"text\"/>\r\n" + "<img src=\"#URL\" alt=\"image\"/>";

        String selfClosingTagInputWithSlashJs = "var input = document.createElement(\"input\");input.setAttribute" +
				"(\"type\", \"text\");var img = document.createElement(\"img\");img.setAttribute(\"src\", \"#URL\");" +
				"img.setAttribute(\"alt\", \"image\");";

        logger.log(Level.INFO, "\n\n" + selfClosingTagInputWithSlashHtml + "\n");

        logger.log(Level.INFO, " **** generated js without line breaks:  **** ");

        System.out.println(convertWithVARService.convert(selfClosingTagInputWithSlashHtml).replace("\n", ""));

        assertEquals("error", selfClosingTagInputWithSlashJs,
                convertWithVARService.convert(selfClosingTagInputWithSlashHtml).replace("\n", ""));

        logger.log(Level.INFO, " **** ***********************  **** ");

        String selfClosingTagInputWithSlashHtml2 = "<input type=\"text\"/>\r\n" + "<img src=\"#URL\" alt=\"image\">";

        logger.log(Level.INFO, "\n\n" + selfClosingTagInputWithSlashHtml2 + "\n");

        logger.log(Level.INFO, " **** generated js without line breaks:  **** ");

        System.out.println(convertWithVARService.convert(selfClosingTagInputWithSlashHtml2).replace("\n", ""));

        assertEquals("error", selfClosingTagInputWithSlashJs,
                convertWithVARService.convert(selfClosingTagInputWithSlashHtml2).replace("\n", ""));

    }

    /**
     * This program throws an exception if the element is not a valid HTML tag
     *
     * @throws HTMLUnknownElementException if the element is not a valid HTML tag
     */

    @Test(expected = HTMLUnknownElementException.class)

    public void testCustomTag() {

        String fanHtml = "<fan></fan>";

        logger.log(Level.INFO, " **** Custom Tags   **** ");

        logger.log(Level.INFO, "\n\n" + fanHtml + "\n");

        convertWithVARService.convert(fanHtml);

    }

    /**
     * Free resources
     * <p>
     * Deletes JS_DEST_DIR because this folder doesn't exist by default. It is only
     * created if it doesn't exist already when the method
     * {@link com.osscameroon.jsgenerator.service.ConvertService#convertHtmlFiletoJsFileFromCommandLineInterface(String)}
     * is called.
     */

    @After
    public void tearDown() {

        convertWithVARService = null;

        try {
            FileUtils.deleteDirectory(new File(JS_DEST_DIR_TEST.getFolder()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}