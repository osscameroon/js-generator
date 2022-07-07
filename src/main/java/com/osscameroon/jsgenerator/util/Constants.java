package com.osscameroon.jsgenerator.util;

/**
 * Provides 2 constants representing the Html input and Js output folders
 *
 * @author Fanon Jupkwo
 */
public enum Constants {

    HTML_SRC_DIR("src/main/resources/htmlFilesInput/"), JS_DEST_DIR("src/main/resources/jsFilesOutput/");

    /**
     * @param folder String
     */
    Constants(String folder) {
        this.folder = folder;
    }
    private final String folder;
    public String getFolder() {
        return folder;
    }

}
