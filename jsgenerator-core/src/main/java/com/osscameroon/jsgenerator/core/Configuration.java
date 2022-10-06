package com.osscameroon.jsgenerator.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration
 *
 * @author Fanon Jupkwo @t jupsfan@gmail.com
 * @since Oct 02, 2022 @t 09:55:40 WEST
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {


    private VariableDeclaration variableDeclaration = VariableDeclaration.LET;
}
