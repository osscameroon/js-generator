package com.osscameroon.jsgenerator.api.domain;

import com.osscameroon.jsgenerator.core.BuiltinVariableNameStrategy;
import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.VariableDeclaration;

import static com.osscameroon.jsgenerator.core.BuiltinVariableNameStrategy.TYPE_BASED;
import static com.osscameroon.jsgenerator.core.VariableDeclaration.LET;

public sealed class Options<O extends Options<?>> permits InlineOptions, MultipartOptions {
    private BuiltinVariableNameStrategy variableNameStrategy = TYPE_BASED;
    private String pattern = "inline.{{ index }}{{ extension }}";
    private String targetElementSelector = ":root > body";
    private VariableDeclaration variableDeclaration = LET;
    private boolean commentConversionModeActivated = true;
    private String extension = ".jsgenerator.js";
    private boolean querySelectorAdded = true;

    protected Options() {
    }

    public BuiltinVariableNameStrategy getVariableNameStrategy() {
        return variableNameStrategy;
    }

    public O setVariableNameStrategy(BuiltinVariableNameStrategy variableNameStrategy) {
        this.variableNameStrategy = variableNameStrategy;
        //noinspection unchecked
        return (O) this;
    }

    public String getPattern() {
        return pattern;
    }

    public O setPattern(String pattern) {
        this.pattern = pattern;
        //noinspection unchecked
        return (O) this;
    }

    public String getTargetElementSelector() {
        return targetElementSelector;
    }

    public O setTargetElementSelector(String targetElementSelector) {
        this.targetElementSelector = targetElementSelector;
        //noinspection unchecked
        return (O) this;
    }

    public VariableDeclaration getVariableDeclaration() {
        return variableDeclaration;
    }

    public O setVariableDeclaration(VariableDeclaration variableDeclaration) {
        this.variableDeclaration = variableDeclaration;
        //noinspection unchecked
        return (O) this;
    }

    public boolean isCommentConversionModeActivated() {
        return commentConversionModeActivated;
    }

    public O setCommentConversionModeActivated(boolean commentConversionModeActivated) {
        this.commentConversionModeActivated = commentConversionModeActivated;
        //noinspection unchecked
        return (O) this;
    }

    public String getExtension() {
        return extension;
    }

    public O setExtension(String extension) {
        this.extension = extension;
        //noinspection unchecked
        return (O) this;
    }

    public boolean isQuerySelectorAdded() {
        return querySelectorAdded;
    }

    public O setQuerySelectorAdded(boolean querySelectorAdded) {
        this.querySelectorAdded = querySelectorAdded;
        //noinspection unchecked
        return (O) this;
    }

    public Configuration toConfiguration() {
        return new Configuration(
                targetElementSelector, querySelectorAdded,
                commentConversionModeActivated, variableDeclaration, variableNameStrategy.get());
    }
}
