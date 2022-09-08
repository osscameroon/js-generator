package com.osscameroon.jsgenerator.cli;

import com.osscameroon.jsgenerator.core.Converter;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 22:38:04
 */
public interface Command extends Callable<Integer> {
    List<String> getInlineContents();

    Converter getConverter();

    List<Path> getPaths();

    boolean isTty();
}
