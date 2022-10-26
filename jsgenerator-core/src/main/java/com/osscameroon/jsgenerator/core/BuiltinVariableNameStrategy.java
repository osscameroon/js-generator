package com.osscameroon.jsgenerator.core;

import java.util.function.Supplier;

public enum BuiltinVariableNameStrategy implements Supplier<VariableNameStrategy> {
    TYPE_BASED(VariableNameStrategy::ofTypeBased),
    RANDOM(VariableNameStrategy::ofRandom),
    ;

    private final Supplier<VariableNameStrategy> variableNameStrategySupplier;

    BuiltinVariableNameStrategy(final Supplier<VariableNameStrategy> variableNameStrategySupplier) {
        this.variableNameStrategySupplier = variableNameStrategySupplier;
    }

    @Override
    public VariableNameStrategy get() {
        return variableNameStrategySupplier.get();
    }
}
