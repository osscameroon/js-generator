package com.osscameroon.jsgenerator.core;

import lombok.NonNull;

import static com.osscameroon.jsgenerator.core.VariableNameStrategy.ofRandom;
import static com.osscameroon.jsgenerator.core.VariableNameStrategy.ofTypeBased;

public enum BuiltinVariableNameStrategy implements VariableNameStrategy {
    TYPE_BASED(ofTypeBased()),
    RANDOM(ofRandom()),
    ;

    private final VariableNameStrategy variableNameStrategy;

    BuiltinVariableNameStrategy(VariableNameStrategy variableNameStrategy) {
        this.variableNameStrategy = variableNameStrategy;
    }

    @Override
    public String nextName(@NonNull String type) {
        return variableNameStrategy.nextName(type);
    }
}
