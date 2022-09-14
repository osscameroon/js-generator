package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import lombok.NonNull;

import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * PathOutputStreamResolver
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 23:08:53
 */
public class PathOutputStreamResolver implements OutputStreamResolver {
    @Override
    public String resolve(@NonNull String template, @NonNull Map<String, Object> container) {
        System.out.printf("template=%s, container=%s%n", template, container);
        return template
            .replaceAll(format("\\{\\{\\s*%s\\s*}}", ORIGINAL_DIRECTORY), valueOf(container.get(ORIGINAL_DIRECTORY)))
            .replaceAll(format("\\{\\{\\s*%s\\s*}}", ORIGINAL_EXTENSION), valueOf(container.get(ORIGINAL_EXTENSION)))
            .replaceAll(format("\\{\\{\\s*%s\\s*}}", ORIGINAL_BASENAME), valueOf(container.get(ORIGINAL_BASENAME)))
            .replaceAll(format("\\{\\{\\s*%s\\s*}}", EXTENSION), valueOf(container.get(EXTENSION)))
            .replaceAll(format("\\{\\{\\s*%s\\s*}}", ORIGINAL), valueOf(container.get(ORIGINAL)));
    }
}
