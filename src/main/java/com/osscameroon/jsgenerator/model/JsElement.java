package com.osscameroon.jsgenerator.model;

import org.jsoup.nodes.Element;

/**
 * Represents a JavaScript Element
 *
 * @author Fanon Jupkwo
 *
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
