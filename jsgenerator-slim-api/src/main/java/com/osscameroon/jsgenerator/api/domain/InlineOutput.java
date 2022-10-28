package com.osscameroon.jsgenerator.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InlineOutput {
    private String filename;
    private String content;

    public InlineOutput(final String content) {
        this(null, content);
    }
}
