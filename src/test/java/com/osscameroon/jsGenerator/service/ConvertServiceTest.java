package com.osscameroon.jsGenerator.service;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static com.osscameroon.jsGenerator.model.Constants.JS_DEST_DIR;

public class ConvertServiceTest {
    @Test
    public void testConvertFile() throws Exception {
        String srcFileName = "sample.html";
        String destFileName = "sample.js";
        ConvertService.convertFile(srcFileName);

        File destFile = new File(JS_DEST_DIR.concat(destFileName));
        Assert.assertTrue(destFile.exists());
    }
}