package com.osscameroon.jsgenerator.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.osscameroon.jsgenerator.core.VariableDeclaration.LET;
import static com.osscameroon.jsgenerator.core.VariableNameStrategy.ofTypeBased;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public Configuration(final VariableDeclaration variableDeclaration) {
        this(variableDeclaration, ofTypeBased());
    }

    public Configuration(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) {
        this(variableDeclaration, ofTypeBased(), querySelectorAdded, commentConversionModeActivated);
    }

    public Configuration(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded) {
        this(variableDeclaration, ofTypeBased(), querySelectorAdded, true);
    }


    public Configuration(final String targetElementSelector,
                         final VariableDeclaration variableDeclaration) {
        this(targetElementSelector, true, true, variableDeclaration, ofTypeBased());
    }

    public Configuration(final String targetElementSelector,
                         final VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) {
        this(targetElementSelector, querySelectorAdded, commentConversionModeActivated, variableDeclaration, ofTypeBased());
    }

    public Configuration(final VariableDeclaration variableDeclaration,
                         final VariableNameStrategy variableNameStrategy) {
        this(ROOT_BODY, true, true, variableDeclaration, variableNameStrategy);
    }

    public Configuration(final VariableDeclaration variableDeclaration,
                         final VariableNameStrategy variableNameStrategy, final boolean querySelectorAdded, final boolean commentConversionModeActivated) {
        this(ROOT_BODY, querySelectorAdded, commentConversionModeActivated, variableDeclaration, variableNameStrategy);
    }
}