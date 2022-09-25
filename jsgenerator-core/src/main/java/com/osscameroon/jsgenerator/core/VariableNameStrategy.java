package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.RandomVariableNameStrategy;
import com.osscameroon.jsgenerator.core.internal.TypeBasedVariableNameStrategy;
import lombok.NonNull;

/**
 * VariableNameStrategy
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 03, 2022 @t 02:46:23
 */
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
