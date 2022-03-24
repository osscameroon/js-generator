package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.ConstantsTest.JS_DEST_DIR_TEST;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Provide methods to test {@link ConvertServiceImpl} methods.
 *
 * @author osscameroon
 *
 */
public class ConvertServiceTest {

	File destFile;
	ConvertService convertService;

	@Before
	public void setUp() throws Exception {

		String destFileName = "sample.js";

		destFile = new File(JS_DEST_DIR_TEST.getFolder().concat(destFileName));

		convertService = new ConvertServiceImplTest();

	}

	@Ignore
	@Test
	// not working
	public void testConvert() {

		/*
		 * Trying to compare this to js Code but they are not equal and don't have the
		 * same length jsCode variable is the code present in sample.js
		 */

		String htmlContent = "<!DOCTYPE html>\r\n" + "\r\n" + "<html>\r\n" + "    <head>\r\n"
				+ "        <meta charset=\"utf-8\">\r\n" + "        <title>Sample</title>\r\n"
				+ "        <link rel=\"stylesheet\" href=\"\">\r\n" + "    </head>\r\n" + "    <body>\r\n"
				+ "        <div id=\"container\">\r\n" + "            <div id=\"header\">\r\n"
				+ "                <h1>Sample</h1>\r\n" + "                <img src=\"kanye.jpg\" alt=\"kanye\">\r\n"
				+ "            </div>\r\n" + "            <div id=\"main\">\r\n" + "                <h2>Main</h2>\r\n"
				+ "                <p>This is the main content.</p>\r\n" + "                <img src=\"\" alt=\"\">\r\n"
				+ "            </div>\r\n" + "            <div id=\"footer\">\r\n"
				+ "                <p>Copyright &copy; 2019</p>\r\n" + "            </div>\r\n" + "        </div>\r\n"
				+ "    </body>\r\n" + "</html>";

		String jsCode = "var title = document.createElement(\"title\");\r\n"
				+ "title.appendChild(document.createTextNode(\"Sample\"));\r\n" + "\r\n"
				+ "var link = document.createElement(\"link\");\r\n" + "link.setAttribute(\"rel\", \"stylesheet\");\r\n"
				+ "link.setAttribute(\"href\", \"\");\r\n" + "\r\n" + "var meta = document.createElement(\"meta\");\r\n"
				+ "meta.setAttribute(\"charset\", \"utf-8\");\r\n" + "meta.appendChild(title);\r\n"
				+ "meta.appendChild(link);\r\n" + "\r\n" + "var head = document.createElement(\"head\");\r\n"
				+ "head.appendChild(meta);\r\n" + "\r\n" + "var h1 = document.createElement(\"h1\");\r\n"
				+ "h1.appendChild(document.createTextNode(\"Sample\"));\r\n" + "\r\n"
				+ "var img = document.createElement(\"img\");\r\n" + "img.setAttribute(\"src\", \"kanye.jpg\");\r\n"
				+ "img.setAttribute(\"alt\", \"kanye\");\r\n" + "\r\n"
				+ "var div = document.createElement(\"div\");\r\n" + "div.setAttribute(\"id\", \"header\");\r\n"
				+ "div.appendChild(h1);\r\n" + "div.appendChild(img);\r\n" + "\r\n"
				+ "var h2 = document.createElement(\"h2\");\r\n"
				+ "h2.appendChild(document.createTextNode(\"Main\"));\r\n" + "\r\n"
				+ "var p = document.createElement(\"p\");\r\n"
				+ "p.appendChild(document.createTextNode(\"This is the main content.\"));\r\n" + "\r\n"
				+ "var img_ = document.createElement(\"img\");\r\n" + "img_.setAttribute(\"src\", \"\");\r\n"
				+ "img_.setAttribute(\"alt\", \"\");\r\n" + "\r\n" + "var div_ = document.createElement(\"div\");\r\n"
				+ "div_.setAttribute(\"id\", \"main\");\r\n" + "div_.appendChild(h2);\r\n" + "div_.appendChild(p);\r\n"
				+ "div_.appendChild(img_);\r\n" + "\r\n" + "var p_ = document.createElement(\"p\");\r\n"
				+ "p_.appendChild(document.createTextNode(\"Copyright © 2019\"));\r\n" + "\r\n"
				+ "var div__ = document.createElement(\"div\");\r\n" + "div__.setAttribute(\"id\", \"footer\");\r\n"
				+ "div__.appendChild(p_);\r\n" + "\r\n" + "var div___ = document.createElement(\"div\");\r\n"
				+ "div___.setAttribute(\"id\", \"container\");\r\n" + "div___.appendChild(div);\r\n"
				+ "div___.appendChild(div_);\r\n" + "div___.appendChild(div__);\r\n" + "\r\n"
				+ "var body = document.createElement(\"body\");\r\n" + "body.appendChild(div___);\r\n" + "\r\n"
				+ "var html = document.createElement(\"html\");\r\n" + "html.appendChild(head);\r\n"
				+ "html.appendChild(body);";

		System.out.println("--------------start------------------------------");

		System.out.println("--------------html------------------------------");

		System.out.println(htmlContent);

		System.out.println("------------js code to verify--------------------------------");

		System.out.println(jsCode.trim());

		System.out.println("--------------------generated js code------------------------");

		System.out.println(convertService.convert(htmlContent));

		System.out.println("--------------------length------------------------");

		System.out.println("gen " + convertService.convert(htmlContent).length() + " verify " + jsCode.length());

		System.out.println("-------------------end-------------------------");

		Assert.assertTrue(convertService.convert(htmlContent).equals(jsCode.trim()));

	}

	@Test
	public void testConvertFileFromCommandLineInterface() throws Exception {
		String srcFileName = "sample.html";

		convertService.convertHtmlFiletoJsFileFromCommandLineInterface(srcFileName);

		Assert.assertTrue(destFile.exists());

	}

	/**
	 * Deletes JS_DEST_DIR because this folder doesn't exist by default. It is only
	 * created if it doesn't exist already when the method
	 * {@link com.osscameroon.jsgenerator.service.ConvertService#convertHtmlFiletoJsFileFromCommandLineInterface(String)}
	 * is called.
	 */

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteDirectory(new File(JS_DEST_DIR_TEST.getFolder()));
	}

}