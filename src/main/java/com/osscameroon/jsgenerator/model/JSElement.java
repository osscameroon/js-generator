package com.osscameroon.jsgenerator.model;

import org.jsoup.nodes.Element;

/**
 * Represents a JavaScript Element
 *
 * @author Fanon Jupkwo
 *
 */
public class JSElement {

    private Element element;

    private JSVariableDeclaration jsVariableDeclaration;

    public JSElement(Element element, JSVariableDeclaration jsVariableDeclaration) {

	this.element = element;

	this.jsVariableDeclaration = jsVariableDeclaration;
    }

    public Element getElement() {

	return element;
    }

    public JSVariableDeclaration getJsVariableDeclaration() {

	return jsVariableDeclaration;
    }

}
