package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.NameGenerationStrategy;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;

/**
 * TypeBasedNameGenerationStrategy
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 03, 2022 @t 02:48:58
 */
public class TypeBasedNameGenerationStrategy implements NameGenerationStrategy {
    private final Map<String, AtomicLong> counters = new HashMap<>();

    @Override
    public String nextName(@NonNull String type) {
        return format("%s_%03d", type, counters.computeIfAbsent(type, __ -> new AtomicLong()).getAndIncrement());
    }
}
