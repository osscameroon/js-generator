package com.osscameroon.jsgenerator.cli;

import com.osscameroon.jsgenerator.core.VariableNameStrategy;

import java.util.function.Supplier;

import static com.osscameroon.jsgenerator.core.VariableNameStrategy.ofRandom;
import static com.osscameroon.jsgenerator.core.VariableNameStrategy.ofTypeBased;

public enum BuiltinVariableNameStrategy implements Supplier<VariableNameStrategy> {
    TYPE_BASED(ofTypeBased()),
    RANDOM(ofRandom()),
    ;

    private final VariableNameStrategy variableNameStrategy;

    BuiltinVariableNameStrategy(VariableNameStrategy variableNameStrategy) {
        this.variableNameStrategy = variableNameStrategy;
    }

    @Override
    public VariableNameStrategy get() {
        return variableNameStrategy;
    }
}
