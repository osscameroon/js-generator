package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.ConstantsTest.JS_DEST_DIR_TEST;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

	@Test

	public void testResultFromConvertMethodIsConstant() {

		String divHtml = "<div></div>";

		Assert.assertTrue(convertService.convert(divHtml).equals(convertService.convert(divHtml)));

	}

//Parameterize this test with other tags
	@Test
	public void testConvertDivTagWithNoChild() {

		String divHtml = "<div></div>";
		String divJs = "var div = document.createElement(\"div\");";

		Assert.assertTrue(convertService.convert(divHtml).equals(divJs));

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
	public void tearDown() {
		try {
			FileUtils.deleteDirectory(new File(JS_DEST_DIR_TEST.getFolder()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}