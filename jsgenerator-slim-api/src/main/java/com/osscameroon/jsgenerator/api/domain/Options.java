package com.osscameroon.jsgenerator.api.domain;

import com.osscameroon.jsgenerator.core.BuiltinVariableNameStrategy;
import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.osscameroon.jsgenerator.core.BuiltinVariableNameStrategy.TYPE_BASED;
import static com.osscameroon.jsgenerator.core.VariableDeclaration.LET;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true, builderMethodName = "deprecatedBuilder")
public class Options {
    private String extension = ".jsgenerator.js";
    private String targetElementSelector = ":root > body";

    private boolean querySelectorAdded = true;

    private boolean commentConversionModeActivated = true;
    private VariableDeclaration variableDeclaration = LET;
    private BuiltinVariableNameStrategy variableNameStrategy = TYPE_BASED;

    public Configuration toConfiguration() {
        return new Configuration(targetElementSelector, querySelectorAdded, commentConversionModeActivated, variableDeclaration, variableNameStrategy.get());
    }
}
