package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.VariableNameStrategy;
import org.springframework.lang.NonNull;

import static java.util.UUID.randomUUID;

public class RandomVariableNameStrategy implements VariableNameStrategy {
    @Override
    public String nextName(@NonNull String type) {
        return "_" + randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "_");
    }
}
