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
package com.osscameroon.jsgenerator.ante.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FileUtil class contains static methods to read html files and write js files.
 * Can be further extended to read other file types where necessary.
 *
 * @author Elroy Kanye
 */
public class FileUtil {
    /**
     * Reads the html file and returns the content as a string.
     *
     * @param filePath the path of the html file
     * @return the content of the html file
     */
    public static StringBuilder readHtmlFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        // read the pathToHtml using buffered reader
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String contentLine = br.readLine();
            while (contentLine != null) {
                sb.append(contentLine);
                contentLine = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb;
    }

    /**
     * Writes the content to the js file.
     *
     * @param content  the content to be written to the js file
     * @param filePath the path of the js file
     */
    public static void writeJsFile(String content, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
