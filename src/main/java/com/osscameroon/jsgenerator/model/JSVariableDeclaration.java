package com.osscameroon.jsgenerator.model;

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

    private final String keyword;

    public String getKeyword() {

        return keyword;
    }

}
