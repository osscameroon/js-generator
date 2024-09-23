package com.osscameroon.jsgenerator.core;

import static com.osscameroon.jsgenerator.core.VariableDeclaration.LET;
import static com.osscameroon.jsgenerator.core.VariableNameStrategy.ofTypeBased;

public class Configuration {
    private static final String ROOT_BODY = ":root > body";

    private String targetElementSelector = ROOT_BODY;
    /**
     * What the browser renders depends on whether we add <strong>document.querySelector(':root > body')</strong> to the output.
     * If added, the browser will render the output successfully, it is useful for debugging purpose,
     * to verify that the js output matches what the html input does.
     * If not, if the user tries to run the output as it is then the browser will not be able to render,it will show a blank page.
     * So, it depends on what the user wants to do with the output.
     *
     * @see <a href="https://jsfiddle.net/">JSFiddle</a>, <a href="https://codepen.io/pen/">CodePen</a> and Browser Console  help to give a quick feedback.
     */

    private boolean querySelectorAdded = true;
    private boolean commentConversionModeActivated = true;
    private VariableDeclaration variableDeclaration = LET;
    private VariableNameStrategy variableNameStrategy = ofTypeBased();

    public Configuration() {
    }

    public Configuration(VariableDeclaration variableDeclaration,
                         boolean querySelectorAdded,
                         boolean commentConversionModeActivated) {
        this.querySelectorAdded = querySelectorAdded;
        this.variableDeclaration = variableDeclaration;
        this.commentConversionModeActivated = commentConversionModeActivated;
    }

    public Configuration(VariableDeclaration variableDeclaration,
                         boolean querySelectorAdded) {
        this.querySelectorAdded = querySelectorAdded;
        this.variableDeclaration = variableDeclaration;
    }

    public Configuration(VariableDeclaration variableDeclaration,
                         VariableNameStrategy variableNameStrategy,
                         boolean querySelectorAdded,
                         boolean commentConversionModeActivated) {
        this.querySelectorAdded = querySelectorAdded;
        this.variableDeclaration = variableDeclaration;
        this.variableNameStrategy = variableNameStrategy;
        this.commentConversionModeActivated = commentConversionModeActivated;
    }

    public Configuration(String targetElementSelector,
                         boolean querySelectorAdded,
                         boolean commentConversionModeActivated,
                         VariableDeclaration variableDeclaration,
                         VariableNameStrategy variableNameStrategy) {
        this.querySelectorAdded = querySelectorAdded;
        this.variableDeclaration = variableDeclaration;
        this.variableNameStrategy = variableNameStrategy;
        this.targetElementSelector = targetElementSelector;
        this.commentConversionModeActivated = commentConversionModeActivated;
    }

    public String getTargetElementSelector() {
        return targetElementSelector;
    }

    public boolean isQuerySelectorAdded() {
        return querySelectorAdded;
    }

    public boolean isCommentConversionModeActivated() {
        return commentConversionModeActivated;
    }

    public VariableDeclaration getVariableDeclaration() {
        return variableDeclaration;
    }

    public VariableNameStrategy getVariableNameStrategy() {
        return variableNameStrategy;
    }
}
