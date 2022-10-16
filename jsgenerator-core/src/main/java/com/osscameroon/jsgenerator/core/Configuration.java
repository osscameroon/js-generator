package com.osscameroon.jsgenerator.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    private VariableDeclaration variableDeclaration = VariableDeclaration.LET;
}
