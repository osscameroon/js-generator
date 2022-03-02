package com.osscameroon.jsGenerator.service;

import static com.osscameroon.jsGenerator.util.Constants.JS_DEST_DIR;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class ConvertServiceTest {
	@Test
	public void testConvertFile() throws Exception {
		String srcFileName = "sample.html";
		String destFileName = "sample.js";
		ConvertService.convertFile(srcFileName);

		File destFile = new File(JS_DEST_DIR.getFolder().concat(destFileName));
		Assert.assertTrue(destFile.exists());
	}
}