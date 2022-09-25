package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import lombok.NonNull;

import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * InlineOutputStreamResolver
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 23:08:30
 */
public class InlineOutputStreamResolver implements OutputStreamResolver {
    @Override
    public String resolve(@NonNull String template, @NonNull Map<String, Object> container) {
        return template
            .replaceAll(format("\\{\\{\\s*%s\\s*}}", EXTENSION), valueOf(container.get(EXTENSION)))
            .replaceAll(format("\\{\\{\\s*%s\\s*}}", INDEX), valueOf(container.get(INDEX)));
    }
}
