package com.osscameroon.jsgenerator.model;

/**
 * Provides 2 ways to declare variables in JavaScript: "var" and "let"
 *
 * @author Fanon Jupkwo
 *
 */
public enum JsVariableDeclaration {

    VAR("var"), LET("let");

    JsVariableDeclaration(String keyword) {

	this.keyword = keyword;

    }

    private String keyword;

    public String getKeyword() {

	return keyword;
    }

}
