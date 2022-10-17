package com.osscameroon.jsgenerator.cli;

import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.VariableDeclaration;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

public interface Command extends Callable<Integer> {
    VariableDeclaration getVariableDeclaration();

    String getTargetElementSelector();

    List<String> getInlineContents();

    Converter getConverter();

    List<Path> getPaths();

    boolean isTty();
}
