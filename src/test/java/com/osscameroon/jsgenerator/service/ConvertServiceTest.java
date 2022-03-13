package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.Constants.JS_DEST_DIR;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConvertServiceTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testConvertFile() throws Exception {
		String srcFileName = "sample.html";

		ConvertService.convertHtmlFiletoJsFile(srcFileName);

		String destFileName = "sample.js";

		// This should use src/test/resources instead

		File destFile = new File(JS_DEST_DIR.getFolder().concat(destFileName));

		Assert.assertTrue(destFile.exists());
	}
}