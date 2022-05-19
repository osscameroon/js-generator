package com.osscameroon.jsgenerator.util;

/**
 *
 * Provides 2 constants representing the Html input and Js output folders for
 * test environment
 *
 * @author Fanon Jupkwo
 * 
 */
public enum ConstantsTest {

    HTML_SRC_DIR_TEST("src/test/resources/htmlFilesInput/"), JS_DEST_DIR_TEST("src/test/resources/jsFilesOutput/");

    /**
     * @param string
     */
    ConstantsTest(String folder) {
	// TODO Auto-generated constructor stub

	this.folder = folder;
    }

    private String folder;

    public String getFolder() {

	return folder;
    }

}
