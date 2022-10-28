package com.osscameroon.jsgenerator.api.domain;

import com.osscameroon.jsgenerator.core.BuiltinVariableNameStrategy;
import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.osscameroon.jsgenerator.core.BuiltinVariableNameStrategy.TYPE_BASED;
import static com.osscameroon.jsgenerator.core.VariableDeclaration.CONST;

@Data
public class Command {
    private String extension = ".jsgenerator.js";
    private String pathPattern = "{{ index }}{{ original }}{{ extension }}";
    @Size(min = 1)
    private List<@NotNull String> inlineContents = new ArrayList<>();
    private String inlinePattern = "inline.{{ index }}{{ extension }}";
    // NOTE: Configuration options down here
    private String targetElementSelector = ":root > body";
    private VariableDeclaration variableDeclaration = CONST;
    private BuiltinVariableNameStrategy variableNameStrategy = TYPE_BASED;

    public Configuration toConfiguration() {
        return new Configuration(targetElementSelector, variableDeclaration, variableNameStrategy.get());
    }
}
