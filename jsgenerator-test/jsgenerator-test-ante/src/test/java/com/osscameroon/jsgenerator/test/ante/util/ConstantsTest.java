/*MIT License

Copyright (c) 2020 OSS Cameroon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/
package com.osscameroon.jsgenerator.test.ante.util;

/**
 * Provides 2 constants representing the Html input and Js output folders for
 * test environment
 *
 * @author Fanon Jupkwo
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
