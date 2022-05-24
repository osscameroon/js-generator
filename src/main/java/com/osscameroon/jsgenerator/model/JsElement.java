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

    private JsVariableDeclaration jsVariableDeclaration;

    public JsElement(Element element, JsVariableDeclaration jsVariableDeclaration) {

	this.element = element;

	this.jsVariableDeclaration = jsVariableDeclaration;
    }

    public Element getElement() {

	return element;
    }

    public JsVariableDeclaration getJsVariableDeclaration() {

	return jsVariableDeclaration;
    }

}
