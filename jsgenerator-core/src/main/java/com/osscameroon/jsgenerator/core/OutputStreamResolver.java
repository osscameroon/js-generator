package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.InlineOutputStreamResolver;
import com.osscameroon.jsgenerator.core.internal.PathOutputStreamResolver;
import com.osscameroon.jsgenerator.core.internal.StdinOutputStreamResolver;
import org.springframework.lang.NonNull;

import java.util.Map;

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
