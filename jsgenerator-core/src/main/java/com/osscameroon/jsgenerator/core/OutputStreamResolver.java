package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.InlineOutputStreamResolver;
import com.osscameroon.jsgenerator.core.internal.PathOutputStreamResolver;
import com.osscameroon.jsgenerator.core.internal.StdinOutputStreamResolver;
import lombok.NonNull;

import java.util.Map;

/**
 * OutputStreamResolver
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 23:03:38
 */
@FunctionalInterface
public interface OutputStreamResolver {
    String ORIGINAL_DIRECTORY = "original-directory";
    String ORIGINAL_EXTENSION = "original-extension";
    String ORIGINAL_BASENAME = "original-basename";
    String EXTENSION = "extension";
    String ORIGINAL = "original";
    String INDEX = "index";

    String resolve(@NonNull final String template, @NonNull final Map<String, Object> container);

    static OutputStreamResolver ofInline() {
        return new InlineOutputStreamResolver();
    }

    static OutputStreamResolver ofStdin() {
        return new StdinOutputStreamResolver();
    }

    static OutputStreamResolver ofPath() {
        return new PathOutputStreamResolver();
    }
}
