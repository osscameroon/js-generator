package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.VariableNameStrategy;
import lombok.NonNull;

import static java.util.UUID.randomUUID;

/**
 * RandomVariableNameStrategy
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 03, 2022 @t 02:48:32
 */
public class RandomVariableNameStrategy implements VariableNameStrategy {
    @Override
    public String nextName(@NonNull String type) {
        return "_" + randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "_");
    }
}
