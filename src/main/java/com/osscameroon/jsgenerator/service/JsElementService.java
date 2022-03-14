package com.osscameroon.jsgenerator.service;

import org.jsoup.nodes.Element;

/**
 * @author Fanon JUPKWO
 *
 */
public interface JsElementService {

	/**
	 * Goes through the Jsoup Elements and converts them to JsElement objects.
	 *
	 * @param element Jsoup Element
	 * @return the generated code in JS
	 */
	String parseElement(Element element);

}