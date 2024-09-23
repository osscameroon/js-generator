package com.osscameroon.jsgenerator.desktop.controller;

import javafx.scene.Parent;

import java.io.IOException;

@FunctionalInterface
public interface FxmlResolver {
    Parent resolve(String relativePathWithoutExtension) throws IOException;
}
