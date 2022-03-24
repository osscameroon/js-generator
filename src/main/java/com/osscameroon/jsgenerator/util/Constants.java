package com.osscameroon.jsgenerator.util;

/**
 * Provides 2 constants representing the Html input and Js output folders
 *
 * @author osscameroon
 *
 */
public enum Constants {

	HTML_SRC_DIR("src/main/resources/htmlFilesInput/"), JS_DEST_DIR("src/main/resources/jsFilesOutput/");

	/**
	 * @param string
	 */
	Constants(String folder) {
		// TODO Auto-generated constructor stub

		this.folder = folder;
	}

	private String folder;

	public String getFolder() {

		return folder;
	}

}
