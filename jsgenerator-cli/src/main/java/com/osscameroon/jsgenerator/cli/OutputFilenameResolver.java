package com.osscameroon.jsgenerator.cli;

import com.osscameroon.jsgenerator.cli.internal.InlineOutputFilenameResolver;
import com.osscameroon.jsgenerator.cli.internal.PathOutputFilenameResolver;
import com.osscameroon.jsgenerator.cli.internal.StdinOutputFilenameResolver;
import lombok.NonNull;

import java.util.Map;

/**
 * OutputFilenameResolver
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 23:03:38
 */
@FunctionalInterface
public interface OutputFilenameResolver {
    String ORIGINAL_DIRECTORY = "original-directory";
    String ORIGINAL_EXTENSION = "original-extension";
    String ORIGINAL_BASENAME = "original-basename";
    String EXTENSION = "extension";
    String ORIGINAL = "original";
    String INDEX = "index";

    String resolve(@NonNull final String template, @NonNull final Map<String, Object> container);

    static OutputFilenameResolver ofInline() {
        return new InlineOutputFilenameResolver();
    }

    static OutputFilenameResolver ofStdin() {
        return new StdinOutputFilenameResolver();
    }

    static OutputFilenameResolver ofPath() {
        return new PathOutputFilenameResolver();
    }
}
