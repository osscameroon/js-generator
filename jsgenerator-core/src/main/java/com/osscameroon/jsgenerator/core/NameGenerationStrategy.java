package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.RandomNameGenerationStrategy;
import com.osscameroon.jsgenerator.core.internal.TypeBasedNameGenerationStrategy;
import lombok.NonNull;

/**
 * NameGenerationStrategy
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 03, 2022 @t 02:46:23
 */
@FunctionalInterface
public interface NameGenerationStrategy {
    String nextName(@NonNull String type);

    static NameGenerationStrategy ofRandom() {
        return new RandomNameGenerationStrategy();
    }

    static NameGenerationStrategy ofTypeBased() {
        return new TypeBasedNameGenerationStrategy();
    }
}
