package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.VariableNameStrategy;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;

public class TypeBasedVariableNameStrategy implements VariableNameStrategy {
    private final Map<String, AtomicLong> counters = new HashMap<>();

    @Override
    public String nextName(@NonNull String type) {
        return format("%s_%03d", type, counters.computeIfAbsent(type, __ -> new AtomicLong()).getAndIncrement());
    }
}
