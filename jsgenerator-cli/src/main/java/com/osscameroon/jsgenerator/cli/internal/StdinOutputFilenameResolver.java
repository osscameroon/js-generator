package com.osscameroon.jsgenerator.cli.internal;

import com.osscameroon.jsgenerator.cli.OutputFilenameResolver;
import lombok.NonNull;

import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * StdinOutputFilenameResolver
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 23:06:28
 */
public class StdinOutputFilenameResolver implements OutputFilenameResolver {
    @Override
    public String resolve(@NonNull String template, @NonNull Map<String, Object> container) {
        return template
            .replaceAll(format("\\{\\{\\s*%s\\s*}}", EXTENSION), valueOf(container.get(EXTENSION)));
    }
}
