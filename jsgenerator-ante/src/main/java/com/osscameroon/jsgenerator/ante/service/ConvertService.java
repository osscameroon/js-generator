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
package com.osscameroon.jsgenerator.ante.service;

import com.osscameroon.jsgenerator.ante.exception.*;

/**
 * Provides the convertion logic from Html to Js.
 *
 * @author Fanon Jupkwo
 * @author Elroy Kanye
 */
public interface ConvertService {

    /**
     * Converts the Html string to Js string.
     *
     * @param content the Html string
     * @return a String Object containing Js
     * @throws NoHTMLCodeException if content is null
     */

    String convert(String content) throws NoHTMLCodeException;

    /**
     * Converts the Html file already located in
     * "src/main/resources/htmlFilesInput/" to Js file generated in
     * "src/main/resources/jsFilesOutput/" folder. Concerning this library, by
     * default, the input folder exists but the output folder don't. Likewise, When
     * a project will use jsgenerator as dependency or plugin, an input folder will
     * be created as soon as possible its classpath will contain jsgenerator.
     *
     * @param htmlFileName the Html file name
     * @throws NoHTMLFileNameException        if htmlFileName is null
     * @throws IncorrectHTMLFileNameException if htmlFileName is incorrect
     * @throws HTMLFileNotFoundException      if the html file is not found
     */

    void convertHtmlFiletoJsFileFromCommandLineInterface(String htmlFileName)
        throws NoHTMLFileNameException, IncorrectHTMLFileNameException, HTMLFileNotFoundException;

    /**
     * Converts each Html file already located in
     * "src/main/resources/htmlFilesInput/" to Js file generated in
     * "src/main/resources/jsFilesOutput/" folder. Concerning this library, by
     * default, the input folder exists but the output folder don't. Likewise, When
     * a project will use jsgenerator as dependency or plugin, an input folder will
     * be created as soon as possible its classpath will contain jsgenerator.
     *
     * @param args the array of Html files names
     * @throws NoHTMLFileNameException         if args is null
     * @throws EmptyHTMLFilesListException     if args is empty
     * @throws DuplicatedHTMLFileNameException if args contains duplicates
     */

    void convertHtmlFiletoJsFileFromCommandLineInterface(String[] args)
        throws NoHTMLFileNameException, EmptyHTMLFilesListException, DuplicatedHTMLFileNameException;

    /**
     * Converts the Html file to Js file generated in output folder.
     * <p>
     * It is similar to
     * {@link com.osscameroon.jsgenerator.ante.service.ConvertService#convertHtmlFiletoJsFileFromCommandLineInterface(String)}
     * but it will be useful for the web API.
     *
     * @param htmlFileName the Html file name
     * @param outputFolder the output folder containing the generated Js file
     */

    void convertHtmlFiletoJsFileFromWeb(String htmlFileName, String outputFolder);

}
