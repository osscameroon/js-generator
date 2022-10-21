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
    private String targetElementSelector = ":root > body";
    private VariableDeclaration variableDeclaration = LET;
    private VariableNameStrategy variableNameStrategy = ofTypeBased();
    /**
     * Whether or not we add document.querySelector(':root > body'); to the output or not.
     * If added, the browser will render the output successfully, it is useful for debugging purpose,
     * to verify that the js output matches what the html input does.
     * If not, if the user tries to run the output as it is then the browser will not be able to render,
     * it will show a blank page.
     * So, it depends on what the user wants to do with the output.
     * In order to check how the browser will render,
     * https://jsfiddle.net/ and https://codepen.io/pen/ help to give a quick rendering.
     * */
    private boolean hasQuerySelector=true;

    public Configuration(final VariableDeclaration variableDeclaration) {
        this(variableDeclaration, ofTypeBased());
    }

    public Configuration(final String targetElementSelector,
                         final VariableDeclaration variableDeclaration) {
        this(targetElementSelector, variableDeclaration, ofTypeBased(),true);
    }

    public Configuration(final VariableDeclaration variableDeclaration,
                         final VariableNameStrategy variableNameStrategy) {
        this(":root > body", variableDeclaration, variableNameStrategy,true);
    }
}
