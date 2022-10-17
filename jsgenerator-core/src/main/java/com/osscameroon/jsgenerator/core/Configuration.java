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

    public Configuration(final VariableDeclaration variableDeclaration) {
        this(variableDeclaration, ofTypeBased());
    }

    public Configuration(final String targetElementSelector,
                         final VariableDeclaration variableDeclaration) {
        this(targetElementSelector, variableDeclaration, ofTypeBased());
    }

    public Configuration(final VariableDeclaration variableDeclaration,
                         final VariableNameStrategy variableNameStrategy) {
        this(":root > body", variableDeclaration, variableNameStrategy);
    }
}
