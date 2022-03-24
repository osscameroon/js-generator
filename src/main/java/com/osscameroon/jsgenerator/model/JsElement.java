package com.osscameroon.jsgenerator.model;

import org.jsoup.nodes.Element;

/**
 * @author osscameroon
 * @version 1.0 Represents a JS Element
 */
public class JsElement {

	private Element element;

	public JsElement(Element element) {

		this.element = element;
	}

	public Element getElement() {

		return element;
	}

}
