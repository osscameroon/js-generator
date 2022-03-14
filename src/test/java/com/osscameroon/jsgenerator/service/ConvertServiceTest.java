package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.Constants.JS_DEST_DIR;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConvertServiceTest {

	File destFile;

	@Before
	public void setUp() throws Exception {

		String destFileName = "sample.js";

		// This should use src/test/resources instead

		destFile = new File(JS_DEST_DIR.getFolder().concat(destFileName));

	}

	/**
	 * Deletes JS_DEST_DIR because this folder doesn't exist by default. It is only
	 * created if it doesn't exist already when the method
	 * {@link com.osscameroon.jsgenerator.service.ConvertService#convertHtmlFiletoJsFile(String)}
	 * is called.
	 */

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteDirectory(new File(JS_DEST_DIR.getFolder()));
	}

	@Test
	public void testConvertFile() throws Exception {
		String srcFileName = "sample.html";

		ConvertService.convertHtmlFiletoJsFile(srcFileName);

		Assert.assertTrue(destFile.exists());

	}
}