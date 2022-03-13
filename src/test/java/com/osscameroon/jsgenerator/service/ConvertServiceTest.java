package com.osscameroon.jsgenerator.service;

import static com.osscameroon.jsgenerator.util.Constants.JS_DEST_DIR;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.osscameroon.jsgenerator.service.ConvertService;

public class ConvertServiceTest {
	@Test
	public void testConvertFile() throws Exception {
		String srcFileName = "sample.html";
		String destFileName = "sample.js";
		ConvertService.convertHtmlFiletoJsFile(srcFileName);

		File destFile = new File(JS_DEST_DIR.getFolder().concat(destFileName));
		Assert.assertTrue(destFile.exists());
	}
}