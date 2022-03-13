package com.osscameroon.jsgenerator.util;

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
			;
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
