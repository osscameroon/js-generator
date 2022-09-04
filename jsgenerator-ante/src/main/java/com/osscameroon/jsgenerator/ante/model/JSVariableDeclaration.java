package com.osscameroon.jsgenerator.ante.model;

/**
 * Provides 2 ways to declare variables in JavaScript: "var" and "let"
 *
 * @author Fanon Jupkwo
 */
public enum JSVariableDeclaration {

    VAR("var"), LET("let");

    JSVariableDeclaration(String keyword) {

        this.keyword = keyword;

    }

    private String keyword;

    public String getKeyword() {

        return keyword;
    }

}
