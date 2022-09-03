package com.osscameroon.jsgenerator.app;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Configuration
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 02, 2022 @t 22:07:10
 */
@Getter
@RequiredArgsConstructor
public class Configuration {
    @NonNull
    private final Output output;
    @NonNull
    private final Input input;

    /**
     * Input
     *
     * @author Salathiel @t salathiel@genese.name
     * @since Sep 02, 2022 @t 22:03:35
     */
    public enum Input {
        INLINE,
        FILES,
        STDIN,
    }

    /**
     * Output
     *
     * @author Salathiel @t salathiel@genese.name
     * @since Sep 02, 2022 @t 22:05:01
     */
    public enum Output {
        STDOUT,
        FILES,
    }
}
