package com.osscameroon.jsgenerator.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Output {
    private String filename;
    private String content;

    public Output(final String content) {
        this(null, content);
    }
}
