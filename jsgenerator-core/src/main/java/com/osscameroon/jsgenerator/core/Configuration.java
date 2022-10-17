package com.osscameroon.jsgenerator.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    private String targetElementSelector = ":root > body";
    private VariableDeclaration variableDeclaration = VariableDeclaration.LET;

    public Configuration(final VariableDeclaration variableDeclaration) {
        this(":root > body", variableDeclaration);
    }
}
