package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.RandomVariableNameStrategy;
import com.osscameroon.jsgenerator.core.internal.TypeBasedVariableNameStrategy;
import org.springframework.lang.NonNull;

@FunctionalInterface
public interface VariableNameStrategy {
    String nextName(@NonNull String type);

    static VariableNameStrategy ofRandom() {
        return new RandomVariableNameStrategy();
    }

    static VariableNameStrategy ofTypeBased() {
        return new TypeBasedVariableNameStrategy();
    }
}
