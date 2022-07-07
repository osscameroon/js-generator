package com.osscameroon.jsgenerator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;

/**
 * Represents a JavaScript Element
 *
 * @author Fanon Jupkwo
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JSElement {

    private Element element;
    private JSVariableDeclaration jsVariableDeclaration;
}
