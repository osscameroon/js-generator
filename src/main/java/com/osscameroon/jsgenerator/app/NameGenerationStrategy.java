package com.osscameroon.jsgenerator.app;

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
}
