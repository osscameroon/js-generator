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
        // NOTE: issue#145 careful with custom element about casing and dash, not to translate in JavaScript identifiers
        var identifier = type;
        final var HAS_DASH = type.contains("-");
        final var IS_ROOT = "targetElement".equals(type);
        final var HAS_UPPER_CASE = !type.chars()
                .allMatch(character -> Character.toLowerCase(character) == character);

        if (!IS_ROOT) {
            identifier = HAS_UPPER_CASE ? type.toLowerCase() : identifier;
            identifier = HAS_DASH
                    ? type.replaceAll("-", "_").replaceAll("_+", "_") : identifier;

            if (HAS_DASH || HAS_UPPER_CASE) {
                identifier = "custom_%s".formatted(identifier);
            }
        }

        return format("%s_%03d", identifier, counters.computeIfAbsent(type, __ -> new AtomicLong(1)).getAndIncrement());
    }
}
